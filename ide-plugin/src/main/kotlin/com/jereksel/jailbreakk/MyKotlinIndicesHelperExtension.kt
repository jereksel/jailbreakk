package com.jereksel.jailbreakk

import com.jereksel.jailbreakk.Const.PACKAGE_PREFIX
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.load.java.descriptors.JavaCallableMemberDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.representativeUpperBound

class MyKotlinIndicesHelperExtension {

    fun appendExtensionCallables(
            consumer: MutableList<in CallableDescriptor>,
            moduleDescriptor: ModuleDescriptor,
            receiverTypes: Collection<KotlinType>,
            nameFilter: (String) -> Boolean
    ) {

        //Return collection with multiple elements
        val receiverType = receiverTypes.singleOrNull() ?: return

        val classDescriptor = receiverType.toClassDescriptor ?: return

        consumer.addAll(classDescriptor.getDescriptors(moduleDescriptor))
    }

    // https://github.com/JetBrains/kotlin/blob/e89aabb/plugins/kotlin-serialization/kotlin-serialization-compiler/src/org/jetbrains/kotlinx/serialization/compiler/resolve/KSerializationUtil.kt#L97
    private val KotlinType?.toClassDescriptor: ClassDescriptor?
        @JvmName("toClassDescriptor")
        get() = this?.constructor?.declarationDescriptor?.let { descriptor ->
            when(descriptor) {
                is ClassDescriptor -> descriptor
                is TypeParameterDescriptor -> descriptor.representativeUpperBound.toClassDescriptor
                else -> null
            }
        }

}