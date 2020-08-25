package com.nokhyun.rxpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CoroutineTestActivity() : AppCompatActivity(), CoroutineScope {
    companion object{
        private val TAG = CoroutineTestActivity::class.java.simpleName
    }
    /**
    * Job - Coroutine의 상태를 가지고 있고, 제어할 수 있다. (New, Active, Completing, Cancelling, Cancelled, Completed)
     *
     * start - 동작 상태를 체크, 동작중인 경우 true, 준비 또는 완료 상태 false를 return
     * join - 동작이 끝날 때까지 대기. async(), await()처럼 사용 가능.
     * cancel - 즉시 종료하도록 유도만 하고 대기하지 않음. 타이트하게 동작하는 단순 루프에서는 delay가 없다면 종료하지 못함.
     * cancelAndJoin - Coroutine에 종료하라는 신호를 보내고 정상 종료할 때까지 대기.
     * cancelChildren - CoroutineScope내에 작성한 children들을 종료. 하위 아이템들만 종료하고 부모는 종료하지 않음.
    * */
    private val job: Job by lazy { Job() }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_test)

        launch {
            Log.d(TAG, "CoroutineScope Test")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}