package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.SourceFile

class KotlinPrivateMethodTest : AbstractCompilerTest() {

    init {

        "Should invoke private method" {

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                
                import jb.SecretClass.adder
                
                class SecretClass {
                    private fun adder(a: String, b: String, c: String): String = a + b + c
                }
                
                fun test(): String {
                    val secret = SecretClass()
                    val a = "a"
                    val b = "b"
                    val result = secret.adder(a, b, "c")
                    if (result == "abc") {
                        return "OK"
                    } else {
                        error("")
                    }
                }
        
            """)

            testCompilation(kotlinSource)

        }

    }

}
