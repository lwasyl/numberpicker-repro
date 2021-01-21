package com.example

import org.junit.Test

class Testt {

    @Test
    fun Test1() = Unit

    @Test
    fun Test2() = Unit

    @Test
    fun Test3() {
        error("💥")
    }

    @Test
    fun Test4() = Unit
}
