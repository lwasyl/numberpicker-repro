package com.example

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

abstract class BaseCallback<T> {

    val passedArguments = mutableListOf<T>()

    val callsCount get() = passedArguments.size

    internal fun store(argument: T) {
        passedArguments += argument
    }
}

class FakeCallback : BaseCallback<Unit>(), () -> Unit {

    override fun invoke() = store(Unit)
}

class FakeCallback1<T> : BaseCallback<T>(), (T) -> Unit {

    override fun invoke(arg: T) = store(arg)
}

class FakeCallback2<T, P> : BaseCallback<Pair<T, P>>(), (T, P) -> Unit {

    override fun invoke(arg1: T, arg2: P) = store(arg1 to arg2)
}

class FakeCallback3<T, P, Q> : BaseCallback<Triple<T, P, Q>>(), (T, P, Q) -> Unit {

    override fun invoke(arg1: T, arg2: P, arg3: Q) = store(Triple(arg1, arg2, arg3))
}

abstract class BaseSuspendableCallback<T> : BaseCallback<T>() {

    protected val flow = MutableSharedFlow<T>()

    suspend operator fun invoke(arg: T) {
        store(arg)
        flow.first()
    }
}

class FakeSuspendableCallback : BaseSuspendableCallback<Unit>() {

    suspend fun onSuccess() {
        flow.emit(Unit)
    }

    suspend operator fun invoke() = invoke(Unit)
}

class FakeSuspendableCallback1<T> : BaseSuspendableCallback<T>() {

    suspend fun onSuccess(arg: T) {
        flow.emit(arg)
    }
}
