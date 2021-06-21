import com.example.FailingReference

class Hello {
    init {
        val fails = FailingReference()

        val test = Foo(
            a = 1,
            b = 2,
            f = fails::invoke,
        )
    }
}

data class Foo(
    val a: Int,
    val b: Int,
    val f: suspend () -> Unit,
)
