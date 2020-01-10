@file:Suppress("unused")

package com.zj.im.dispatcher

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.*
import com.zj.im.castNotSafety
import com.zj.im.log
import java.lang.Exception
import java.util.concurrent.Executors

inline fun <reified T, reified R> LifecycleOwner.addTransferObserver(uniqueCode: Any): UIHandlerCreator<T, R> {
    return UIHandlerCreator(this, uniqueCode, T::class.java, R::class.java)
}

inline fun <reified T> LifecycleOwner.addReceiveObserver(uniqueCode: Any): UIHelperCreator<T, T> {
    return UIHelperCreator(this, uniqueCode, T::class.java, T::class.java, null)
}

class UIHandlerCreator<T, R>(private val owner: LifecycleOwner, private val uniqueCode: Any, private val inCls: Class<T>, private val outCls: Class<R>) {
    fun addHandler(cls: Class<DataHandler<T, R>>): UIHelperCreator<T, R> {
        return UIHelperCreator(owner, uniqueCode, inCls, outCls, cls)
    }
}

class UIHelperCreator<T, R>(private val owner: LifecycleOwner, private val uniqueCode: Any, internal val inCls: Class<T>, internal val outCls: Class<R>, internal val handlerCls: Class<DataHandler<T, R>>?) {

    internal var filterIn: ((T) -> Boolean)? = null
    internal var filterOut: ((R) -> Boolean)? = null
    private var onDataReceived: ((R) -> Unit)? = null
    private var isPaused: Boolean = false
    private val cacheDatas = hashSetOf<R>()

    fun filterIn(filter: (T) -> Boolean): UIHelperCreator<T, R> {
        this.filterIn = filter
        return this
    }

    fun filterOut(filter: (R) -> Boolean): UIHelperCreator<T, R> {
        this.filterOut = filter
        return this
    }

    fun listen(onDataReceived: (R) -> Unit) {
        this.onDataReceived = onDataReceived
        UIOptions(owner, uniqueCode, this) {
            cacheDatas.add(it)
            if (!isPaused) {
                notifyDataChanged()
            }
        }
    }

    private fun notifyDataChanged() {
        cacheDatas.forEach {
            onDataReceived?.invoke(it)
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

internal class UIOptions<T, R>(owner: LifecycleOwner, private val uniqueCode: Any, private val creator: UIHelperCreator<T, R>, result: (R) -> Unit) : LifecycleObserver {

    private val handleWhat = 0x1101
    private val executors = Executors.newFixedThreadPool(5)
    private val handler = Handler(Looper.getMainLooper()) {
        if (it.what == handleWhat) {
            castNotSafety<Any?, R?>(it.obj)?.let { r ->
                result(r)
            } ?: log("the data ${it.obj} was handled but null result in cast transform")
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
    fun post(data: Any): Boolean {
        if (data is Collection<*>) {
            (data as? Collection<*>)?.let {
                it.firstOrNull()?.let { c ->
                    if (c.javaClass == creator.inCls) {
                        (data as? Collection<T>)?.let { co ->
                            postListData(java.util.ArrayList(co))
                            return true
                        }
                    }
                }
            }
        }
        if (data.javaClass == creator.inCls || data.javaClass.simpleName == creator.inCls.simpleName) {
            postData(data as? T)
            return true
        }
        return false
    }

    private fun postListData(data: java.util.ArrayList<T>?) {
        data?.forEach {
            postData(it)
        }
    }

    private fun postData(data: T?) {
        if (data == null) {
            log("why are you post a null data and also register a type-null observer?");return
        }
        executors.submit(UIExecutor(creator, data) {
            handler.sendMessage(Message.obtain().apply {
                what = handleWhat
                obj = it
            })
        })
    }
}

internal class UIExecutor<T, R>(private val creator: UIHelperCreator<T, R>, private val data: T, private val finishd: (R) -> Unit) : Runnable {

    override fun run() {
        try {
            postData(data)?.let {
                finishd(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun postData(data: T): R? {
        return creator.filterIn?.let {
            if (it.invoke(data)) postFilterInData(data)
            else {
                log("the data $data may abandon with filter in")
                null
            }
        } ?: postFilterInData(data)
    }

    private fun postFilterInData(data: T): R? {
        val out: R = creator.handlerCls?.newInstance()?.handle(data) ?: castNotSafety(data)
        return postHandlerData(out)
    }

    private fun postHandlerData(data: R): R? {
        return (creator.filterOut?.let {
            if (it.invoke(data)) data
            else {
                log("the data $data may abandon with filter out")
                null
            }
        } ?: data)
    }
}