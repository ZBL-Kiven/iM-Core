package com.zj.imcore.im.transfer

import com.alibaba.fastjson.JSON
import com.cf.im.db.domain.impl._MessageBeanImpl
import com.cf.im.db.repositorys.MessageRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.dispatcher.UIStore
import com.zj.imcore.base.FCApplication
import com.zj.imcore.enums.MsgSubtype
import com.zj.imcore.enums.MsgType
import com.zj.imcore.utils.unity.DateUtils
import com.zj.model.chat.MsgInfo
import com.zj.model.interfaces.MessageIn
import com.zj.model.mod.MessageBean
import kotlin.random.Random

object MsgInfoTransfer {

    fun transforMsg(d: JsonObject, callId: String?, sendingState: SendMsgState?, onFinish: () -> Unit) {
        val msg = Gson().fromJson(d.get("data").toString(), MessageBean::class.java)
        msg.callId = if (callId.isNullOrEmpty()) d.get("call_id").asString else callId
        msg.sendMsgState = sendingState?.type ?: 0
        msg.localCreateTs = System.currentTimeMillis()

        MessageRepository.insertOrUpdate(JSON.toJSONString(msg)) {
            val info = transform(it)
            UIStore.postData(info)
            onFinish()
        }
    }

    fun transform(beans: List<_MessageBeanImpl>): MutableList<MsgInfo> {
        return beans.mapTo(arrayListOf()) {
            transform(it)
        }
    }

    fun transform(bean: _MessageBeanImpl): MsgInfo {
        return MsgInfo(bean)
    }

    fun transform(data: MessageBean): MsgInfo {
        data.subtype = null
        data.subtypeDetail = null
        return MsgInfo(object : MessageIn {
            override fun channelId(): String? {
                return "" + data.dialog_id
            }

            override fun subType(): String? {
                if (data.subtype == null) data.subtype = when (Random.nextInt(5)) {
                    0 -> {
                        MsgType.STICKER.name
                    }
                    1 -> {
                        MsgType.FILE.name
                    }
                    2 -> {
                        MsgType.INFO.name
                    }
                    else -> {
                        MsgType.NORMAL.name
                    }
                }
                return data.subtype
            }

            override fun subTypeDetail(): String? {
                if (data.subtypeDetail == null) data.subtypeDetail = when (Random.nextInt(2)) {
                    0 -> {
                        MsgSubtype.IMAGE.name
                    }
                    else -> {
                        MsgSubtype.VIDEO.name
                    }
                }
                return data.subtypeDetail
            }

            override fun text(): String? {
                return data.text
            }

            override fun createdTs(): Long {
                return DateUtils.getDate("yyyy-MM-dd'T'HH-mm-ss", data.created)?.time ?: 0
            }

            override fun uid(): Long {
                return data.uid
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
                return if (data.localCreateTs <= 0) createdTs() else data.localCreateTs
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
                return if (FCApplication.isSelf(data.uid)) "self" else "other"
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
                return "http://img3.imgtn.bdimg.com/it/u=3079900250,2526713801&fm=26&gp=0.jpg"
            }

            override fun getVideoThumbWidth(): Int {
                return 1920
            }

            override fun getVideoThumbHeight(): Int {
                return 1080
            }

            override fun getVideoDuration(): Long {
                return 10000
            }

            override fun sendingState(): Int {
                return data.sendMsgState
            }

            override fun localFilePath(): String? {
                return ""
            }
        })
    }
}