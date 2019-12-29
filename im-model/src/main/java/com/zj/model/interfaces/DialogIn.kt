package com.zj.model.interfaces

@Suppress("unused")
interface DialogIn {

    //unique id for conversation
    fun getId(): String

    fun getTitle(): String

    fun getSubDetail(): String

    //the conversation last updated Ts
    fun getLatestTs(): Long

    fun getSelfReadTs(): Long

    fun getUnReadCount(): Int

    //last time someone read the conversation
    fun getOtherReadTs(): Long

    //user id of the peer during p2p conversation
    fun getUserId(): String?

    //are there favorite messages in the conversation
    fun hasStar(): Boolean

    //对话的草稿
    fun getDraft(): String?

    //对话是否显示
    fun shown(): Boolean

    fun sortTs(): Long

    fun notification(): String

    fun hide(): String

    fun hideTs(): Long

    fun getThumbUrl(): String

    fun isPin(): Boolean

    fun isMute(): Boolean

    fun isDelete(): Boolean
}