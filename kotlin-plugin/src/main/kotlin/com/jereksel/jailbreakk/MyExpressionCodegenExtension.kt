package com.jereksel.jailbreakk

import jdk.internal.org.objectweb.asm.commons.GeneratorAdapter
import org.jetbrains.kotlin.codegen.OperationStackValue
import org.jetbrains.kotlin.codegen.StackValue
import org.jetbrains.kotlin.codegen.asmType
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.resolve.calls.callUtil.getFirstArgumentExpression
import org.jetbrains.kotlin.resolve.calls.callUtil.getParameterForArgument
import org.jetbrains.kotlin.resolve.calls.callUtil.getReceiverExpression
import org.jetbrains.kotlin.resolve.calls.inference.returnTypeOrNothing
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.types.typeUtil.makeNullable
import org.jetbrains.org.objectweb.asm.Type

class MyExpressionCodegenExtension : ExpressionCodegenExtension {

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
                val type = valueParameterDescriptor.type.asmType(c.typeMapper)
                it.iconst(i)
                it.aconst(type)
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
                stackValue.put(it)
                it.astore(stackValue.type)
            }

            it.invokevirtual("java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false)
            it.checkcast(candidateDescriptor.returnTypeOrNothing.asmType(c.typeMapper))
        }
    }

}