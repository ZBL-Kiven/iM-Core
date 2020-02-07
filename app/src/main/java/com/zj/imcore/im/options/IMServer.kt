package com.zj.imcore.im.options

import android.app.Application
import android.os.Handler
import com.alibaba.fastjson.JSON
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.interfaces.SendingCallBack
import com.zj.im.utils.nio
import com.zj.imcore.im.options.mod.BaseMod
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.nio.charset.Charset

class IMServer : ServerHub<String>(), WebSocketImpl {

    private val headers: Map<String, String>? = null
    private var handler: Handler? = null
    private var mSocket: WebSocketClient? = null

    override fun init(context: Application?) {
        super.init(context)
        handler = Handler {
            when (it.what) {
                HEART_BEATS_EVENT -> {
                    sendHeartbeats()
                }
                CONNECTION_EVENT -> {
                    connect()
                }
            }
            return@Handler false
        }
        connectDelay(0)
    }

    private fun connectDelay(connTime: Long = RECONNECTION_TIME) {
        handler?.removeMessages(CONNECTION_EVENT)
        handler?.sendEmptyMessageDelayed(CONNECTION_EVENT, connTime)
    }

    override fun onOpen(s: ServerHandshake?) {
        curSocketState = SocketState.CONNECTED
        nextHeartbeats()
    }

    override fun onClose(errorCode: Int, case: String?, isFromRemote: Boolean) {
        when (errorCode) {
            1002 -> print("IMServer", "socket erro with 1002  case: $case")
            -111 -> print("IMServer", case ?: "socket closed by shutdown")
            1006 -> print("IMServer", "socket erro with 1006  case: $case")
            else -> curSocketState = SocketState.CONNECTED_ERROR.case("the socket have to reconnection with error: $case")
        }
    }

    override fun onError(e: Exception?) {
        curSocketState = SocketState.CONNECTED_ERROR.case("the socket have to reconnection with error: ${e?.message}")
    }

    private fun connect() {
        curSocketState = SocketState.CONNECTION
        val connInfo = getConnectionInfo()
        try {
            var isReconn = false
            if (mSocket != null && mSocket?.isOpen == false) {
                mSocket?.reconnect()
                isReconn = true
            }
            if (mSocket == null) {
                val uri = URI.create(connInfo)
                mSocket = WebSocketClient(this, uri, this.headers)
            }
            if (!isReconn) {
                mSocket?.connectionLostTimeout = CONNECTION_TIME_OUT
                mSocket?.connect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun send(params: String, callId: String, callBack: SendingCallBack): Long {
        var exc: Throwable? = null
        try {
            mSocket!!.send(params)
        } catch (e: Exception) {
            exc = e
        } finally {
            callBack.result(exc == null, exc)
            val b = params.toByteArray(Charset.forName("UTF-8"))
            return b.size.toLong()
        }
    }

    override fun onMessage(msg: String?) {
        val mod = JSON.parseObject(msg, BaseMod::class.java)
        if (recordShakeState(mod.type)) {
            println("----- received ==> ${mod.callId}")
            onMessageReceived(msg)
        }
    }

    override fun closeSocket(case: String) {
        mSocket?.close(-111, "the socket will close by shutdown")
    }

    private fun onMessageReceived(msg: String?) {
        msg?.let {
            val size = it.toByteArray(Charset.forName("UTF-8")).size.toLong()
            postReceivedMessage(it, false, size)
        }
    }

    private fun getConnectionInfo(): String {
        val token = SPUtils_Proxy.getAccessToken("")
        //        FCApplication.logout("the socket connect error with null token")
        return "ws://106.75.100.103:8000/wand/$token"
    }

    private fun recordShakeState(type: String): Boolean {
        return when {
            type.equals(HEART_BEATS_PONG, true) -> { //pong
                curSocketState = SocketState.PONG
                false
            }
            else -> true
        }
    }

    private fun nextHeartbeats() {
        handler?.removeMessages(HEART_BEATS_EVENT)
        handler?.sendEmptyMessageDelayed(HEART_BEATS_EVENT, HEART_BEATS_TIME)
    }

    private fun sendHeartbeats() {
        if (curSocketState.isConnected()) {
            val heartbeats = BaseMod().apply {
                this.callId = CALL_ID_HEART_BEATS
                this.type = HEART_BEATS_PING
            }
            send(JSON.toJSONString(heartbeats), CALL_ID_HEART_BEATS, object : SendingCallBack {
                override fun result(isOK: Boolean, throwable: Throwable?) {
                    heartBeatsTime = if (isOK) HEART_BEATS_TIME else HEART_BEATS_TIME_SPECIAL
                    nextHeartbeats()
                }
            })
            curSocketState = SocketState.PING
        }
    }

    private var pingHasNotResponseCount = 0
    private var pongTime = 0L
    private var pingTime = 0L
    private var heartBeatsTime = HEART_BEATS_TIME

    private var curSocketState: SocketState = SocketState.INIT
        set(value) {
            when (value) {
                SocketState.PONG -> {
                    pingHasNotResponseCount = 0
                    pongTime = System.currentTimeMillis()
                }
                SocketState.PING -> {
                    if (field == SocketState.PING && !hasNetworkAccess()) {
                        checkNetWork()
                    }
                    val curTime = System.currentTimeMillis()
                    val outOfTime = heartBeatsTime * 3f
                    val lastPingTime = curTime - pingTime - heartBeatsTime
                    if (pingTime > 0 && pongTime <= 0) {
                        pingHasNotResponseCount++
                    }
                    if (pingHasNotResponseCount > 3 || (pongTime > 0L && curTime - (pongTime + lastPingTime) > outOfTime)) {
                        closeSocket(PING_TIMEOUT)
                    }
                    pingTime = System.currentTimeMillis()
                }
                SocketState.CONNECTED -> {
                    clearPingRecord()
                }
                SocketState.NETWORK_STATE_CHANGE, SocketState.CONNECTED_ERROR -> {
                    clearPingRecord()
                    connectDelay(RECONNECTION_TIME)
                }
                else -> {
                }
            }
            if (value != field) {
                field = value
                if (value.isValidState()) postConnectState(curSocketState)
                when (value) {
                    SocketState.PING -> print("on socket status change ----- ", "--- $value -- ${nio(pingTime)}")
                    SocketState.PONG -> print("on socket status change ----- ", "--- $value -- ${nio(pongTime)}")
                    SocketState.CONNECTED_ERROR -> print("on socket status change ----- ", "$value  ==> reconnection with error : ${value.case}")
                    else -> print("on socket status change ----- ", "--- $value --")
                }
            }
        }
        get() {
            synchronized(field) {
                return field
            }
        }

    private fun clearPingRecord() {
        pongTime = 0L
        pingTime = 0L
        pingHasNotResponseCount = 0
    }

    companion object {
        const val PING_TIMEOUT = "the socket would reconnection because the ping was no response too many times!"
        const val HEART_BEATS_EVENT = 0xf1365
        const val CONNECTION_EVENT = 0xf1378
        const val HEART_BEATS_TIME = 5000L
        const val RECONNECTION_TIME = 5000L
        const val CONNECTION_TIME_OUT = 3000
        const val HEART_BEATS_TIME_SPECIAL = 1000L
        const val CALL_ID_HEART_BEATS = "CUS-M61C-Q3TN-OX6Y"
        const val HEART_BEATS_PING = "ping"
        const val HEART_BEATS_PONG = "pong"
    }
}
