package com.jereksel.jailbreakk

import com.jereksel.jailbreakk.Const.PACKAGE_PREFIX
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind.SYNTHESIZED
import org.jetbrains.kotlin.descriptors.Modality.FINAL
import org.jetbrains.kotlin.descriptors.Visibilities.PUBLIC
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.annotations.Annotations.Companion.EMPTY
import org.jetbrains.kotlin.load.java.descriptors.JavaCallableMemberDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

fun ClassDescriptor.getDescriptors(moduleDescriptor: ModuleDescriptor): List<CallableDescriptor> {

    val descriptors = unsubstitutedMemberScope.getContributedDescriptors { true }

    return descriptors
            .filter { (it is JavaCallableMemberDescriptor) or (it is SimpleFunctionDescriptor) }
            .map { it as CallableMemberDescriptor }
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
                            descriptor.typeParameters,
                            descriptor.valueParameters,
                            descriptor.returnType,
                            FINAL,
                            PUBLIC
                    )
                }
            }
}
