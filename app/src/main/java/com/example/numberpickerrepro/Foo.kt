package com.example.numberpickerrepro

class Foo {
    val bar = Bar2()
}

interface Bar {
    val x: Int
}

class Bar1 : Bar {
    override val x = 0
}

class Bar2 : Bar {
    override val x = 1
}

fun foo() {
    bar()
}

fun bar() {
    println("Bar")
}
