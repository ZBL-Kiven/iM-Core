package com.zj.im_model.mod

import com.google.gson.annotations.SerializedName
import java.util.*

@Suppress("unused", "SpellCheckingInspection")
open class MsgVoiceInfo {
    @SerializedName("id")
    var id: String? = UUID.randomUUID().toString()

    //语音的大小
    @SerializedName("size")
    var size: Int = 0

    //语音的时间长度
    @SerializedName("duration")
    var duration: Long = 1892887

    //语音的下载地址
    @SerializedName("url")
    var url: String? = null

    //语音是否已经被读过
    @SerializedName("read")
    var isRead: Boolean = false

}