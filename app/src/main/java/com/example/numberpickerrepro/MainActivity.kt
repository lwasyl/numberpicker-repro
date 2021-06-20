package com.example.numberpickerrepro

import com.example.FakeSuspendableCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

data class Foo(
    val a: Int,
    val b: Int,
    val f: suspend () -> Unit
) {
}

class Dupa {
    private val x = FakeSuspendableCallback()

    init {
        CoroutineScope(EmptyCoroutineContext).launch {
            a {
                a {
                    a {
                        Foo(
                            a = 1,
                            b = 2,
                            f = x::invoke,
                        )

                    }
                }
            }
        }
    }

    suspend fun a(z: suspend () -> Unit) {
        CoroutineScope(EmptyCoroutineContext).launch { z() }
    }

}
