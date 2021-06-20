package com.example.numberpickerrepro

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()

fun foo() {}

suspend fun bar() {}

suspend fun boo(
    a: () -> Unit,
    c: suspend () -> Unit,
) {
    boo(
        c,
        a,
    )
}
