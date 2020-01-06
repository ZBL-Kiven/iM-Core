package com.zj.im.main

import com.zj.im.chat.enums.LifeType
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.modle.IMLifecycle
import com.zj.im.main.dispatcher.DataReceivedDispatcher
import com.zj.im.utils.netUtils.IConnectivityManager
import com.zj.im.utils.netUtils.NetWorkInfo

internal object StatusHub {

    var isRunningInBackground = false
    val isNetWorkAccess: Boolean
        get() {
            return IConnectivityManager.isNetWorkActive == NetWorkInfo.CONNECTED
        }
    var isReceiving = false

    var curSocketState: SocketState = SocketState.INIT

    private var lifeType = IMLifecycle(LifeType.START, 0)
        set(value) {
            if (field != value) {
                field = value
                DataReceivedDispatcher.onLifeStateChanged(field)
            }
        }

    fun isRunning(): Boolean {
        return lifeType.type == LifeType.RESUME
    }

    fun isAlive(): Boolean {
        return lifeType.type != LifeType.STOP
    }

    fun isDataEnable(): Boolean {
        return curSocketState.isConnected() && isNetWorkAccess
    }

    fun onLifecycle(state: IMLifecycle) {
        lifeType = state
    }
}