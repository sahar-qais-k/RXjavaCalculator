package com.sahar.simplecalculator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.sahar.simplecalculator.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.mariuszgromada.math.mxparser.Expression
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        foo()
    }


    private fun foo() {
        val observable = Observable.create<String> { emitter ->
            binding.editTextTextPersonName.doOnTextChanged { text, start, before, count ->
                emitter.onNext(text.toString())
            }
        }.debounce  (1.5.toLong(), TimeUnit.SECONDS)
        observable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
            { t ->
                val output = Expression(t ).calculate()
                binding.viewText.text = output.toString()
            },
            { e ->
                 Toast.makeText(applicationContext, "enter valid input", Toast.LENGTH_LONG).show()
            }
        )
    }



}