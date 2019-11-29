package com.jereksel.jailbreakk

import com.jereksel.jailbreakk.Const.PACKAGE_PREFIX
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind.SYNTHESIZED
import org.jetbrains.kotlin.descriptors.Modality.FINAL
import org.jetbrains.kotlin.descriptors.Visibilities.PUBLIC
import org.jetbrains.kotlin.descriptors.annotations.Annotations.Companion.EMPTY
import org.jetbrains.kotlin.load.java.descriptors.JavaCallableMemberDescriptor
import org.jetbrains.kotlin.load.java.descriptors.JavaMethodDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.isPublishedApi

fun ClassDescriptor.getDescriptors(moduleDescriptor: ModuleDescriptor): List<CallableDescriptor> {

    val descriptors = unsubstitutedMemberScope.getContributedDescriptors { true }

    val methods = descriptors
            .filterIsInstance<FunctionDescriptor>()
            .filterNot { it.visibility.isPublicAPI }
            .map { descriptor ->

                val name = FqName("$PACKAGE_PREFIX.${fqNameSafe.asString()}")

                JailbreakkSimpleFunctionDescriptor(
                        EmptyPackageFragmentDescriptorImpl(moduleDescriptor, name),
                        EMPTY,
                        descriptor.name,
                        SYNTHESIZED,
                        descriptor.source,
                        defaultType
                ).also {
                    it.initialize(
                            thisAsReceiverParameter,
                            null,
                            declaredTypeParameters + descriptor.typeParameters,
                            descriptor.valueParameters,
                            descriptor.returnType,
                            FINAL,
                            PUBLIC
                    )
                }
            }

    val fields = descriptors
            .filterIsInstance<PropertyDescriptor>()
            .filterNot { it.visibility.isPublicAPI }


    return methods
}
