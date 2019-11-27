package com.jereksel.jailbreakk

class PrimitiveTest : AbstractCompilerTest() {

    init {

        "Primitive types" {

            class SecretClass() {
                private fun getBoolean(n: Boolean) = n
                private fun getByte(n: Byte) = n
                private fun getChar(n: Char) = n
                private fun getShort(n: Short) = n
                private fun getInt(n: Int) = n
                private fun getLong(n: Long) = n
                private fun getFloat(n: Float) = n
                private fun getDouble(n: Double) = n
            }



        }

    }

}