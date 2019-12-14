package com.jereksel.jailbreakk

import org.jetbrains.kotlin.codegen.OperationStackValue
import org.jetbrains.kotlin.codegen.StackValue
import org.jetbrains.kotlin.codegen.asmType
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.codegen.state.KotlinTypeMapper
import org.jetbrains.kotlin.resolve.calls.callUtil.getReceiverExpression
import org.jetbrains.kotlin.resolve.calls.inference.returnTypeOrNothing
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.jvm.AsmTypes.JAVA_STRING_TYPE
import org.jetbrains.kotlin.resolve.jvm.AsmTypes.OBJECT_TYPE
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.Type.*
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter
import java.lang.reflect.Field

class JailbreakkExpressionCodegenExtension : ExpressionCodegenExtension {

    companion object {

        val CLASS_TYPE = getType(Class::class.java)!!
        val FIELD_TYPE = getType(Field::class.java)!!

        val BOOLEAN_OBJECT_TYPE = getType(java.lang.Boolean::class.java)!!
        val BYTE_OBJECT_TYPE = getType(java.lang.Byte::class.java)!!
        val CHAR_OBJECT_TYPE = getType(java.lang.Character::class.java)!!
        val DOUBLE_OBJECT_TYPE = getType(java.lang.Double::class.java)!!
        val FLOAT_OBJECT_TYPE = getType(java.lang.Float::class.java)!!
        val INT_OBJECT_TYPE = getType(java.lang.Integer::class.java)!!
        val LONG_OBJECT_TYPE = getType(java.lang.Long::class.java)!!
        val SHORT_OBJECT_TYPE = getType(java.lang.Short::class.java)!!

        private val primitiveToWrapper: Map<Type, Type> = mapOf(
                BOOLEAN_TYPE to BOOLEAN_OBJECT_TYPE,
                BYTE_TYPE to BYTE_OBJECT_TYPE,
                CHAR_TYPE to CHAR_OBJECT_TYPE,
                DOUBLE_TYPE to DOUBLE_OBJECT_TYPE,
                FLOAT_TYPE to FLOAT_OBJECT_TYPE,
                INT_TYPE to INT_OBJECT_TYPE,
                LONG_TYPE to LONG_OBJECT_TYPE,
                SHORT_TYPE to SHORT_OBJECT_TYPE
        )

        fun Type.toWrapper(): Type = primitiveToWrapper[this] ?: error("Type $this is not primitive type")

    }

    override fun applyProperty(receiver: StackValue, resolvedCall: ResolvedCall<*>, c: ExpressionCodegenExtension.Context): StackValue? {
        val candidateDescriptor = resolvedCall.candidateDescriptor
        if (candidateDescriptor !is JailbreakkPropertyDescriptor) {
            return super.applyProperty(receiver, resolvedCall, c)
        }

        val containingClass = candidateDescriptor.originalType.asmType(c.typeMapper)
        val fieldName = candidateDescriptor.originalName.toString()
        val fieldType = candidateDescriptor.type.asmType(c.typeMapper)

        return OperationStackValue(fieldType, candidateDescriptor.type) {
            it.aconst(containingClass)
            it.aconst(fieldName)
            it.invokevirtual("java/lang/Class", "getDeclaredField", getMethodDescriptor(FIELD_TYPE, JAVA_STRING_TYPE), false)

            it.dup()
            it.iconst(1)
            it.invokevirtual("java/lang/reflect/Field", "setAccessible", "(Z)V", false)

            c.codegen.gen(resolvedCall.getReceiverExpression()!!).put(it)

            it.performGetField(candidateDescriptor.type, c.typeMapper)

        }

    }

    override fun applyFunction(receiver: StackValue, resolvedCall: ResolvedCall<*>, c: ExpressionCodegenExtension.Context): StackValue? {
        println("FUNCTION")
        val candidateDescriptor = resolvedCall.candidateDescriptor
        if (candidateDescriptor !is JailbreakkSimpleFunctionDescriptor) {
            return super.applyFunction(receiver, resolvedCall, c)
        }
        val returnType = candidateDescriptor.returnType!!

        val parameters = resolvedCall.valueArgumentsByIndex
                .orEmpty()
                .map {
                    c.codegen.gen(it.arguments.first().getArgumentExpression())!!
                }

        val objectType = getType(java.lang.Object::class.java)
        val classType = getType(Class::class.java)

        return OperationStackValue(returnType.asmType(c.typeMapper), returnType) {

            it.aconst(candidateDescriptor.classType.asmType(c.typeMapper))
            it.aconst(candidateDescriptor.name.asString())
            it.aconst(candidateDescriptor.valueParameters.size)
            it.newarray(classType)

            candidateDescriptor.valueParameters.forEachIndexed { i, valueParameterDescriptor ->
                it.dup()
                it.iconst(i)
                it.putType(valueParameterDescriptor.type, c.typeMapper)
                it.astore(classType)
            }

            it.invokevirtual("java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false)

            it.dup()
            it.iconst(1)
            it.invokevirtual("java/lang/reflect/Method", "setAccessible", "(Z)V", false)

            c.codegen.gen(resolvedCall.getReceiverExpression()!!).put(it)

            it.aconst(candidateDescriptor.valueParameters.size)
            it.newarray(objectType)

            parameters.forEachIndexed { i, stackValue ->
                it.dup()
                it.iconst(i)
                it.putStackValue(stackValue)
                it.astore(objectType)
            }

            it.invokevirtual("java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false)
            it.castObject(candidateDescriptor.returnTypeOrNothing, c.typeMapper)
        }
    }

    private fun InstructionAdapter.performGetField(kotlinType: KotlinType, typeMapper: KotlinTypeMapper) {
        when(val type = kotlinType.asmType(typeMapper)) {
            BOOLEAN_TYPE -> invokevirtual("java/lang/reflect/Field", "getBoolean", getMethodDescriptor(BOOLEAN_TYPE, OBJECT_TYPE), false)
            BYTE_TYPE -> invokevirtual("java/lang/reflect/Field", "getByte", getMethodDescriptor(BYTE_TYPE, OBJECT_TYPE), false)
            CHAR_TYPE -> invokevirtual("java/lang/reflect/Field", "getChar", getMethodDescriptor(CHAR_TYPE, OBJECT_TYPE), false)
            SHORT_TYPE -> invokevirtual("java/lang/reflect/Field", "getShort", getMethodDescriptor(SHORT_TYPE, OBJECT_TYPE), false)
            INT_TYPE -> invokevirtual("java/lang/reflect/Field", "getInt", getMethodDescriptor(INT_TYPE, OBJECT_TYPE), false)
            FLOAT_TYPE -> invokevirtual("java/lang/reflect/Field", "getFloat", getMethodDescriptor(FLOAT_TYPE, OBJECT_TYPE), false)
            DOUBLE_TYPE -> invokevirtual("java/lang/reflect/Field", "getDouble", getMethodDescriptor(DOUBLE_TYPE, OBJECT_TYPE), false)
            LONG_TYPE -> invokevirtual("java/lang/reflect/Field", "getLong", getMethodDescriptor(LONG_TYPE, OBJECT_TYPE), false)
            else -> {
                invokevirtual("java/lang/reflect/Field", "get", getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE), false)
                checkcast(type)
            }
        }
    }

    private fun InstructionAdapter.putType(kotlinType: KotlinType, typeMapper: KotlinTypeMapper) {

        when(val type = kotlinType.asmType(typeMapper)) {
            BOOLEAN_TYPE -> putPrimitiveType(BOOLEAN_TYPE)
            BYTE_TYPE -> putPrimitiveType(BYTE_TYPE)
            CHAR_TYPE -> putPrimitiveType(CHAR_TYPE)
            SHORT_TYPE -> putPrimitiveType(SHORT_TYPE)
            INT_TYPE -> putPrimitiveType(INT_TYPE)
            FLOAT_TYPE -> putPrimitiveType(FLOAT_TYPE)
            DOUBLE_TYPE -> putPrimitiveType(DOUBLE_TYPE)
            LONG_TYPE -> putPrimitiveType(LONG_TYPE)
            else -> aconst(type)
        }

    }

    private fun InstructionAdapter.putPrimitiveType(primitiveType: Type) {
        getstatic(primitiveType.toWrapper().internalName, "TYPE", CLASS_TYPE.descriptor)
    }

    private fun InstructionAdapter.putStackValue(stackValue: StackValue) {

        stackValue.put(this)

        when (stackValue.type) {
            BOOLEAN_TYPE -> invokestatic(BOOLEAN_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(BOOLEAN_OBJECT_TYPE, BOOLEAN_TYPE), false)
            CHAR_TYPE -> invokestatic(CHAR_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(CHAR_OBJECT_TYPE, CHAR_TYPE), false)
            SHORT_TYPE -> invokestatic(SHORT_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(SHORT_OBJECT_TYPE, SHORT_TYPE), false)
            INT_TYPE -> invokestatic(INT_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(INT_OBJECT_TYPE, INT_TYPE), false)
            LONG_TYPE -> invokestatic(LONG_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(LONG_OBJECT_TYPE, LONG_TYPE), false)
            BYTE_TYPE -> invokestatic(BYTE_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(BYTE_OBJECT_TYPE, BYTE_TYPE), false)
            FLOAT_TYPE -> invokestatic(FLOAT_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(FLOAT_OBJECT_TYPE, FLOAT_TYPE), false)
            DOUBLE_TYPE -> invokestatic(DOUBLE_OBJECT_TYPE.internalName, "valueOf", getMethodDescriptor(DOUBLE_OBJECT_TYPE, DOUBLE_TYPE), false)
        }

    }

    private fun InstructionAdapter.castObject(destType: KotlinType, typeMapper: KotlinTypeMapper) {

        when (val type = destType.asmType(typeMapper)) {
            BOOLEAN_TYPE -> {
                checkcast(BOOLEAN_OBJECT_TYPE)
                invokevirtual("java/lang/Boolean", "booleanValue", getMethodDescriptor(BOOLEAN_TYPE), false)
            }
            BYTE_TYPE -> {
                checkcast(BYTE_OBJECT_TYPE)
                invokevirtual("java/lang/Byte", "byteValue", getMethodDescriptor(BYTE_TYPE), false)
            }
            CHAR_TYPE -> {
                checkcast(CHAR_OBJECT_TYPE)
                invokevirtual("java/lang/Character", "charValue", getMethodDescriptor(CHAR_TYPE), false)
            }
            DOUBLE_TYPE -> {
                checkcast(DOUBLE_OBJECT_TYPE)
                invokevirtual("java/lang/Double", "doubleValue", getMethodDescriptor(DOUBLE_TYPE), false)
            }
            FLOAT_TYPE -> {
                checkcast(FLOAT_OBJECT_TYPE)
                invokevirtual("java/lang/Float", "floatValue", getMethodDescriptor(FLOAT_TYPE), false)
            }
            INT_TYPE -> {
                checkcast(INT_OBJECT_TYPE)
                invokevirtual("java/lang/Integer", "intValue", getMethodDescriptor(INT_TYPE), false)
            }
            LONG_TYPE -> {
                checkcast(LONG_OBJECT_TYPE)
                invokevirtual("java/lang/Long", "longValue", getMethodDescriptor(LONG_TYPE), false)
            }
            SHORT_TYPE -> {
                checkcast(SHORT_OBJECT_TYPE)
                invokevirtual("java/lang/Short", "shortValue", getMethodDescriptor(SHORT_TYPE), false)
            }
            else -> {
                checkcast(type)
            }
        }

    }

}
