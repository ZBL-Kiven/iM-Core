package com.zj.im.chat.interfaces

import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.exceptions.ExceptionHandler
import com.zj.im.chat.hub.StatusHub
import com.zj.im.chat.modle.IMLifecycle
import com.zj.im.chat.utils.MainLooper
import com.zj.im.chat.utils.netUtils.IConnectivityManager
import com.zj.im.chat.utils.netUtils.NetWorkInfo
import com.zj.im.utils.log.NetWorkRecordInfo
import com.zj.im.utils.log.TCPNetRecordChangedListener

abstract class MessageInterface {

    /**----------*/

    private var socketStateObserver: MutableMap<String, ((SocketState) -> Unit)?>? = null
        get() {
            if (field == null) field = mutableMapOf()
            return field
        }

    fun registerSocketStateChangeListener(name: String, observer: (SocketState) -> Unit) {
        observer(SocketState.INIT)
        this.socketStateObserver?.put(name, observer)
    }

    fun removeSocketStateChangeListener(name: String) {
        this.socketStateObserver?.remove(name)
    }

    internal fun onSocketStatusChanged(state: SocketState) {
        StatusHub.isTcpConnected = state.isConnected()
        socketStateObserver?.forEach { (_, p) ->
            p?.invoke(state)
        }
    }

    /**----------*/

    private var netWorkStateObserver: MutableMap<String, ((NetWorkInfo) -> Unit)?>? = null
        get() {
            if (field == null) field = mutableMapOf()
            return field
        }


    fun registerNetWorkStateChangeListener(name: String, observer: (NetWorkInfo) -> Unit) {
        observer(IConnectivityManager.isNetWorkActive)
        this.netWorkStateObserver?.put(name, observer)
    }


    fun removeNetWorkStateChangeListener(name: String) {
        this.netWorkStateObserver?.remove(name)
    }

    internal fun onNetWorkStatusChanged(state: NetWorkInfo) {
        netWorkStateObserver?.forEach { (_, p) ->
            p?.invoke(state)
        }
    }

    /**----------*/

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

    internal fun onLifecycle(state: IMLifecycle) {
        lifecycleListeners?.forEach { (k, v) ->
            try {
                v.status(k, state)
            } catch (e: Exception) {
                ExceptionHandler.postError(e)
            }
        }
    }

    /**----------*/

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

    internal fun onRecordChange(info: NetWorkRecordInfo) {
        changedListeners?.forEach { (_, v) ->
            MainLooper.post {
                v.onChanged(info)
            }
        }
    }

    /**-------------*/

    /**
     *override after connected and intercept the nex step with :isContinue
     * */
    internal var onConnected: ((isContinue: (Boolean) -> Unit) -> Unit)? = { it(true) }

    fun overrideOnConnected(obj: (isContinue: (Boolean) -> Unit) -> Unit) {
        this.onConnected = obj
    }

    /**
     * override after auth and intercept the nex step with :isContinue
     * */
    internal var onAuthSuccess: ((isContinue: (Boolean) -> Unit) -> Unit)? = { it(true) }

    fun overrideOnAuthSuccess(obj: (isContinue: (Boolean) -> Unit) -> Unit) {
        this.onAuthSuccess = obj
    }

    /**-------------*/

    abstract fun onMsgPatch(data: AnalyzingData, onFinish: () -> Unit)

    abstract fun progressUpdate(progress: Int, callId: String)

    /**-------------*/

}