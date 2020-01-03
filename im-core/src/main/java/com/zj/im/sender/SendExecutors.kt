package com.zj.im.sender

import com.zj.im.chat.hub.ServerHub
import com.zj.im.main.StatusHub
import com.zj.im.chat.modle.BaseMsgInfo
import com.zj.im.chat.interfaces.SendingCallBack
import com.zj.im.chat.modle.SendingUp
import com.zj.im.utils.TimeOutUtils
import com.zj.im.main.dispatcher.DataReceivedDispatcher

/**
 * Created by ZJJ
 */

internal class SendExecutors<T>(info: BaseMsgInfo<T>, server: ServerHub<T>?, done: (isOK: Boolean, info: BaseMsgInfo<T>, e: Throwable?) -> Unit) {

    init {
        var exc: Throwable? = null
        fun sendingFail() {
            TimeOutUtils.remove(info.callId)
        }
        try {
            when (SendingUp.CANCEL) {
                info.sendingUp -> sendingFail()
                else -> {
                    TimeOutUtils.putASentMessage(info.callId, info.data, info.timeOut, info.isResend, info.ignoreConnecting)
                    server?.sendToSocket(info.data, info.callId, object : SendingCallBack {

                        override fun result(isOK: Boolean, throwable: Throwable?) {
                            try {
                                exc = throwable
                                if (!isOK) {
                                    if (!StatusHub.isDataEnable()) {
                                        TimeOutUtils.remove(info.callId)
                                        DataReceivedDispatcher.pushData(info)
                                        return
                                    }
                                    sendingFail()
                                }
                            } catch (e: Exception) {
                                exc = e
                            }
                        }
                    })
                }
            }
        } catch (e: Exception) {
            exc = e
        } finally {
            done(exc == null, info, exc)
        }
    }
}
