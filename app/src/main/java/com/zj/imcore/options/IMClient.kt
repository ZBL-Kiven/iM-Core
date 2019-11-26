package com.zj.imcore.options

import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.modle.AuthBuilder
import com.zj.im.chat.modle.HeartbeatsBuilder
import com.zj.im.chat.modle.SocketConnInfo
import java.util.*

class IMClient : ClientHub() {

    override fun getConnInfo(conn: (Boolean, SocketConnInfo?) -> Unit) {
        conn(true, SocketConnInfo("sp-api.i-mocca.com", 5222, 10000))
    }

    override val heartbeatsBuilder: HeartbeatsBuilder?
        get() = HeartbeatsBuilder(hashMapOf<String, Any>().apply {
            put(CALL_TYPE, HEART_BEATS_PING)
            put(CALL_ID, CALL_ID_HEART_BEATS)
        }, HEART_BEATS_TIME) {
            val type = it?.get("type")?.toString()
            return@HeartbeatsBuilder type == HEART_BEATS_PING || type == HEART_BEATS_PONG
        }

    override val authBuilder: AuthBuilder?
        get() = AuthBuilder(hashMapOf<String, Any>().apply {
            put(CALL_TYPE, AUTH)
            put("key", KEY)
            put(DEVICE_TYPE[0], DEVICE_TYPE[1])
            put(CALL_ID, CALL_ID_AUTH)
        }, AUTH_TIMEOUT) {
            val type = it?.get("type")?.toString()
            if (type == REPLY) {
                if (it["call_id"] == CALL_ID_AUTH) {
                    if (it["status"]?.toString().equals("ok", true)) return@AuthBuilder AuthBuilder.AuthStatus.SUCCESS else return@AuthBuilder AuthBuilder.AuthStatus.FAIL
                }
            };null
        }

    companion object {
        const val CALL_ID = "call_id"
        const val CALL_TYPE = "type"
        const val CALL_ID_AUTH = "CUS-089C-PDQ3-EWC7"
        const val CALL_ID_HEART_BEATS = "CUS-M61C-Q3TN-OX6Y"

        const val KEY = "tcp:uid-68354"

        internal val DEVICE_TYPE = arrayOf("device", "Android")

        internal const val TCP_TIME_OUT = 3000L

        internal const val AUTH_TIMEOUT = 10000L

        internal const val HEART_BEATS_TIME = 3000

        const val REPLY = "reply"
        const val AUTH = "auth"
        const val HEART_BEATS_PING = "ping"
        const val HEART_BEATS_PONG = "pong"

        private var CALL_ID_HEAD = "android-"

        fun getRandomCallId(): String {
            return "$CALL_ID_HEAD-${UUID.randomUUID()}"
        }
    }
}