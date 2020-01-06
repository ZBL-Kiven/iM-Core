package com.zj.imcore.im.options

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class WebSocketClient @JvmOverloads constructor(private val impl: WebSocketImpl, uri: URI, header: Map<String, String>? = null) : WebSocketClient(uri, header) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        impl.onConnected(handshakedata)
    }

    override fun onClose(p0: Int, p1: String?, p2: Boolean) {
        impl.onClose(p0, p1, p2)
    }

    override fun onMessage(p0: String?) {
        impl.onMessage(p0)
    }

    override fun onError(p0: Exception?) {
        impl.onError(p0)
    }
}