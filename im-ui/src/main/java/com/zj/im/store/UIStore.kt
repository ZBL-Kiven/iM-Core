package com.zj.im.store

import com.zj.im.log
import com.zj.im.store.interfaces.DataHandler
import com.zj.im.store.interfaces.EventCallBack
import com.zj.im.with
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.Executors
import kotlin.Comparator

@Suppress("unused")
internal class UIStore<DATA, R, HANDLER : DataHandler<R>>(private val msgHandler: DataHandler<R>, private val eventCall: EventCallBack<DATA, R>) {

    private var comparator = Comparator<DATA> { i1, i2 ->
        return@Comparator eventCall.compare(i1, i2)
    }
    private var isDataHanding = false
    private val handleExecutors = Executors.newSingleThreadExecutor()

    private var uiQueue: PriorityQueue<DATA>? = null
        get() {
            if (field == null) field = PriorityQueue(100000, comparator)
            return field
        }

    fun put(data: DATA): Boolean {
        return uiQueue?.with {
            if (it.offer(data)) {
                notifyDataSetChanged()
                true
            } else false
        } ?: false
    }

    fun putAll(data: Collection<DATA>): Boolean {
        return uiQueue?.with {
            if (it.addAll(data)) {
                notifyDataSetChanged()
                return@with true
            } else false
        } ?: false
    }

    private fun poll(): DATA? {
        if (!isDataHanding) {
            val has = uiQueue?.with {
                it.poll()
            }
            if (has != null) {
                isDataHanding = true
            }
            return has
        }
        throw IllegalArgumentException("the data is handing aware")
    }

    private fun pollAndHandle(poll: DATA) {
        val handleRunnable = HandleRunnable(poll, eventCall) {
            if (it == null) {
                log("data may abandon by handler")
            } else pushHandledData(it)
            notifyDataHandled()
        }
        handleExecutors.submit(handleRunnable)
    }

    private fun notifyDataSetChanged() {
        if (!isDataHanding) {
            val poll = poll() ?: return
            pollAndHandle(poll)
        }
    }

    private fun notifyDataHandled() {
        isDataHanding = false
        val poll = poll() ?: return
        pollAndHandle(poll)
    }

    private fun pushHandledData(data: R?) {
        msgHandler.onDataGot(data)
    }

    fun onDestroy() {
        uiQueue?.with { it.clear() }
        val r = handleExecutors.shutdownNow()
        r.clear()
        isDataHanding = false
    }
}