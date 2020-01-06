package com.zj.im

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.lang.Exception
import java.util.*

@Suppress("UNCHECKED_CAST")
@Throws(java.lang.ClassCastException::class, ClassCastException::class)
fun <I, O> castNotSafety(a: I): O {
    return a as O
}

val mainHandler = Handler(Looper.getMainLooper())

fun log(str: String) {
    Log.e(" im-uiKit ----- ", str)
}

fun <T, R> PriorityQueue<T>.with(block: (PriorityQueue<T>) -> R): R {
    synchronized(this) {
        return block(this)
    }
}


