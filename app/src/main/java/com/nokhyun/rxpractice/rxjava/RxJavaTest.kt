package com.nokhyun.rxpractice.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit


/*
* Disposable 구독이 더는 필요없을때 메모리 누수 방지를 위해 명시적인 폐기처리를 할 수 있음
*
* CompositeDisposable 여러 구독을 한번에 폐기 가능.
* ex) compositeDisposable.add(disposable)
*
 */
class RxJavaTest {
    fun start() {
        groupBy()
    }

    private fun groupBy() {
        val disposable = Observable.just(
            "Magenta Circle",
            "Cyan Circle",
            "Yellow Triangle",
            "Yellow Circle",
            "Magenta Triangle",
            "Cyan Triangle",
            "NONE"
        )
            .groupBy { t ->
                when {
                    t.contains("Circle") -> {
                        return@groupBy "C"
                    }
                    t.contains("Triangle") -> {
                        return@groupBy "T"
                    }
                    else -> {
                        return@groupBy "None"
                    }
                }
            }.subscribe { group ->
                /* 새로운 그룹이 생성될 때 그룹을 발행하고 이미 똑같은 그룹이 있다면 중복 생성하지는 않는다.
                * ex) value A
                * create groupA -> value A
                * value AA
                * groupA -> value A, value AA
                * value B
                * create groupB -> value B
                * */

                println("${group.key} 그룹 발행 시작")
                group.subscribe { shape ->
                    println("${group.key}: $shape")
                }
            }

        disposable.dispose()
    }

    private fun scanEx2() {
        Observable.just("a", "b", "c", "d", "e")
            .scan { t1: String, t2: String ->
                t1 + t2
            }.subscribe {
                println(it)
            }
    }

    // 발행 한 후의 값이 다음 인자에 첫번째 값으로 들어간다. (값이 누적됨.)
    private fun scanEx1() {
        Observable.range(1, 5)
            .scan { t1: Int, t2: Int ->
                print("$t1+$t2=")
                t1 + t2
            }.subscribe {
                println(it)
            }
    }


    private fun test9() {
        val justSrc = Observable.just(System.currentTimeMillis())
        val deferSrc = Observable.defer {
            Observable.just(System.currentTimeMillis())
        }

        println("#1 now = ${System.currentTimeMillis()}")
        try {
            Thread.sleep(5000)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("#2 now = ${System.currentTimeMillis()}")
        justSrc.subscribe { println("#1 time = $it") }
        deferSrc.subscribe { println("#2 time = $it") }    // defer는 subscribe를 할때 구독
    }

    private fun test8() {
        val src = Observable.interval(1, TimeUnit.SECONDS)
        src.subscribe {
            print("#1: $it")
        }
        Thread.sleep(3000)

        src.subscribe {
            print("#2: $it")
        }
        Thread.sleep(3000)
    }

    private fun test7() {
//        Completable.create { it.onComplete() }.subscribe { tv_main.text = "Completed1" }
//        Completable.fromRunnable {  }.subscribe { tv_main.text = "completed2" }
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

//        val callable = Callable { "Hello World" }
//        val source = Observable.fromCallable(callable)
//        source.subscribe {
//            tv_main.text = it
//        }
    }

    private fun test3() {
        val source: Observable<String> = Observable.just("Hello", "World")


        var str: String = ""
        source.map {
            str += it
            return@map str
        }.subscribe {
//            tv_main.text = it
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