package com.jereksel.jailbreakk

import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.impl.PackageFragmentDescriptorImpl
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.scopes.MemberScope

class EmptyPackageFragmentDescriptorImpl(module: ModuleDescriptor, fqName: FqName) : PackageFragmentDescriptorImpl(module, fqName) {
    override fun getMemberScope(): MemberScope {
        return MemberScope.Empty
    }
}

