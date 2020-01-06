package com.zj.im.chat.hub

import com.zj.im.chat.enums.LifeType
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.enums.SocketState
import com.zj.im.main.StatusHub
import com.zj.im.main.dispatcher.DataReceivedDispatcher
import com.zj.im.chat.modle.BaseMsgInfo
import com.zj.im.chat.modle.IMLifecycle
import com.zj.im.utils.log.logger.printInFile

/**
 * Created by ZJJ
 *
 * the bridge of client, override and custom your client hub.
 *
 * it may reconnection if change the system clock to earlier.
 *
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class ClientHub<T>{

    protected abstract fun onMsgPatch(data: T?, callId: String?, isSpecialData: Boolean, sendingState: SendMsgState?, isResent: Boolean, onFinish: () -> Unit)

    abstract fun progressUpdate(progress: Int, callId: String)

    fun setConnectState(state: SocketState) {
        DataReceivedDispatcher.pushData(BaseMsgInfo.connectStateChange<T>(state))
    }

    internal fun onSendingStateChanged(sendingState: SendMsgState, callId: String, data: T?, isSpecialData: Boolean, resend: Boolean) {
        onMsgPatch(data, callId, isSpecialData, sendingState, resend) {
            StatusHub.isReceiving = false
        }
    }

    open fun canReceived(): Boolean {
        return StatusHub.isRunning() && !StatusHub.isReceiving && DataReceivedDispatcher.isDataEnable()
    }

    open fun canSend(): Boolean {
        return StatusHub.isAlive() &&  DataReceivedDispatcher.isDataEnable()
    }

    internal fun pause(code: Int) {
        printInFile("on pause called ", "$code --- onPause")
        StatusHub.onLifecycle(IMLifecycle(LifeType.PAUSE, code))
    }

    internal fun resume(code: Int) {
        printInFile("on resume called ", "$code --- onResume")
        StatusHub.onLifecycle(IMLifecycle(LifeType.RESUME, code))
    }

    internal fun shutdown() {
        StatusHub.onLifecycle(IMLifecycle(LifeType.STOP, -1))
    }
}
