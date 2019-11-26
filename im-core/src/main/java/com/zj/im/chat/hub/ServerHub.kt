package com.zj.im.chat.hub

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.zj.im.chat.core.DataStore
import com.zj.im.chat.exceptions.ExceptionHandler
import com.zj.im.chat.exceptions.NecessaryAttributeEmptyException
import com.zj.im.chat.interfaces.*
import com.zj.im.main.ChatBase
import com.zj.im.main.ChatBase.context
import com.zj.im.net.socket.BaseSocketService
import com.zj.im.sender.SendObject
import com.zj.im.utils.cast
import com.zj.im.utils.log.NetRecordUtils

/**
 * Created by ZJJ
 *
 * the bridge of server, override and custom your server hub.
 */
abstract class ServerHub : BaseMessageHub() {

    private var baseSocketService: BaseSocketService? = null

    private var serviceConn: ServiceConnection? = null

    fun init() {
        serviceConn = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                onServiceDisConnected(baseSocketService)
            }

            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                baseSocketService = cast<IBinder?, BaseSocketService.SocketBinder?>(binder)?.service
                val service = getService("onServiceConnected")
                service?.initServer { return@initServer this@ServerHub }
                onServiceConnected(service)
            }
        }
        serviceConn?.let {
            context?.bindService(Intent(context, BaseSocketService::class.java), it, Service.BIND_AUTO_CREATE)
        }
    }

    fun getService(tag: String, ignoreNull: Boolean = false): BaseSocketService? {
        if (!isShutdown && !ignoreNull && baseSocketService == null) {
            ChatBase.postError(NecessaryAttributeEmptyException("at $tag \n socketService == null ,you must restart the sdk and recreate the service"))
        }
        return baseSocketService
    }

    /**
     * connect to socket
     */
    open fun connect(address: String?, port: Int?, socketTimeOut: Int?, callBack: ConnectCallBack?) {
        val service = getService("connect")
        if (service == null) callBack?.onConnection(false, null)
        service?.connect(address, port, socketTimeOut, callBack)
    }

    /**
     * heartbeats request
     *
     * @param params params of heartbeats request
     */
    open fun onHeartBeatsRequest(params: Map<String, Any>?, callBack: HeartBeatsCallBack) {
        val service = getService("onHeartBeatsRequest")
        if (service == null) callBack.heartBeats(false, null)
        service?.sendToSocket(packParams(params), callBack::heartBeats)
    }

    /**
     * request to send a message
     *
     * @param sendObject   params
     * @param callBack call when end of request
     */
    open fun sendToSocket(sendObject: SendObject, callBack: SendReplyCallBack?) {
        val service = getService("sendToSocket")
        if (service == null) callBack?.onReply(false, sendObject, null)
        val rawMsg = packParams(sendObject.getParams())
        NetRecordUtils.recordLastModifyReceiveData(rawMsg.size.toLong())
        service?.sendToSocket(rawMsg) { isOk, t ->
            callBack?.onReply(isOk, sendObject, t)
        }
    }

    internal fun receivedMessage(data: ByteArray) {
        NetRecordUtils.recordLastModifySendData(data.size.toLong())
        DataStore.put(BaseMsgInfo.receiveMsg(unPackParams(data)))
    }

    abstract fun packParams(param: Map<String, Any>?): ByteArray

    abstract fun unPackParams(data: ByteArray): Map<String, Any>?

    abstract fun onServiceConnected(service: Service?)

    abstract fun onServiceDisConnected(service: Service?)

    fun shutdown() {
        try {
            serviceConn?.let { context?.unbindService(it) }
            baseSocketService?.shutdown()
            baseSocketService = null
        } catch (e: Exception) {
            ExceptionHandler.postError(e)
        }
    }
}
