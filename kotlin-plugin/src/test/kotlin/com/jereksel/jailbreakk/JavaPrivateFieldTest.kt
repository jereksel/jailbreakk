package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.SourceFile

class JavaPrivateFieldTest : AbstractCompilerTest() {

    init {

        "Should get private field" {

            val secretClass = SourceFile.java("SecretClass.java", """
            
                public class SecretClass {
                    private String f = "abc";
                }
            
        """)

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                import jb.SecretClass.f_field
            
                fun test(): String {
                    val secret = SecretClass()
                    if (secret.f_field == "abc") {
                        return "OK"
                    } else {
                        error("")
                    }
                }
        
            """)

            testCompilation(secretClass, kotlinSource)

        }

    }

}