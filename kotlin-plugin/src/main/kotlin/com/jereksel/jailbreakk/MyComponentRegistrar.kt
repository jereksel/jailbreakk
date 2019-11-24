package com.jereksel.jailbreakk

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.jvm.extensions.PackageFragmentProviderExtension

@AutoService(ComponentRegistrar::class)
class MyComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        PackageFragmentProviderExtension.registerExtension(project, MyPackageFragmentProviderExtension())
        ExpressionCodegenExtension.registerExtension(project, MyExpressionCodegenExtension())
    }
}

