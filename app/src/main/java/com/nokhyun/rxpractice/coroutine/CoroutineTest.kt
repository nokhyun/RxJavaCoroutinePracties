package com.nokhyun.rxpractice.coroutine

import android.util.Log
import kotlinx.coroutines.*
import java.lang.Exception

class CoroutineTest {
    companion object{ 
         val TAG = CoroutineTest::class.java.simpleName
    }

    /**
     * Dispatchers 설명
     * Dispatchers.Main - UI 스레드를 사용
     * Dispatchers.IO - 네트워크, 디스크를 사용 할 때 사용
     * Dispatchers.Default - CPU 사용량이 많은 작업에 사용
     *
     * CoroutineScope - 코루틴의 범위, 코루틴 블록을 묶음으로 제어 할 수있는 단위.
     * GlobalScope - CoroutineScope의 한 종류. 미리 정의된 방식으로 프로그램 전반에 걸쳐 백그라운드에서 동작.
     *
     * CoroutineContext 코루틴을 어떻게 처리할 것인지에 대한 여러가지 정보의 집합. (Job, Dispatcher)
     * */
    val coroutineScopeDefault: CoroutineScope by lazy { CoroutineScope(Dispatchers.Default) }
    val coroutineScopeIO: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO) }
    val coroutineScopeMain: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    fun start(){
        co6()
        co7()
    }

    private fun co1(){
        Log.d(TAG, "doing someting in main thread")

        GlobalScope.launch {
            delay(3000)
            Log.d(TAG, "done someting in Coroutine")
        }

        Log.d(TAG, "done in main thread")
    }

     private fun co2(){
        GlobalScope.launch {
            launch {
                Log.d(TAG, "Launch has No return value")
            }

            val value: Int = async {
                1 + 2
            }.await()

            Log.d(TAG, "Async has return value: $value")
        }
    }

     private fun co3(){
        GlobalScope.launch {
            doSomething()
            Log.d(TAG, "done something")
        }
    }

    /**
     * 코루틴 안에서 일반적인 메소드는 호출 할 수 없다. 잠시 실행을 멈추거나 다시 실행될 수 있기 때문.
     * 코루틴에서 실행할 수 있는 메소드를 만드려면 함수를 정의할 때 suspend를 붙인다.
     * suspend fun는 안에서 다른 코루틴을 실행할 수 있다.
     */
     private suspend fun doSomething(){
        GlobalScope.launch {
            delay(1000)
            Log.d(TAG, "do something in a suspend method")
        }
    }

     private fun co4(){
        coroutineScopeMain.launch {
            val userOne = async(Dispatchers.IO){
                fetchFirstUser()
            }

            val userTwo = async(Dispatchers.Default){
                fetchSecondUser()
            }

            showUsers(userOne.await(), userTwo.await())
        }
    }

    private fun fetchSecondUser(){
        Log.d(TAG, "fetchSecondUser")
    }

    private fun fetchFirstUser() {
        Log.d(TAG, "fetchFirstUser")

    }

    private fun showUsers(userOne: Unit, userTwo: Unit) {
        Log.d(TAG, "Success")
    }


    /**
     * withContext - async와 동일한 역할을 하는 키워드. await()를 호출 할 필요가 없다.
    * */
    private fun co5(){
        coroutineScopeIO.launch {
            Log.d(TAG, "Do simeting on IO thread")
            val name = withContext(Dispatchers.Main){
                delay(2000)
                "My name is Android"
            }

            Log.d(TAG, "result: $name")
        }
    }
    
    private fun co6(){
        val handler = CoroutineExceptionHandler{coroutineContext, throwable ->
            Log.d(TAG, "$throwable handled")
        }
        coroutineScopeIO.launch(handler) {
            launch { 
                throw Exception()
            }
        }
    }

    private fun co7(){
        coroutineScopeMain.launch{
            try{
                val name = withContext(Dispatchers.Main){
                    throw Exception()
                }
            }catch (e: Exception){
                Log.d(TAG, "$e handled")
            }
        }
    }
    

}