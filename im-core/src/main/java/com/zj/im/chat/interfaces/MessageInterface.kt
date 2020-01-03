package com.zj.im.chat.interfaces

import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.modle.IMLifecycle
import com.zj.im.utils.MainLooper
import com.zj.im.utils.netUtils.NetWorkInfo
import com.zj.im.main.StatusHub
import com.zj.im.utils.log.NetWorkRecordInfo
import com.zj.im.utils.log.TCPNetRecordChangedListener

abstract class MessageInterface<T> {

    private var socketStateObserver: MutableMap<String, ((SocketState) -> Unit)?>? = null
        get() {
            if (field == null) field = mutableMapOf()
            return field
        }

    fun registerSocketStateChangeListener(name: String, observer: (SocketState) -> Unit) {
        this.socketStateObserver?.put(name, observer)
        MainLooper.post {
            observer(StatusHub.curSocketState)
        }
    }

    fun removeSocketStateChangeListener(name: String) {
        this.socketStateObserver?.remove(name)
    }

    internal fun onSocketStatusChanged(state: SocketState) {
        MainLooper.post {
            socketStateObserver?.forEach { (_, p) ->
                p?.invoke(state)
            }
        }
    }

    private var netWorkStateObserver: MutableMap<String, ((NetWorkInfo) -> Unit)?>? = null
        get() {
            if (field == null) field = mutableMapOf()
            return field
        }

    fun registerNetWorkStateChangeListener(name: String, observer: (NetWorkInfo) -> Unit) {
        this.netWorkStateObserver?.put(name, observer)

    }

    fun removeNetWorkStateChangeListener(name: String) {
        this.netWorkStateObserver?.remove(name)
    }

    internal fun onNetWorkStatusChanged(state: NetWorkInfo) {
        MainLooper.post {
            netWorkStateObserver?.forEach { (_, p) ->
                p?.invoke(state)
            }
        }
    }

    private var lifecycleListeners: HashMap<String, LifecycleListener>? = null
        get() {
            if (field == null) field = hashMapOf()
            return field
        }

    fun registerLifecycleListener(name: String, lifecycleListener: LifecycleListener) {
        lifecycleListeners?.put(name, lifecycleListener)
    }

    fun unRegisterLifecycleListener(name: String) {
        lifecycleListeners?.remove(name)
    }

    protected fun onLifecycle(state: IMLifecycle) {
        lifecycleListeners?.forEach { (k, v) ->
            MainLooper.post {
                try {
                    v.status(k, state)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private var changedListeners: HashMap<String, TCPNetRecordChangedListener>? = null
        get() {
            if (field == null) field = hashMapOf()
            return field
        }

    fun addRecordListener(name: String, tc: TCPNetRecordChangedListener) {
        changedListeners?.put(name, tc)
    }

    fun removeRecordListener(name: String) {
        changedListeners?.remove(name)
    }

    internal open fun onRecordChange(info: NetWorkRecordInfo) {
        changedListeners?.forEach { (_, v) ->
            MainLooper.post {
                v.onChanged(info)
            }
        }
    }
}