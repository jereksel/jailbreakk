package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode.OK
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.joor.Reflect

abstract class AbstractCompilerTest : StringSpec() {

    protected fun testCompilation(vararg source: SourceFile) {

        val result = KotlinCompilation().apply {
            sources = source.toList()

            // pass your own instance of a compiler plugin
            compilerPlugins = listOf(MyComponentRegistrar())

            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        result.exitCode shouldBe OK

        Reflect.on("MainKt", result.classLoader)
                .call("test")
                .get<String>() shouldBe "OK"

    }

}