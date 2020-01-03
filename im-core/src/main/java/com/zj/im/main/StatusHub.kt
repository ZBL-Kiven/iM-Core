package com.zj.im.main

import com.zj.im.chat.enums.LifeType
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.modle.IMLifecycle

internal object StatusHub {

    var isRunningInBackground = false
    var isNetWorkAccess = false
    var isReceiving = false

    var curSocketState: SocketState = SocketState.INIT

    private var lifeType = LifeType.START

    fun isRunning(): Boolean {
        return lifeType == LifeType.RESUME
    }

    fun isAlive(): Boolean {
        return lifeType != LifeType.STOP
    }

    fun isDataEnable(): Boolean {
        return curSocketState.isConnected() && isNetWorkAccess
    }

    fun onLifecycle(state: IMLifecycle) {
        lifeType = state.type
    }
}