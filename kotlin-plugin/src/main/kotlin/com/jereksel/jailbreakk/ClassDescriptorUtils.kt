package com.jereksel.jailbreakk

import com.jereksel.jailbreakk.Const.PACKAGE_PREFIX
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind.SYNTHESIZED
import org.jetbrains.kotlin.descriptors.Modality.FINAL
import org.jetbrains.kotlin.descriptors.Visibilities.PUBLIC
import org.jetbrains.kotlin.descriptors.annotations.Annotations.Companion.EMPTY
import org.jetbrains.kotlin.descriptors.impl.PropertyGetterDescriptorImpl
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

fun ClassDescriptor.getDescriptors(moduleDescriptor: ModuleDescriptor): List<CallableDescriptor> {

    val descriptors = unsubstitutedMemberScope.getContributedDescriptors { true }

    val methods = descriptors
            .filterIsInstance<FunctionDescriptor>()
            .filterNot { it.visibility.isPublicAPI }
            .map { descriptor ->

                val name = FqName("$PACKAGE_PREFIX.${fqNameSafe.asString()}")

                JailbreakkSimpleFunctionDescriptor(
                        containingDeclaration = EmptyPackageFragmentDescriptorImpl(moduleDescriptor, name),
                        annotations = EMPTY,
                        name = descriptor.name,
                        kind = SYNTHESIZED,
                        source = descriptor.source,
                        classType = defaultType
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
            .map { descriptor ->

                val name = FqName("$PACKAGE_PREFIX.${fqNameSafe.asString()}")

                JailbreakkPropertyDescriptor(
                        containingDeclaration = EmptyPackageFragmentDescriptorImpl(moduleDescriptor, name),
                        annotations = EMPTY,
                        modality = FINAL,
                        visibility = PUBLIC,
                        isVar = false,
                        name = Name.identifier("${descriptor.name}_field"),
                        kind = SYNTHESIZED,
                        source = descriptor.source,
                        originalName = descriptor.name,
                        originalType = defaultType
                ).apply {

                    val getter = PropertyGetterDescriptorImpl(
                            this,
                            EMPTY,
                            FINAL,
                            visibility,
                            false,
                            false,
                            false,
                            SYNTHESIZED,
                            null,
                            descriptor.source
                    )

                    getter.initialize(descriptor.type)
                    initialize(getter, null)
                    setType(
                            descriptor.type,
                            descriptor.typeParameters,
                            null,
                            thisAsReceiverParameter
                    )

                }

            }


    return methods + fields
}
