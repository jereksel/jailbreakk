package com.jereksel.jailbreakk

import org.jetbrains.kotlin.codegen.OperationStackValue
import org.jetbrains.kotlin.codegen.StackValue
import org.jetbrains.kotlin.codegen.asmType
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.codegen.state.KotlinTypeMapper
import org.jetbrains.kotlin.resolve.calls.callUtil.getReceiverExpression
import org.jetbrains.kotlin.resolve.calls.inference.returnTypeOrNothing
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter

class MyExpressionCodegenExtension : ExpressionCodegenExtension {

    val CLASS_TYPE = Type.getType(Class::class.java)

    companion object {
        val BOOLEAN_OBJECT_TYPE = Type.getType(java.lang.Boolean::class.java)!!
        val BYTE_OBJECT_TYPE = Type.getType(java.lang.Byte::class.java)!!
        val CHAR_OBJECT_TYPE = Type.getType(java.lang.Character::class.java)!!
        val DOUBLE_OBJECT_TYPE = Type.getType(java.lang.Double::class.java)!!
        val FLOAT_OBJECT_TYPE = Type.getType(java.lang.Float::class.java)!!
        val INT_OBJECT_TYPE = Type.getType(java.lang.Integer::class.java)!!
        val LONG_OBJECT_TYPE = Type.getType(java.lang.Long::class.java)!!
        val SHORT_OBJECT_TYPE = Type.getType(java.lang.Short::class.java)!!

        val primitiveToWrapper: Map<Type, Type> = mapOf(
                Type.BOOLEAN_TYPE to BOOLEAN_OBJECT_TYPE,
                Type.BYTE_TYPE to BYTE_OBJECT_TYPE,
                Type.CHAR_TYPE to CHAR_OBJECT_TYPE,
                Type.DOUBLE_TYPE to DOUBLE_OBJECT_TYPE,
                Type.FLOAT_TYPE to FLOAT_OBJECT_TYPE,
                Type.INT_TYPE to INT_OBJECT_TYPE,
                Type.LONG_TYPE to LONG_OBJECT_TYPE,
                Type.SHORT_TYPE to SHORT_OBJECT_TYPE
        )

        fun Type.toWrapper(): Type = primitiveToWrapper[this] ?: error("Type $this is not primitive type")

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

        val objectType = Type.getType(java.lang.Object::class.java)
        val classType = Type.getType(Class::class.java)

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
            it.aconst(1)
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

    private fun InstructionAdapter.putType(kotlinType: KotlinType, typeMapper: KotlinTypeMapper) {

        when(val type = kotlinType.asmType(typeMapper)) {
            Type.BOOLEAN_TYPE -> putPrimitiveType(Type.BOOLEAN_TYPE)
            Type.BYTE_TYPE -> putPrimitiveType(Type.BYTE_TYPE)
            Type.CHAR_TYPE -> putPrimitiveType(Type.CHAR_TYPE)
            Type.INT_TYPE -> putPrimitiveType(Type.INT_TYPE)
            Type.FLOAT_TYPE -> putPrimitiveType(Type.FLOAT_TYPE)
            Type.DOUBLE_TYPE -> putPrimitiveType(Type.DOUBLE_TYPE)
            Type.LONG_TYPE -> putPrimitiveType(Type.LONG_TYPE)
            else -> aconst(type)
        }

    }

    private fun InstructionAdapter.putPrimitiveType(primitiveType: Type) {
        getstatic(primitiveType.toWrapper().internalName, "TYPE", CLASS_TYPE.descriptor)
    }

    private fun InstructionAdapter.putStackValue(stackValue: StackValue) {

        stackValue.put(this)

        if (stackValue.type == Type.BOOLEAN_TYPE) {
            invokestatic("java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false)
        }

    }

    private fun InstructionAdapter.castObject(destType: KotlinType, typeMapper: KotlinTypeMapper) {

        val type = destType.asmType(typeMapper)

        if (type == Type.BOOLEAN_TYPE) {
            checkcast(BOOLEAN_OBJECT_TYPE)
            invokevirtual("java/lang/Boolean", "booleanValue", "()Z", false)
        } else {
            checkcast(type)
        }

    }

    fun a() {
        val m = Class::class.java.getMethod("a")
        m.invoke(null, true)
    }

}
