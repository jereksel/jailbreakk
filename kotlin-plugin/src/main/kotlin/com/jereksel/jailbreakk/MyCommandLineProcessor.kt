package com.jereksel.jailbreakk

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@AutoService(CommandLineProcessor::class)
class MyCommandLineProcessor : CommandLineProcessor {
    /**
     * Just needs to be consistent with the key for DebugLogGradleSubplugin#getCompilerPluginId
     */
    override val pluginId: String = "com.jereksel.jailbreakk"

    /**
     * Should match up with the options we return from our DebugLogGradleSubplugin.
     * Should also have matching when branches for each name in the [processOption] function below
     */
    override val pluginOptions: Collection<CliOption> = listOf(
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
    }

}
