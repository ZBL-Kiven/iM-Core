package com.zj.im

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.zj.im.scheduler.ReceiveListener
import java.util.*

inline fun <reified IN, reified OUT : Any> LifecycleOwner.registerMsgReceivedListener(name: String): ReceiveListener<IN, OUT> {
    return ReceiveListener.create(name, this)
}

@Suppress("UNCHECKED_CAST")
fun <I, O> cast(a: I?): O? {
    return try {
        a as? O
    } catch (e: java.lang.ClassCastException) {
        e.printStackTrace()
        null
    } catch (e: ClassCastException) {
        e.printStackTrace()
        null
    }
}

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


