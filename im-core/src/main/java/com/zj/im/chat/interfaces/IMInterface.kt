@file:Suppress("unused")

package com.zj.im.chat.interfaces

import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.exceptions.ChatException
import com.zj.im.chat.core.BaseOption
import com.zj.im.chat.enums.RuntimeEfficiency
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.utils.TimeOutUtils
import com.zj.im.main.ChatBase
import com.zj.im.sender.SendObject

/**
 * created by ZJJ
 *
 * extend this and call init before use ,or it will be crash without init!!
 *
 * the entry of chatModule ,call register/unRegister listeners to observer/cancel the msg received
 *
 * you can call pause/resume to modify the messagePool`s running state.
 * */

abstract class IMInterface : MessageInterface() {

    internal var option: BaseOption? = null

    protected fun initChat(option: BaseOption) {
        this.option = option
        ChatBase.init(this)
    }

    internal fun getServer(where: String = ""): ServerHub? {
        return option?.getServer(where)
    }

    internal fun getClient(where: String = ""): ClientHub? {
        return option?.getClient(where)
    }

    internal fun onLayerChanged(isHidden: Boolean) {
        option?.onLayerChanged(isHidden)
    }

    internal fun isInterrupt(): Boolean {
        return getClient() != null && getServer() != null
    }

    internal fun initMsgHandler(runningKey: String) {
        option?.initMsgHandler(runningKey)
    }


    internal fun checkNetWorkIsWorking(): Boolean {
        return option?.buildOption?.checkNetWorkIsWorking() ?: true
    }

    /**
     * send a msg
     * */
    fun send(sendObject: SendObject) {
        option?.sendToSocket(sendObject)
    }

    /**
     * query is the msg exists in queue
     * */
    fun queryInSending(callId: String?): Boolean {
        return ChatBase.queryInQueue(callId)
    }

    /**
     * remove msg if it exists in msg queue
     * */
    fun cancelSendingMsg(callId: String?) {
        ChatBase.deleteFormQueue(callId)
    }

    fun setFrequency(efficiency: RuntimeEfficiency) {
        option?.setFrequency(efficiency)
    }

    fun pause(code: Int) {
        option?.pause(code)
    }

    fun resume(code: Int) {
        option?.resume(code)
    }

    fun shutDown() {
        option?.shutDown()
        ChatBase.shutDown()
    }

    fun postError(e: ChatException) {
        ChatBase.postError(e)
    }

    fun cancelTimeOut(callId: String) {
        TimeOutUtils.remove(callId)
    }

    fun reconnection(case: String) {
        ChatBase.correctConnectionState(SocketState.CONNECTED_ERROR, case)
    }

    fun showToast(s: String) {
        ChatBase.show(s)
    }

    fun getLogsFolderPath(zipFolderName: String, zipFileName: String): String {
        return ChatBase.getLogsFolder(zipFolderName, zipFileName)
    }
}
