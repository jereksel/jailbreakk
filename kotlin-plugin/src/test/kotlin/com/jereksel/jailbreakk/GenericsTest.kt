package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.SourceFile

class GenericsTest : AbstractCompilerTest() {

    init {

        "Private method in generic class" {

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                
                import jb.SecretClass.secret
                
                class SecretClass<T> {
                    private fun secret() = "OK"
                }
                
                fun test(): String {
                    val secret = SecretClass<Int>()
                    return secret.secret()
                }
        
            """)

            testCompilation(kotlinSource)

        }

        "Private field in generic class" {

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                
                import jb.SecretClass.secret_field
                
                class SecretClass<T> {
                    private val secret = "OK"
                }
                
                fun test(): String {
                    val secret = SecretClass<Int>()
                    return secret.secret_field
                }
        
            """)

            testCompilation(kotlinSource)

        }

        "Method returning type parameter" {

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                
                import jb.SecretClass.secret
                
                class SecretClass<T>(private val value: T) {
                    private fun secret(): T = value
                }
                
                fun test(): String {
                    val secret = SecretClass("OK")
                    return secret.secret()
                }
        
            """)

            testCompilation(kotlinSource)

        }

    }

}