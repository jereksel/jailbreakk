package com.jereksel.jailbreakk

import com.google.common.annotations.VisibleForTesting
import com.intellij.openapi.module.Module
import com.intellij.util.io.inputStream
import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.idea.core.unwrapModuleSourceInfo
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.util.jar.Manifest

// Based on https://github.com/JetBrains/kotlin/blob/822b455/plugins/kotlin-serialization/kotlin-serialization-ide/src/org/jetbrains/kotlinx/serialization/idea/SerializationSwitchUtils.kt#L14

@VisibleForTesting
var forceTrue = false

fun <T> getIfEnabledOn(declarationDescriptor: DeclarationDescriptor, body: () -> T): T? {
    if (!forceTrue) {
        val module = declarationDescriptor.module.getCapability(ModuleInfo.Capability)?.unwrapModuleSourceInfo()?.module ?: return null
        return getIfEnabledOn(module, body)
    }
    return body()
}

fun <T> getIfEnabledOn(module: Module, body: () -> T): T? {
    if (!forceTrue) {
        val facet = KotlinFacet.get(module) ?: return null
        val pluginClasspath = facet.configuration.settings.compilerArguments?.pluginClasspaths ?: return null
        val jar = pluginClasspath
                .filter { it.contains("kotlin-plugin") }
                .firstOrNull { it.isJailbreakkKotlinPlugin() } ?: return null
    }
    return body()
}

private fun String.isJailbreakkKotlinPlugin(): Boolean {
    val zipfile = Paths.get(this)
    val fs = FileSystems.newFileSystem(zipfile, null)
    val root = fs.getPath("/META-INF/MANIFEST.MF")
    return try {
        val manifest = Manifest(root.inputStream())
        val attributes = manifest.mainAttributes
        attributes.getValue("Implementation-Title") == "jailbreakk-kotlin-plugin"
    } catch (e: IOException) {
        false
    }
}

fun runIfEnabledOn(descriptor: DeclarationDescriptor, body: () -> Unit) { getIfEnabledOn(descriptor, body) }

