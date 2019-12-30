package com.zj.im.chat.interfaces

import com.zj.im.chat.enums.SendMsgState

/**
 * Created by ZJJ
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class AnalyzingData<T> {

    val state: SendMsgState?
    val callId: String?

    private val type: Type
    private val param: T?
    private val response: T?

    fun getData(): T? {
        return if (isSelf()) param else response
    }

    fun isSelf(): Boolean {
        return type != Type.SOURCE_RECEIVE
    }

    fun isRecent(): Boolean {
        return type == Type.SOURCE_RESEND
    }

    /**
     * inner params
     * */

    internal enum class Type {

        SOURCE_SEND, SOURCE_RECEIVE, SOURCE_RESEND

    }

    /**
     * the message by send
     * */
    internal constructor(state: SendMsgState?, callId: String?, param: T?, isResend: Boolean) {
        this.state = state
        this.callId = callId
        this.param = param
        this.response = null
        this.type = if (isResend) Type.SOURCE_RESEND else Type.SOURCE_SEND
    }

    /**
     * the message form server
     * */
    internal constructor(response: T?) {
        this.state = SendMsgState.SUCCESS
        this.callId = ""
        this.param = null
        this.response = response
        this.type = Type.SOURCE_RECEIVE
    }
}