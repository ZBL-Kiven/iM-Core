package com.zj.im_model.mod

import com.google.gson.annotations.SerializedName
import com.zj.im.utils.getIncrementKey

@Suppress("unused", "SpellCheckingInspection")
open class MsgImageInfo {
    @SerializedName("id")
    var id: String? = getIncrementKey()

    //图片的宽度
    @SerializedName("width")
    var width: Int = 0

    //图片的高度
    @SerializedName("height")
    var height: Int = 0

    //图片的下载地址
    @SerializedName("url")
    var url: String? = null

    //图片的大小
    @SerializedName("size")
    var size: Long = 0

    var duration: Double = 0.0
}