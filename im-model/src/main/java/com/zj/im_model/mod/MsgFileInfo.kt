package com.zj.im_model.mod

import com.google.gson.annotations.SerializedName

@Suppress("unused", "SpellCheckingInspection")
open class MsgFileInfo {

    //消息文件的唯一标示
    @SerializedName("key")
    var key: String? = null

    //文件名
    @SerializedName("name")
    var name: String? = null

    //文件描述
    @SerializedName("description")
    var description: String? = null

    @SerializedName("team_id")
    var teamId: String? = null

    //文件所属群组
    @SerializedName("channel_id")
    var channelId: String? = null

    //文件所属成员
    @SerializedName("uid")
    var uid: String? = null

    //文件类型(未用到)
    @SerializedName("type")
    var type: String? = null

    //文件是否有效
    @SerializedName("inactive")
    var isInactive: Boolean = false

    //文件的分类
    //1. image
    //2. video
    @SerializedName("category")
    var category: String? = null

    //未用到
    @SerializedName("title")
    var title: String? = null

    //未用到
    @SerializedName("summary")
    var summary: String? = null

    //文件是否被删除
    @SerializedName("deleted")
    var isDeleted: Boolean = false

    //文件的宽度(图片类型时有值)
    @SerializedName("width")
    var width: Int = 0

    //文件的高度(图片类型时有值)
    @SerializedName("height")
    var height: Int = 0

    //文件的大小
    @SerializedName("size")
    var size: Long = 0

    //文件的方向(图片类型时有值)
    @SerializedName("orientation")
    var orientation: Int = 0

    //未用到
    @SerializedName("source")
    var source: String? = null

    //未用到
    @SerializedName("original")
    var isOriginal: Boolean = false

    //文件的 mime type
    @SerializedName("mime")
    var mime: String? = null

    //文件的下载 URL
    @SerializedName("url")
    var url: String? = null

    //文件的图片预览图 URL (视频文件用到)
    @SerializedName("image_url")
    var imageUrl: String? = null

    //文件是否公开(未用到)
    @SerializedName("is_public")
    var isPublic: Boolean = false

    //文件的评论数量(未用到)
    @SerializedName("comments_count")
    var commentsCount: Int = 0

    //文件的长度(视频文件用到)
    @SerializedName("duration")
    var duration: Long = 0L
}