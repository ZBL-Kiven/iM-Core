package com.zj.im

import android.os.Handler
import android.os.Looper
import java.util.*

@Suppress("UNCHECKED_CAST")
@Throws(java.lang.ClassCastException::class, ClassCastException::class)
fun <I, O> castNotSafety(a: I): O {
    return a as O
}

val mainHandler = Handler(Looper.getMainLooper())

fun log(str: String) {
    println("im-ui ----- $str")
}

fun debugLog(str: String) {
//    println("im-ui-debug ----- $str")
}

fun <T, R> PriorityQueue<T>.with(block: (PriorityQueue<T>) -> R): R {
    synchronized(this) {
        return block(this)
    }
}


