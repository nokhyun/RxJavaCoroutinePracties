package com.nokhyun.rxpractice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RxJava
//        test1()
//        test2()
//        test3()
//        test4()
//            test5()
//        test6()
//        test7()
//        test8()
//        test9()

        // coroutine
//        co1()
//        co2()
        co3()
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

    // 코루틴 안에서 일반적인 메소드는 호출 할 수 없다. 잠시 실행을 멈추거나 다시 실행될 수 있기 때문.
    // 코루틴에서 실행할 수 있는 메소드를 만드려면 함수를 정의할 때 suspend를 붙인다.
    // suspend fun는 안에서 다른 코루틴을 실행할 수 있다.
    private suspend fun doSomething(){
        GlobalScope.launch {
            delay(1000)
            Log.d(TAG, "do something in a suspend method")
        }
    }



    private fun test9(){
        val justSrc = Observable.just(System.currentTimeMillis())
        val deferSrc = Observable.defer{
            Observable.just(System.currentTimeMillis())
        }

        println("#1 now = ${System.currentTimeMillis()}")
        try{
            Thread.sleep(5000)
        }catch (e: Exception){
            e.printStackTrace()
        }

        println("#2 now = ${System.currentTimeMillis()}")
        justSrc.subscribe { println("#1 time = $it") }
        deferSrc.subscribe{println("#2 time = $it")}    // defer는 subscribe를 할때 구독
    }

    private fun test8(){
        val src = Observable.interval(1, TimeUnit.SECONDS)
        src.subscribe{
            print("#1: $it")
        }
        Thread.sleep(3000)

        src.subscribe{
            print("#2: $it")
        }
        Thread.sleep(3000)
    }

    private fun test7() {
//        Completable.create { it.onComplete() }.subscribe { tv_main.text = "Completed1" }
        Completable.fromRunnable {  }.subscribe { tv_main.text = "completed2" }
    }


    private fun test6() {
//        Maybe.create<Int> { emitter ->
//            emitter.onSuccess(100)
//            emitter.onComplete()
//
//        }.doOnSuccess {
//            println("doOnSuccess1")
//        }.doOnComplete { println("DoOnComplete1")
//        }.subscribe { print(it)}
//
//        Maybe.create<Int> {
//            it.onComplete()
//        }.doOnSuccess { print("DoOnSuccess2") }
//            .doOnComplete { print("DoOnComplete2") }
//            .subscribe { print(it) }
//        val src1 = Observable.just(1,2,3)
//        val srcMaybe1 = src1.firstElement()
//        srcMaybe1.subscribe{
//            tv_main.text = it.toString()
//        }
//
//        val src2 = Observable.empty<Int>()
//        val srcMaybe2 = src2.firstElement()
//        srcMaybe2.subscribe( { print(it)}, {t ->  }, { print("onComplete!")})


    }

    private fun test5() {
//        Single.just("Hello").subscribe{it->
//            tv_main.text = it
//        }
//        Single.create<String> { emitter ->
//            emitter.onSuccess("Hello")
//        }.subscribe{it->
//            tv_main.text = it
//        }

        val src: Observable<Int> = Observable.just(1, 2, 3)

        val singleSrc1 = src.all {
            it > 0
        }
        val singleSrc2 = src.first(-1)
        val singleSrc3 = src.toList()

        val singleSrc = Single.just("Hello World")
        val observableSrc = singleSrc.toObservable()
    }

    private fun test4() {
        var str: String = ""
//        val itemArray = mutableListOf<String>("A","B","c")
//        val source = Observable.fromArray(itemArray)
//        source.map {
//            it.forEach { item ->
//                str += item
//            }
//            return@map str
//        }.subscribe{
//            tv_main.text = it.toString()
//        }

//        val publisher: Publisher<String> = Publisher {
//            it.onNext("A")
//            it.onNext("B")
//            it.onNext("C")
//            it.onComplete()
//        }
//
//        val source = Observable.fromPublisher(publisher)
//
//        source.subscribe {
//            Log.d(TAG, "it:$it")
//            tv_main.text = it
//        }

        val callable = Callable { "Hello World" }
        val source = Observable.fromCallable(callable)
        source.subscribe {
            tv_main.text = it
        }
    }

    private fun test3() {
        val source: Observable<String> = Observable.just("Hello", "World")


        var str: String = ""
        source.map {
            str += it
            return@map str
        }.subscribe {
            tv_main.text = it
        }

    }

    private fun test2() {
        val sb = StringBuilder()
        val source: Observable<String> = Observable.create {
            it.onNext("Hello")
            it.onError(Throwable())
            it.onNext("World")
//            it.onComplete()
        }

        source.subscribe({
            println(it)
        }, { println("Error") })


    }

    private fun test1() {

//        val items = PublishSubject.create<Int>()
//
//
//        items.onNext(1)
//        items.onNext(2)
//        items.onNext(3)
//        items.onNext(4)
//
////        items.filter(Predicate { it % 2 == 0 }).subscribe(System.out::println)
//        items.filter(Predicate { it % 2 == 0 }).subscribe {
//            tv_main.text = it.toString()
//        }
//
//        items.onNext(5)
//        items.onNext(6)
//        items.onNext(7)
    }
}



