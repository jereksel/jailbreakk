package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode.OK
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.joor.Reflect.on

class JavaPrivateMethodTest : StringSpec({

    "Should invoke private method" {

        val javaSource = SourceFile.java("SecretClass.java", """
            
            public class SecretClass {
                private String adder(String a, String b, String c) {
                    return a + b + c;
                }
            }
            
        """)

        val kotlinSource = SourceFile.kotlin("Main.kt", """
            import jb.SecretClass.adder
            
            fun test(): String {
                val secret = SecretClass()
                val a = "a"
                val b = "b"
                return secret.adder(a, b, "c")
            }
        
    """)

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource, javaSource)

            // pass your own instance of a compiler plugin
            compilerPlugins = listOf(MyComponentRegistrar())

            inheritClassPath = true
            messageOutputStream = System.out // see diagnostics in real time
        }.compile()

        result.exitCode shouldBe OK

        on("MainKt", result.classLoader)
                .call("test")
                .get<String>() shouldBe "abc"

    }

})