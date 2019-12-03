package com.jereksel.jailbreakk

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.PropertyDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.KotlinType

class JailbreakkPropertyDescriptor(
        containingDeclaration: DeclarationDescriptor,
        annotations: Annotations,
        modality: Modality,
        visibility: Visibility,
        isVar: Boolean,
        name: Name,
        kind: CallableMemberDescriptor.Kind,
        source: SourceElement,
        val originalName: Name,
        val originalType: KotlinType
) : PropertyDescriptorImpl(containingDeclaration, null, annotations, modality, visibility, isVar, name, kind, source, false, false, false, false, false, false) {
}