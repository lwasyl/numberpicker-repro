package com.example.numberpickerrepro

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.numberpickerrepro.databinding.FragmentWithBindingBinding
import com.example.numberpickerrepro.databinding.IncludedLayoutBinding
import leakcanary.LeakCanary
import shark.SharkLog
import kotlin.concurrent.thread
import kotlin.random.Random

lateinit var sActivity: Activity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SharkLog.logger = null

        sActivity = this

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_add).setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, FragmentWithBinding())
//                .addToBackStack(null)
//                .commit()

            Intent().apply {
                component =
                    ComponentName("com.android.chrome", "com.google.android.apps.chrome.Main")
                action = Intent.ACTION_VIEW
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                type = "text/html"
                data = "data:text/html,<h1>Hello World</h1>".toUri()
            }.let { startActivity(it) }
        }

        findViewById<Button>(R.id.btn_pop).setOnClickListener {
            supportFragmentManager.popBackStackImmediate()
        }

        findViewById<Button>(R.id.btn_gc).setOnClickListener {
            println("GC")
            repeat(1000) { Activity() }
            System.gc()
        }

        findViewById<Button>(R.id.btn_analyze).setOnClickListener {
            LeakCanary.dumpHeap()
        }
        thread {
            while(true) {

            }
        }
    }
}

class FragmentWithBinding : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWithBindingBinding.inflate(inflater)
        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FragmentViewModel::class.java)

        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        binding.container.bind(viewModel)

        return binding.root
    }

    private fun LinearLayout.bind(everything: FragmentViewModel) {
        val inflater = LayoutInflater.from(context)
        everything.elements.observe(viewLifecycleOwner) { value ->
            removeAllViews()
            value.map { element ->
                IncludedLayoutBinding.inflate(inflater).apply {
                    model = element
                    viewModel = everything
                    lifecycleOwner = viewLifecycleOwner
                    executePendingBindings()
                }.root
            }.forEach(::addView)
        }
    }
}

class FragmentViewModel : ViewModel() {
    val elements = MutableLiveData<List<String>>()
    val name = MutableLiveData<Any>().also { it.value = this }
    val leaking: MutableLiveData<() -> Unit> =
        MutableLiveData<() -> Unit>().also { it.value = this::leaking }

    private fun leaking() {
        Toast.makeText(sActivity, this.toString(), Toast.LENGTH_SHORT).show()
    }

    fun add() {
        elements.value = elements.value.orEmpty() + "Number: ${Random.nextInt(10, 100)}"
    }

    fun remove() {
        elements.value = elements.value.orEmpty().drop(1)
    }
}

@BindingAdapter("customOnClick")
fun Button.customOnClick(onClick: () -> Unit) {
    setOnClickListener { onClick() }
}
