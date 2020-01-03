package com.zj.im.chat.enums

enum class SocketState(internal var case: String = "") {

    INIT, CONNECTED, CONNECTION, CONNECTED_ERROR, NETWORK_STATE_CHANGE;

    fun isConnected(): Boolean {
        return this == CONNECTED
    }

    fun canConnect(): Boolean {
        return this == INIT || this == CONNECTED_ERROR || this == NETWORK_STATE_CHANGE
    }
}