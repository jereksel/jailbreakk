package com.jereksel.jailbreakk

import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.PackageFragmentProvider
import org.jetbrains.kotlin.incremental.components.LookupTracker
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.storage.StorageManager

//TODO: Find way to disable Intellij errors in this file
class JailbreakkIDEPackageFragmentProviderExtension : JailbreakkPackageFragmentProviderExtension() {

    override fun getPackageFragmentProvider(project: Project, module: ModuleDescriptor, storageManager: StorageManager, trace: BindingTrace, moduleInfo: ModuleInfo?, lookupTracker: LookupTracker): PackageFragmentProvider? {
        return getIfEnabledOn(module) {
            super.getPackageFragmentProvider(project, module, storageManager, trace, moduleInfo, lookupTracker)
        }
    }
}