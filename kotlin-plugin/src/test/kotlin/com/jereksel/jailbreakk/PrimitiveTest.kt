package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.SourceFile

class PrimitiveTest : AbstractCompilerTest() {

    init {

        "Primitive types in method should work" {

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                import jb.SecretClass.*

                class SecretClass {
                    private fun getBoolean(n: Boolean) = n
                    private fun getByte(n: Byte) = n
                    private fun getChar(n: Char) = n
                    private fun getShort(n: Short) = n
                    private fun getInt(n: Int) = n
                    private fun getLong(n: Long) = n
                    private fun getFloat(n: Float) = n
                    private fun getDouble(n: Double) = n
                }
                
                fun test(): String {
                    val clz = SecretClass()
                    
                    if (clz.getBoolean(false)) {
                        return "FAIL"
                    }
                    
                    val b = clz.getByte(1)
                    val c = clz.getChar(2.toChar())
                    val s = clz.getShort(3)
                    val i = clz.getInt(4)
                    val l = clz.getLong(5L)
                    val f = clz.getFloat(6.0F)
                    val d = clz.getDouble(7.0)

                    if (b.toInt() + c.toInt() + s.toInt() + i + l + f + d != 28.0) {
                        return "FAIL"
                    }
                    
                    return "OK"
                
                }
            
            """)

            testCompilation(kotlinSource)

        }

        "Primitive types in fields should work" {

            val kotlinSource = SourceFile.kotlin("Main.kt", """
                import jb.SecretClass.*

                class SecretClass {
                    private val boolean: Boolean = false
                    private val byte: Byte = 1
                    private val char: Char = 2.toChar()
                    private val short: Short = 3
                    private val int: Int = 4
                    private val long: Long = 5L
                    private val float: Float = 6.0F
                    private val double: Double = 7.0
                }
                
                fun test(): String {
                    val clz = SecretClass()
                    
                    if (clz.boolean_field) {
                        return "FAIL"
                    }
                    
                    val b = clz.byte_field
                    val c = clz.char_field
                    val s = clz.short_field
                    val i = clz.int_field
                    val l = clz.long_field
                    val f = clz.float_field
                    val d = clz.double_field

                    if (b.toInt() + c.toInt() + s.toInt() + i + l + f + d != 28.0) {
                        return "FAIL"
                    }
                    
                    return "OK"
                
                }
            
            """)

            testCompilation(kotlinSource)

        }


    }

}