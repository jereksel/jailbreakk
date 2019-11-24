package com.jereksel.jailbreakk

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.load.java.descriptors.JavaCallableMemberDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

fun ClassDescriptor.getDescriptors(moduleDescriptor: ModuleDescriptor): List<CallableDescriptor> {

    val descriptors = unsubstitutedMemberScope.getContributedDescriptors { true }

    return descriptors
            .filterIsInstance<JavaCallableMemberDescriptor>()
            .filterNot { it.visibility.isPublicAPI }
            .map { descriptor ->
                println(descriptor)

                val f = JailbreakkSimpleFunctionDescriptor(
                        EmptyPackageFragmentDescriptorImpl(moduleDescriptor, FqName("${Const.PACKAGE_PREFIX}.${fqNameSafe.asString()}.${descriptor.name.identifier}")),
                        Annotations.EMPTY,
                        descriptor.name,
                        CallableMemberDescriptor.Kind.SYNTHESIZED,
                        descriptor.source,
                        this.defaultType
                )

                f.initialize(
                        thisAsReceiverParameter,
                        null,
                        descriptor.typeParameters,
                        descriptor.valueParameters,
                        descriptor.returnType,
                        Modality.FINAL,
                        Visibilities.PUBLIC
                )

                f
            }
}
