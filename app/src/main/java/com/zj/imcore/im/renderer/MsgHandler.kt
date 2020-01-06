package com.zj.imcore.im.renderer

import com.zj.im.store.interfaces.EventCallBack
import com.zj.imcore.base.FCApplication
import com.zj.imcore.utils.unity.DateUtils
import com.zj.model.chat.MsgInfo
import com.zj.model.interfaces.MessageIn
import com.zj.model.mod.MessageBean

class MsgHandler : EventCallBack<MessageBean, MsgInfo> {

    override fun handle(data: MessageBean, completed: (MsgInfo?) -> Unit) {
        val info = MsgInfo(object : MessageIn {
            override fun channelId(): String? {
                return "" + data.dialog_id
            }

            override fun subType(): String? {
                return data.subtype
            }

            override fun subTypeDetail(): String? {
                return data.subtype
            }

            override fun text(): String? {
                return data.text
            }

            override fun createdTs(): Long {
                return DateUtils.getDate("yyyy-MM-dd'T'HH-mm-ss", data.created)?.time ?: 0
            }

            override fun uid(): String? {
                return "${data.uid}"
            }

            override fun referKey(): String {
                return ""
            }

            override fun starId(): String? {
                return ""
            }

            override fun deleted(): Boolean {
                return data.deleted
            }

            override fun localCreatedTs(): Long {
                return if (data.localCreateTs <= 0) this.createdTs() else data.localCreateTs
            }

            override fun callId(): String? {
                return data.callId
            }

            override fun key(): String {
                return "${data.id}"
            }

            override fun getAvatarUrl(): String? {
                return "http://img2.imgtn.bdimg.com/it/u=2428248806,898201848&fm=26&gp=0.jpg"
            }

            override fun getName(): String? {
                return if (FCApplication.isSelf(data.uid.toString())) "self" else "other"
            }

            override fun getStickerUrl(): String? {
                return "http://n.sinaimg.cn/sinacn12/480/w240h240/20180430/d10b-fzvpatr4383984.gif"
            }

            override fun getStickerWidth(): Int {
                return 240
            }

            override fun getStickerHeight(): Int {
                return 240
            }

            override fun getImageUrl(): String? {
                return "http://img0.imgtn.bdimg.com/it/u=1548183809,1124291893&fm=11&gp=0.jpg"
            }

            override fun getImageWidth(): Int {
                return 1280
            }

            override fun getImageHeight(): Int {
                return 720
            }

            override fun getVoiceUrl(): String? {
                return ""
            }

            override fun getVoiceDuration(): Long {
                return 10000
            }

            override fun getFileUrl(): String? {
                return ""
            }

            override fun getFileSize(): Long {
                return 0
            }

            override fun getVideoUrl(): String? {
                return ""
            }

            override fun getVideoThumb(): String? {
                return ""
            }

            override fun getVideoThumbWidth(): Int {
                return 0
            }

            override fun getVideoThumbHeight(): Int {
                return 0
            }

            override fun getVideoDuration(): Long {
                return 0
            }

            override fun sendingState(): Int {
                return data.sendMsgState
            }

            override fun localFilePath(): String? {
                return ""
            }
        })
        completed(info)
    }

    override fun compare(a: MessageBean, b: MessageBean): Int {
        return when {
            a.id > b.id -> 1
            a.id < b.id -> -1
            else -> 0
        }
    }
}