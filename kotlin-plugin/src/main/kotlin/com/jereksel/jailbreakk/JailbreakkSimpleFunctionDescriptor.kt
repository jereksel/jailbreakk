package com.jereksel.jailbreakk

import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.SimpleType

class JailbreakkSimpleFunctionDescriptor(
        containingDeclaration: DeclarationDescriptor,
        annotations: Annotations,
        name: Name,
        kind: CallableMemberDescriptor.Kind,
        source: SourceElement,
        val classType: SimpleType
) : SimpleFunctionDescriptorImpl(containingDeclaration, null, annotations, name, kind, source)