package com.zj.imcore.im.options

import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception

interface WebSocketImpl {

    fun onConnected(s: ServerHandshake?)

    fun onClose(errorCode: Int, case: String?, isFromRemote: Boolean)

    fun onMessage(msg: String?)

    fun onError(e: Exception?)

}