@file:Suppress("unused")

package com.zj.ui.dispatcher

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.zj.ui.castNotSafety
import com.zj.ui.log
import java.lang.Exception
import java.util.concurrent.Executors

data class CacheData<R : Any>(val d: R?, val lst: List<R>?, val payload: String?)

inline fun <reified T : Any, reified R : Any> LifecycleOwner.addTransferObserver(uniqueCode: Any): UIHandlerCreator<T, R> {
    return UIHandlerCreator(this, uniqueCode, T::class.java, R::class.java)
}

inline fun <reified T : Any> LifecycleOwner.addReceiveObserver(uniqueCode: Any): UIHelperCreator<T, T> {
    return UIHelperCreator(this, uniqueCode, T::class.java, T::class.java, null)
}

class UIHandlerCreator<T : Any, R : Any>(private val owner: LifecycleOwner, private val uniqueCode: Any, private val inCls: Class<T>, private val outCls: Class<R>) {
    fun addHandler(cls: Class<DataHandler<T, R>>): UIHelperCreator<T, R> {
        return UIHelperCreator(owner, uniqueCode, inCls, outCls, cls)
    }
}

class UIHelperCreator<T : Any, R : Any>(private val owner: LifecycleOwner, private val uniqueCode: Any, internal val inCls: Class<T>, internal val outCls: Class<R>, internal val handlerCls: Class<DataHandler<T, R>>?) {

    internal var filterIn: ((T, String?) -> Boolean)? = null
    internal var filterOut: ((R, String?) -> Boolean)? = null
    private var onDataReceived: ((R?, List<R>?, String?) -> Unit)? = null
    private var isPaused: Boolean = false
    private val cacheDatas = hashSetOf<CacheData<R>>()

    fun filterIn(filter: (T, String?) -> Boolean): UIHelperCreator<T, R> {
        this.filterIn = filter
        return this
    }

    fun filterOut(filter: (R, String?) -> Boolean): UIHelperCreator<T, R> {
        this.filterOut = filter
        return this
    }

    fun listen(onDataReceived: (R?, List<R>?, String?) -> Unit) {
        this.onDataReceived = onDataReceived
        UIOptions(owner, uniqueCode, this) { d, s, c ->
            cacheDatas.add(CacheData(d, s, c))
            if (!isPaused) {
                notifyDataChanged()
            }
        }
    }

    private fun notifyDataChanged() {
        cacheDatas.forEach {
            onDataReceived?.invoke(it.d, it.lst, it.payload)
        }
        cacheDatas.clear()
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
        notifyDataChanged()
    }

    fun shutdown() {
        cacheDatas.clear()
        onDataReceived = null
        isPaused = false
        filterIn = null
        filterOut = null
    }
}

internal class UIOptions<T : Any, R : Any>(owner: LifecycleOwner, private val uniqueCode: Any, private val creator: UIHelperCreator<T, R>, private val result: (R?, List<R>?, String?) -> Unit) : LifecycleObserver {

    private val pal = "payload"
    private val cag = "category"
    private val handleWhat = 0x1101
    private val executors = Executors.newSingleThreadExecutor()
    private val cacheList: ArrayList<R> = arrayListOf()

    private val handler = Handler(Looper.getMainLooper()) {
        if (it.what == handleWhat) {
            val b = it.data
            val payload = if (b.containsKey(pal)) b.getString(pal) else null
            when (b.getInt(cag)) {
                0 -> {
                    castNotSafety<Any?, R?>(it.obj)?.let { r ->
                        result(r, null, payload)
                    } ?: log("the data ${it.obj} was handled but null result in cast transform")
                }
                1 -> {
                    castNotSafety<Any?, List<R>?>(it.obj)?.let { lst ->
                        result(null, lst, payload)
                    } ?: log("the data ${it.obj} was handled but null list result in cast transform")
                }
            }
        }
        return@Handler false
    }

    init {
        owner.lifecycle.addObserver(this)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is UIOptions<*, *>) return false
        return other.uniqueCode == uniqueCode
    }

    override fun hashCode(): Int {
        return uniqueCode.hashCode()
    }

    fun getSubscirbeClassName(): String {
        return creator.inCls.simpleName
    }

    fun getUniquen(): Any {
        return uniqueCode
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        UIStore.putAnObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        try {
            creator.shutdown()
            UIStore.removeAnObserver(this)
            handler.removeCallbacksAndMessages(null)
            if (!executors.isShutdown) executors.shutdownNow()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun post(data: Any, payload: String?): Boolean {
        if (data is Collection<*>) {
            (data as? Collection<*>)?.let {
                it.firstOrNull()?.let { c ->
                    if (c.javaClass == creator.inCls) {
                        (data as? Collection<T>)?.let { co ->
                            postData(null, co, payload)
                            return true
                        }
                    }
                }
            }
        }
        if (data.javaClass == creator.inCls || data.javaClass.simpleName == creator.inCls.simpleName) {
            postData(data as? T?, null, payload)
            return true
        }
        return false
    }

    private fun postData(data: T?, lst: Collection<T>?, payload: String?) {
        if (data == null && lst.isNullOrEmpty()) {
            log("why are you post a null data and also register a type-null observer?");return
        }
        executors.submit(UIExecutor(creator, data, lst, payload) { d, ls, p ->
            val a: Any = d ?: (ls ?: return@UIExecutor)
            postToMain(a, p, if (a == d) 0 else 1)
        })
    }

    private fun postToMain(data: Any, payload: String?, c: Int) {
        handler.sendMessage(Message.obtain().apply {
            what = handleWhat
            obj = data
            val b = Bundle()
            if (!payload.isNullOrEmpty()) b.putString(pal, payload)
            b.putInt(cag, c)
            this.data = b
        })
    }
}

private class UIExecutor<T : Any, R : Any>(private val creator: UIHelperCreator<T, R>, private val data: T?, private val lst: Collection<T>?, private val payload: String?, private val finishd: (R?, List<R>?, String?) -> Unit) : Runnable {

    override fun run() {

        try {
            val ds = arrayListOf<T>()
            if (lst != null) ds.addAll(lst)
            if (data != null) ds.add(data)

            val handled = arrayListOf<R>()
            ds.forEach {
                postData(it)?.let { r ->
                    handled.add(r)
                }
            }
            if (handled.isNullOrEmpty()) finishd(null, null, payload) else {
                val hd = if (handled.size == 1) handled.first() else null
                val hds = if (handled.size > 1) handled else null
                finishd(hd, hds, payload)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun postData(data: T): R? {
        return creator.filterIn?.let {
            if (it.invoke(data, payload)) postFilterInData(data)
            else {
                log("the data $data may abandon with filter in")
                return@postData null
            }
        } ?: postFilterInData(data)
    }

    private fun postFilterInData(data: T): R? {
        val out: R = creator.handlerCls?.newInstance()?.handle(data) ?: castNotSafety(data)
        return postHandlerData(out)
    }

    private fun postHandlerData(data: R): R? {
        return (creator.filterOut?.let {
            if (it.invoke(data, payload)) data
            else {
                log("the data $data may abandon with filter out")
                return@postHandlerData null
            }
        } ?: data)
    }
}