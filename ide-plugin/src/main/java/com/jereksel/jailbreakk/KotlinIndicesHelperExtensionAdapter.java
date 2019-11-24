package com.jereksel.jailbreakk;

import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.descriptors.CallableDescriptor;
import org.jetbrains.kotlin.descriptors.ModuleDescriptor;
import org.jetbrains.kotlin.idea.core.extension.KotlinIndicesHelperExtension;
import org.jetbrains.kotlin.incremental.components.LookupLocation;
import org.jetbrains.kotlin.types.KotlinType;

import java.util.Collection;
import java.util.List;

public class KotlinIndicesHelperExtensionAdapter implements KotlinIndicesHelperExtension {

    @Override
    public void appendExtensionCallables(
            @NotNull List<? super CallableDescriptor> list,
            @NotNull ModuleDescriptor moduleDescriptor,
            @NotNull Collection<? extends KotlinType> collection,
            @NotNull Function1<? super String, Boolean> function1
    ) {
        new MyKotlinIndicesHelperExtension().appendExtensionCallables(list, moduleDescriptor, collection, function1);
    }

}
