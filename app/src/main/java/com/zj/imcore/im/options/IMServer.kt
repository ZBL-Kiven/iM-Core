package com.zj.imcore.im.options

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.interfaces.ConnectCallBack
import com.zj.im.chat.interfaces.SendingCallBack
import com.zj.im.chat.modle.SocketConnInfo
import com.zj.imcore.im.options.mod.BaseMod
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.nio.charset.Charset

class IMServer : ServerHub<String>(), WebSocketImpl {

    private var headers: Map<String, String>? = null
    private var connectListener: ConnectCallBack? = null
    private var uri: URI? = null
    private var handler: Handler? = null

    private var socket: WebSocketClient? = null
        get() {
            if(field != null && field?.isOpen ==false){
                field?.reconnect()
            }else if(field == null){
                uri?.let { field = WebSocketClient(this, it, headers) } ?: postToClose("the connect uri is empty or invalid")
            }
            return field
        }

    override fun init() {
        handler = Handler(Looper.getMainLooper()) {
            if (it.what == HEART_BEATS_EVENT) {
                sendHeartbeats()
                recordShakeState(HEART_BEATS_PING)
            }
            return@Handler false
        }
    }

    override fun onConnected(s: ServerHandshake?) {
        val code = s?.httpStatus ?: 404
        val str = s?.httpStatusMessage ?: ""
        val sortSuccess: Short = 101
        val b = sortSuccess == code
        if (b) {
            nextHeartbeats()
            connectListener?.onConnection(b, null)
        }
        print("IMServer", "socket connect ${if (b) "success" else "fail"}  case $str")
    }

    override fun onClose(errorCode: Int, case: String?, isFromRemote: Boolean) {
        when (errorCode) {
            -111 -> {
                print("IMServer", case ?: "socket closed by shutdown")
            }
            1006 -> {

            }
            else -> {
                postToClose(case ?: "socket closed by unkown error")
            }
        }
    }

    override fun onMessage(msg: String?) {
        val mod = Gson().fromJson(msg, BaseMod::class.java)
        if (recordShakeState(mod.type)) {
            onMessageReceived(msg)
        }
    }

    override fun onError(e: Exception?) {
        postToClose(e?.message ?: "the socket closed by an unkown exception")
    }

    override fun connect(connInfo: SocketConnInfo?, callBack: ConnectCallBack?) {
        var exc: Throwable? = null
        try {
            uri = URI.create(connInfo?.address)
            socket!!.run {
                connectionLostTimeout = connInfo?.socketTimeOut ?: 10000
                connect()
            }
        } catch (e: Exception) {
            exc = e
            socket?.close(-111)
            socket = null
        } finally {
            if (exc != null) {
                callBack?.onConnection(false, exc)
            } else this.connectListener = callBack
        }
    }

    override fun send(params: String?, callId: String, callBack: SendingCallBack?): Long {
        var exc: Throwable? = null
        try {
            socket!!.send(params)
        } catch (e: Exception) {
            exc = e
        } finally {
            callBack?.result(exc == null, exc)
            val b = params?.toByteArray(Charset.forName("UTF-8"))
            return (b?.size ?: 0) * 1L
        }
    }

    override fun closeSocket() {
        socket?.close(-111, "the socket will close by shutdown")
    }

    private fun onMessageReceived(msg: String?) {
        msg?.let {
            val size = it.toByteArray(Charset.forName("UTF-8")).size.toLong()
            postReceivedMessage(it, false, size)
        }
    }

    private var pingHasNotResponseCount = 0
    private var pongTime = 0L
    private var pingTime = 0L
    private var heartBeatsTime = 0L

    private fun recordShakeState(type: String): Boolean {

        return try {
            when {
                type.equals(HEART_BEATS_PONG, true) -> { //pong
                    pingHasNotResponseCount = 0
                    pongTime = System.currentTimeMillis()
                    nextHeartbeats()
                    false
                }
                type.equals(HEART_BEATS_PING, true) -> { //ping
                    val curTime = System.currentTimeMillis()
                    val outOfTime = heartBeatsTime * 3f
                    val lastPingTime = curTime - pingTime - heartBeatsTime
                    if (pingTime > 0 && pongTime <= 0) {
                        pingHasNotResponseCount++
                    }
                    if (pingHasNotResponseCount > 3 || (pongTime > 0L && curTime - (pongTime + lastPingTime) > outOfTime)) {
                        pongTime = 0L
                        pingTime = 0L
                        pingHasNotResponseCount = 0
                        postToClose(PING_TIMEOUT)
                    }
                    pingTime = System.currentTimeMillis()
                    false
                }
                else -> true
            }
        } finally {
            print("record socket state", type)
        }
    }

    private fun nextHeartbeats() {
        handler?.removeMessages(HEART_BEATS_EVENT)
        handler?.sendEmptyMessageDelayed(HEART_BEATS_EVENT, HEART_BEATS_TIME)
    }

    private fun sendHeartbeats() {
        val heartbeats = BaseMod().apply {
            this.callId = CALL_ID_HEART_BEATS
            this.type = HEART_BEATS_PING
        }
        send(Gson().toJson(heartbeats), CALL_ID_HEART_BEATS, null)
    }

    companion object {
        const val PING_TIMEOUT = "the socket would reconnection because the ping was no response too many times!"
        const val HEART_BEATS_EVENT = 0xf1365
        const val HEART_BEATS_TIME = 5000L
        const val CALL_ID_HEART_BEATS = "CUS-M61C-Q3TN-OX6Y"
        const val HEART_BEATS_PING = "ping"
        const val HEART_BEATS_PONG = "pong"
    }
}
