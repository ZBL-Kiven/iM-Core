package com.zj.im.scheduler

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.zj.im.cast
import com.zj.im.main.UIHelper
import com.zj.im.mainHandler
import com.zj.im.store.UIMaker
import com.zj.im.store.interfaces.DataHandler
import com.zj.im.store.interfaces.DataListener
import com.zj.im.store.interfaces.EventCallBack
import java.util.ArrayList

/**
 * Created by ZJJ
 */

@Suppress("unused")
class ReceiveListener<IN, OUT : Any> constructor(private val name: String, private val cI: Class<IN>, private val cO: Class<OUT>) : LifecycleObserver {

    companion object {
        inline fun <reified IN, reified OUT : Any> create(name: String, owner: LifecycleOwner): ReceiveListener<IN, OUT> {
            val l = ReceiveListener(name, IN::class.java, OUT::class.java)
            owner.lifecycle.addObserver(l)
            return l
        }
    }

    private var eventCallBack: EventCallBack<IN, OUT>? = null
    private var query: Query<IN, OUT>? = null
    private var listener: DataListener<OUT>? = null
    private var uiMaker: UIMaker<IN, OUT, *>? = null
    private var canReceive: Boolean = false
    private val tempCache: MutableList<OUT> = mutableListOf()

    fun query(): Query.QueryData<OUT> {
        return Query.from<IN, OUT>(cO) {
            this.query = it
            return@from this@ReceiveListener
        }
    }

    fun lock(locked: Boolean) {
        this.canReceive = !locked
        if (!locked) notifyDataReceived()
    }

    fun addHandler(eventCallBack: EventCallBack<IN, OUT>): ReceiveListener<IN, OUT> {
        this.eventCallBack = eventCallBack
        return this
    }

    fun subscribe(listener: DataListener<OUT>): ReceiveListener<IN, OUT> {
        this.listener = listener
        uiMaker = UIMaker(dataHandler, eventCallBack)
        return this
    }

    private val dataHandler = object : DataHandler<OUT>() {

        override fun onDataGot(r: OUT?) {
            if (r == null) return
            listener?.let {
                filter(query, r)?.let { o ->
                    tempCache.add(o)
                    if (canReceive) notifyDataReceived()
                    return@onDataGot
                }
                com.zj.im.log("the data may lose by data handler")
            } ?: com.zj.im.log("the listener was null, ensure that you're never subscribe?")
        }
    }

    private fun notifyDataReceived() {
        synchronized(tempCache) {
            val lst = ArrayList(tempCache)
            tempCache.clear()
            lst.forEach {
                mainHandler.post { listener?.onReceived(it) }
            }
        }
    }

    /**
     * post a data or data list into ui-maker
     * */
    internal fun postData(data: Any?) {
        if (data == null) com.zj.im.log("the received data was null ,so it never process by ui-maker")
        else {
            if (data.javaClass != cI && (data is Collection<*> && data.toMutableList()[0]?.javaClass != cI)) {
                return
            }
            uiMaker?.let { maker ->
                synchronized(maker) {
                    cast<Any, List<IN>>(data)?.let {
                        maker.pushAll(it)
                    } ?: cast<Any, IN>(data)?.let {
                        maker.push(it)
                    }
                }
            } ?: com.zj.im.log("the data may abandoned by a null ui-maker")
        }
    }

    /**
     * filter if wrong classes, wrong query filters and filter data before get.
     * */
    private fun filter(query: Query<IN, OUT>?, data: OUT): OUT? {
        return if (query != null && !query.getQueryResult(data)) null else data
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun onCreate() {
        UIHelper.registerReceivedListener(name, this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        UIHelper.unRegisterReceivedListener(name)
        uiMaker?.onDestroy()
    }
}
