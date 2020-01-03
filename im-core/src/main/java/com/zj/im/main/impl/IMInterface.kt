package com.zj.im.main.impl

import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.zj.im.chat.core.BaseOption
import com.zj.im.chat.exceptions.NecessaryAttributeEmptyException
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.interfaces.MessageInterface
import com.zj.im.main.ChatBase
import com.zj.im.utils.cast
import com.zj.im.utils.log.logger.logUtils


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
 * @property onAppLayerChanged it called when SDK was changed form foreground / background
 *
 */
abstract class IMInterface<T> : MessageInterface<T>() {

    private var baseSocketService: ChatBase<T>? = null

    private var serviceConn: ServiceConnection? = null

    private var client: ClientHub<T>? = null

    private var server: ServerHub<T>? = null

    private var isServiceConnected = false

    internal var option: BaseOption? = null

    protected fun initChat(option: BaseOption) {
        this.option = option
        baseSocketService?.let {
            it.init(this)
            return
        }
        serviceConn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                isServiceConnected = false
                this@IMInterface.onServiceDisConnected()
            }

            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                baseSocketService = cast<IBinder?, ChatBase.SocketBinder<T>?>(binder)?.service
                baseSocketService?.init(this@IMInterface)
                isServiceConnected = true
                this@IMInterface.onServiceConnected()
            }
        }
        serviceConn?.let {
            this.option?.context?.let { ctx ->
                ctx.bindService(Intent(ctx, ChatBase::class.java), it, Service.BIND_AUTO_CREATE)
            }
        }
    }

    private fun getService(tag: String, ignoreNull: Boolean = false): ChatBase<T>? {
        if (!ignoreNull && baseSocketService == null) {
            onError(NecessaryAttributeEmptyException("at $tag \n socketService == null ,you must restart the sdk and recreate the service"))
        }
        logUtils.d("IMInterface.getService", tag)
        return baseSocketService
    }

    internal fun getClient(case: String = ""): ClientHub<T>? {
        logUtils.d("IMI.getClient", case)
        if (client == null) {
            client = getClient()
            logUtils.d("IMI.getClient", "create client with $case")
        }
        if (client == null) {
            postError(NecessaryAttributeEmptyException("can't create a client by null!"))
        }
        return client
    }

    internal fun getServer(case: String = ""): ServerHub<T>? {
        logUtils.d("IMI.getServer", case)
        if (server == null) {
            server = getServer()
            logUtils.d("IMI.getServer", "create server with $case")
        }
        if (server == null) {
            postError(NecessaryAttributeEmptyException("can't create a server by null!"))
        }
        return server
    }

    internal fun getNotification(): Notification? {
        return option?.notification
    }

    internal fun getSessionId(): Int {
        return option?.sessionId ?: -1
    }

    protected abstract fun getClient(): ClientHub<T>

    protected abstract fun getServer(): ServerHub<T>

    abstract fun onError(e: Throwable)

    open fun prepare() {}

    open fun shutdown() {}

    open fun onAppLayerChanged(isHidden: Boolean) {}

    open fun onServiceConnected() {}

    open fun onServiceDisConnected() {}

    fun postError(e: Throwable) {
        onError(e)
    }

    fun shutDown(case: String) {
        getService("shutDown by $case", true)?.shutDown()
        serviceConn?.let {
            option?.context?.unbindService(it)
        }
        serviceConn = null
        baseSocketService = null
        client = null
        server = null
    }
}
