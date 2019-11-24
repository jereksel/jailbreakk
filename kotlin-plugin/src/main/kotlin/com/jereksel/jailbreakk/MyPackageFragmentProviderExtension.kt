package com.jereksel.jailbreakk

import com.jereksel.jailbreakk.Const.PACKAGE_PREFIX
import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.impl.PackageFragmentDescriptorImpl
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.incremental.components.LookupTracker
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.isSubpackageOf
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.jvm.extensions.PackageFragmentProviderExtension
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter
import org.jetbrains.kotlin.resolve.scopes.MemberScope
import org.jetbrains.kotlin.resolve.scopes.MemberScopeImpl
import org.jetbrains.kotlin.storage.StorageManager
import org.jetbrains.kotlin.utils.Printer

class MyPackageFragmentProviderExtension : PackageFragmentProviderExtension {

    override fun getPackageFragmentProvider(
            project: Project,
            module: ModuleDescriptor,
            storageManager: StorageManager,
            trace: BindingTrace,
            moduleInfo: ModuleInfo?,
            lookupTracker: LookupTracker
    ): PackageFragmentProvider? {

        return object: PackageFragmentProvider {
            override fun getPackageFragments(fqName: FqName): List<PackageFragmentDescriptor> {

                if (fqName.isRoot || fqName.parent().isRoot) {
                    return emptyList()
                }

                if (!fqName.isSubpackageOf(FqName(PACKAGE_PREFIX))) {
                    return emptyList()
                }

                val fqNameWithoutPrefix = FqName.fromSegments(fqName.pathSegments().drop(1).map { it.asString() })

                val clz = module.findClassAcrossModuleDependencies(ClassId(fqNameWithoutPrefix.parent(), fqNameWithoutPrefix.shortName())) ?: return emptyList()

                return listOf(
                        object: PackageFragmentDescriptorImpl(
                                module,
                                fqName
                        ) {
                            override fun getMemberScope(): MemberScope {

                                val descriptors = clz.getDescriptors(module)

                                return object : MemberScopeImpl() {

                                    override fun getContributedFunctions(
                                            name: Name,
                                            location: LookupLocation
                                    ): Collection<SimpleFunctionDescriptor> {
                                        return descriptors.filterIsInstance<SimpleFunctionDescriptor>().filter { it.name == name }
                                    }

                                    override fun getContributedVariables(
                                            name: Name,
                                            location: LookupLocation
                                    ): Collection<PropertyDescriptor> {
                                        return descriptors.filterIsInstance<PropertyDescriptor>().filter { it.name == name }
                                    }

                                    override fun getContributedDescriptors(
                                            kindFilter: DescriptorKindFilter,
                                            nameFilter: (Name) -> Boolean
                                    ): Collection<DeclarationDescriptor> {
                                        return descriptors
                                                .filter { kindFilter.accepts(it) && nameFilter(it.name) }
                                    }

                                    override fun printScopeStructure(p: Printer) {
                                        p.println(this::class.java.simpleName)
                                    }

                                }

                            }
                        }
                )

            }

            override fun getSubPackagesOf(fqName: FqName, nameFilter: (Name) -> Boolean): Collection<FqName> {
                return emptyList()
            }
        }

    }

}