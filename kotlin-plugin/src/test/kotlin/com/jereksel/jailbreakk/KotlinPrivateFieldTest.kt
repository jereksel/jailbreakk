package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.SourceFile

class KotlinPrivateFieldTest : AbstractCompilerTest() {

    init {

        "Should get private field" {

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                
                import jb.SecretClass.f_field
                
                class SecretClass {
                    private val f = "abc"
                }
                
                fun test(): String {
                    val secret = SecretClass()
                    if (secret.f_field == "abc") {
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