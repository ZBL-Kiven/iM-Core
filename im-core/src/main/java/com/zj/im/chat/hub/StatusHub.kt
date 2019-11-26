package com.zj.im.chat.hub

import com.zj.im.chat.enums.LifeType
import com.zj.im.chat.interfaces.IMLifecycleListener
import com.zj.im.chat.modle.IMLifecycle
import com.zj.im.main.ChatBase

object StatusHub : IMLifecycleListener {

    var isRunningInBackground = false
    var isNetWorkAccess = false
    var isTcpConnected = false

    private var lifeType = LifeType.START

    fun isRunning(): Boolean {
        return lifeType == LifeType.RESUME
    }

    override fun onLifecycle(state: IMLifecycle) {
        lifeType = state.type
        ChatBase.onLifecycle(state)
    }

}