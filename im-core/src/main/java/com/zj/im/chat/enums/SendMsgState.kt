package com.zj.im.chat.enums

/**
 * created by ZJJ
 *
 * the msg sending state
 * */

@Suppress("unused")
enum class SendMsgState(val type: Int) {
    NONE(1), SUCCESS(3), FAIL(-1), SENDING(0), TIME_OUT(-2), ON_SEND_BEFORE_END(2);

    companion object {
        fun parseStateByType(type: Int?): SendMsgState? {
            var state: SendMsgState? = null
            values().forEach {
                if (it.type == type) {
                    state = it
                    return@forEach
                }
            }
            return state
        }
    }
}