package com.nokhyun.rxpractice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nokhyun.rxpractice.coroutine.CoroutineTest
import com.nokhyun.rxpractice.rxjava.RxJavaTest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val coroutineTest: CoroutineTest by lazy { CoroutineTest() }
    private val rxJavaTest: RxJavaTest by lazy { RxJavaTest() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RxJava
        rxJavaTest.start()

        // coroutine
        coroutineTest.start()
        bt_next.setOnClickListener { startActivity(Intent(this@MainActivity, CoroutineTestActivity::class.java)) }
    }
}



