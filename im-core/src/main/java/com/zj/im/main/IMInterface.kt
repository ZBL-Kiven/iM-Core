@file:Suppress("unused")

package com.zj.im.main

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.core.BaseOption
import com.zj.im.chat.enums.RuntimeEfficiency
import com.zj.im.chat.exceptions.*
import com.zj.im.chat.exceptions.AuthFailException
import com.zj.im.chat.exceptions.ExceptionHandler
import com.zj.im.chat.exceptions.LooperInterruptedException
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.interfaces.MessageInterface
import com.zj.im.chat.utils.TimeOutUtils
import com.zj.im.net.socket.BaseSocketService
import com.zj.im.sender.SendObject
import com.zj.im.utils.cast
import com.zj.im.utils.log.logger.FileUtils
import com.zj.im.utils.log.logger.printInFile
import java.io.File
import java.lang.IllegalArgumentException

/**
 * created by ZJJ
 *
 * extend this and call init before use ,or it will be crash without init!!
 *
 * the entry of chatModule ,call register/unRegister listeners to observer/cancel the msg received
 *
 * you can call pause/resume to modify the messagePool`s running state.
 *
 * @property getClient return your custom client for sdk {@see ClientHub}
 *
 * @property getServer return your custom server for sdk {@see ServerHub}
 *
 * @property onError handler the sdk errors with runtime
 *
 * @property prepare on SDK init prepare
 *
 * @property shutdown it called when SDK was shutdown
 *
 * @property onLayerChanged it called when SDK was changed form foreground / background
 *
 */


abstract class IMInterface<T> : MessageInterface() {

    private var baseSocketService: ChatBase<T>? = null

    private var serviceConn: ServiceConnection? = null

    protected fun initChat(option: BaseOption<T>) {
        this.option = option
        serviceConn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                this@IMInterface.option?.buildOption.onServiceDisConnected()
            }

            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                baseSocketService = cast<IBinder?, ChatBase.SocketBinder<T>?>(binder)?.service
                baseSocketService?.init(this@IMInterface)
                this@IMInterface.option?.buildOption.onServiceConnected()
            }
        }
        serviceConn?.let {
            this.option?.context?.let { ctx ->
                ctx.bindService(Intent(ctx, BaseSocketService::class.java), it, Service.BIND_AUTO_CREATE)
            }
        }
    }
    private fun getService(tag: String, ignoreNull: Boolean = false): ChatBase<T>? {
        if (!ignoreNull && baseSocketService == null) {
            baseSocketService?.postError(NecessaryAttributeEmptyException("at $tag \n socketService == null ,you must restart the sdk and recreate the service"))
        }
        return baseSocketService
    }


    abstract fun getClient(): ClientHub

    abstract fun getServer(): ServerHub<T>

    abstract fun onError(e: ChatException)

    open fun checkNetWorkIsWorking(): Boolean = true

    open fun prepare() {}

    open fun shutdown() {}

    open fun onLayerChanged(inBackground: Boolean) {}

    open fun onServiceConnected() {}

    open fun onServiceDisConnected() {}

    internal var option: BaseOption<T>? = null









    internal fun isInterrupt(): Boolean {
        return getClient() != null && getServer() != null
    }

    internal fun initMsgHandler(runningKey: String) {
        option?.initMsgHandler(runningKey)
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

    fun onPause(code: Int) {
        option?.pause(code)
        pause(code)
    }

    fun onResume(code: Int) {
        option?.resume(code)
        resume(code)
    }

    fun shutDown() {
        option?.shutDown()
        ChatBase.shutDown()
        destroy()
    }

    protected open fun pause(code: Int) {}

    protected open fun resume(code: Int) {}

    protected open fun destroy() {}

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
