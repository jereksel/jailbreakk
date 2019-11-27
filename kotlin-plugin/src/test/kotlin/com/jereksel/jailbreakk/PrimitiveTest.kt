package com.jereksel.jailbreakk

import com.tschuchort.compiletesting.SourceFile

class PrimitiveTest : AbstractCompilerTest() {

    init {

        "Primitive types" {

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
                    
//                    val b = clz.getByte(1)
//                    val c = clz.getChar(2.toChar())
//                    val s = clz.getShort(3)
//                    val i = clz.getInt(4)
//                    val l = clz.getLong(5L)
//                    val float = clz.getFloat(6.0F)
//                    val double = clz.getDouble(7.0)
                    
                    return "OK"
                
                }
            
            """)

            testCompilation(kotlinSource)

        }

    }

}