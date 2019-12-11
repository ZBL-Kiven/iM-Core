package com.zj.imcore.mod

import com.google.gson.annotations.SerializedName
import com.zj.im.utils.getIncrementKey

@Suppress("unused", "SpellCheckingInspection")
open class MsgVoiceInfo {
    @SerializedName("id")
    var id: String? = getIncrementKey()

    //语音的大小
    @SerializedName("size")
    var size: Int = 0

    //语音的时间长度
    @SerializedName("duration")
    var duration: Double = 0.toDouble()

    //语音的下载地址
    @SerializedName("url")
    var url: String? = null

    //语音是否已经被读过
    @SerializedName("read")
    var isRead: Boolean = false

}