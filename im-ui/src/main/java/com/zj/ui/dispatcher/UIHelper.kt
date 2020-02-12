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
import java.io.Serializable
import java.lang.Exception
import java.util.concurrent.Executors

enum class Category : Serializable {

    SINGLE, DATA;

    var index: Int = 0
    var end: Int = 0

    fun setListDataIndexing(i: Int, i1: Int): Category {
        DATA.index = i
        DATA.end = i1
        return this@Category
    }
}

data class CacheData<R>(val d: R, val s: String?, val c: Category)

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

    internal var filterIn: ((T, String?, Category) -> Boolean)? = null
    internal var filterOut: ((R, String?, Category) -> Boolean)? = null
    private var onDataReceived: ((R, String?, Category) -> Unit)? = null
    private var isPaused: Boolean = false
    private val cacheDatas = hashSetOf<CacheData<R>>()

    fun filterIn(filter: (T, String?, Category) -> Boolean): UIHelperCreator<T, R> {
        this.filterIn = filter
        return this
    }

    fun filterOut(filter: (R, String?, Category) -> Boolean): UIHelperCreator<T, R> {
        this.filterOut = filter
        return this
    }

    fun listen(onDataReceived: (R, String?, Category) -> Unit) {
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
            onDataReceived?.invoke(it.d, it.s, it.c)
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

internal class UIOptions<T, R>(owner: LifecycleOwner, private val uniqueCode: Any, private val creator: UIHelperCreator<T, R>, result: (R, String?, Category) -> Unit) : LifecycleObserver {

    private val pal = "payload"
    private val cag = "category"
    private val handleWhat = 0x1101
    private val executors = Executors.newFixedThreadPool(5)
    private val handler = Handler(Looper.getMainLooper()) {
        if (it.what == handleWhat) {
            val b = it.data
            val payload = if (b.containsKey(pal)) b.getString(pal) else null
            val category = b.getSerializable(cag) as Category
            castNotSafety<Any?, R?>(it.obj)?.let { r ->
                result(r, payload, category)
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
    fun post(data: Any, payload: String?): Boolean {
        if (data is Collection<*>) {
            (data as? Collection<*>)?.let {
                it.firstOrNull()?.let { c ->
                    if (c.javaClass == creator.inCls) {
                        (data as? Collection<T>)?.let { co ->
                            postListData(java.util.ArrayList(co), payload)
                            return true
                        }
                    }
                }
            }
        }
        if (data.javaClass == creator.inCls || data.javaClass.simpleName == creator.inCls.simpleName) {
            postData(data as? T, payload, Category.SINGLE)
            return true
        }
        return false
    }

    private fun postListData(data: java.util.ArrayList<T>?, payload: String?) {
        val size = data?.size ?: 0
        data?.forEachIndexed { i, d ->
            postData(d, payload, Category.DATA.setListDataIndexing(i, size))
        }
    }

    private fun postData(data: T?, payload: String?, category: Category) {
        if (data == null) {
            log("why are you post a null data and also register a type-null observer?");return
        }
        executors.submit(UIExecutor(creator, data, payload, category) { d, s, c ->
            handler.sendMessage(Message.obtain().apply {
                what = handleWhat
                obj = d
                val b = Bundle()
                if (!payload.isNullOrEmpty()) b.putString(pal, s)
                b.putSerializable(cag, c)
                this.data = b
            })
        })
    }
}

internal class UIExecutor<T, R>(private val creator: UIHelperCreator<T, R>, private val data: T, private val payload: String?, private val category: Category, private val finishd: (R, String?, Category) -> Unit) : Runnable {

    override fun run() {
        try {
            postData(data)?.let {
                finishd(it, payload, category)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun postData(data: T): R? {
        return creator.filterIn?.let {
            if (it.invoke(data, payload, category)) postFilterInData(data)
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
            if (it.invoke(data, payload, category)) data
            else {
                log("the data $data may abandon with filter out")
                return@postHandlerData null
            }
        } ?: data)
    }
}