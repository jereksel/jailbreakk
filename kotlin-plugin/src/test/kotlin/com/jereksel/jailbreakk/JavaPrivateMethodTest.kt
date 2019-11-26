package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.SourceFile

class JavaPrivateMethodTest : AbstractCompilerTest() {

    init {

        "Should invoke private method" {

            val secretClass = SourceFile.java("SecretClass.java", """
            
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
                    val result = secret.adder(a, b, "c")
                    if (result == "abc") {
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