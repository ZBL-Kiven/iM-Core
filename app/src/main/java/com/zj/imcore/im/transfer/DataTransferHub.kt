package com.zj.imcore.im.transfer

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zj.im.UIHelper
import com.zj.im.chat.enums.SendMsgState
import com.zj.imcore.base.FCApplication
import com.zj.imcore.enums.MsgSubtype
import com.zj.imcore.enums.MsgType
import com.zj.imcore.utils.unity.DateUtils
import com.zj.model.chat.DialogInfo
import com.zj.model.chat.MsgInfo
import com.zj.model.chat.ProgressInfo
import com.zj.model.interfaces.DialogIn
import com.zj.model.interfaces.MessageIn
import com.zj.model.mod.MessageBean
import kotlin.random.Random

object DataTransferHub {

    //todo 临时拆分
    fun onSocketDataReceived(data: String?, callId: String?, sendingState: SendMsgState?, onFinish: () -> Unit) {
        val d = Gson().fromJson(data, JsonObject::class.java)
        val msg = Gson().fromJson(d.get("data").toString(), MessageBean::class.java)
        mCacheMsgs.firstOrNull { it == msg }?.let {
            msg.callId = it.callId
            msg.localCreateTs = System.currentTimeMillis()
        } ?: {
            msg.callId = if (callId.isNullOrEmpty()) d.get("call_id").asString else callId
            msg.sendMsgState = sendingState?.type ?: 0
            msg.localCreateTs = System.currentTimeMillis()
        }.invoke()
        mCacheMsgs.add(msg)
        UIHelper.postReceiveData(getMockMsgs(msg))
        onFinish()
    }

    fun onSendingProgressChanged(process: Int, callId: String) {
        UIHelper.postReceiveData(ProgressInfo(callId, process))
    }

    fun queryDialogInDb() {
        val dialogs = arrayListOf<DialogInfo>().apply {
            (0 until 10).forEach { i ->
                add(DialogInfo(object : DialogIn {
                    override fun getId(): String {
                        return "8589934605"
                    }

                    override fun getTitle(): String {
                        return "test - $i"
                    }

                    override fun getSubDetail(): String {
                        return "asdasdasd"
                    }

                    override fun getLatestTs(): Long {
                        return i.toLong()
                    }

                    override fun getSelfReadTs(): Long {
                        return 0
                    }

                    override fun getUnReadCount(): Int {
                        return i
                    }

                    override fun getOtherReadTs(): Long {
                        return 1
                    }

                    override fun getUserId(): String? {
                        return "2"
                    }

                    override fun hasStar(): Boolean {
                        return false
                    }

                    override fun getDraft(): String? {
                        return "this is example draft text"
                    }

                    override fun isShown(): Boolean {
                        return true
                    }

                    override fun sortTs(): Long {
                        return i * 1L
                    }

                    override fun notification(): Boolean {
                        return true
                    }

                    override fun hideTs(): Long {
                        return 0
                    }

                    override fun getThumbUrl(): String {
                        return "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3835973537,1602418292&fm=26&gp=0.jpg"
                    }

                    override fun isPin(): Boolean {
                        return false
                    }

                    override fun isMute(): Boolean {
                        return false
                    }

                    override fun isDelete(): Boolean {
                        return false
                    }
                }))
            }
        }
        UIHelper.postReceiveData(dialogs)
    }

    fun queryMsgInDb(uid: String, dialogId: String) {
        UIHelper.postReceiveData(mCacheMsgs)
    }

    private fun getMockMsgs(data: MessageBean): MsgInfo {

        val isMock = false

        if (isMock) {
            data.subtype = null
            data.subtypeDetail = null
        }
        val info = MsgInfo(object : MessageIn {
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
        return info
    }

    val mCacheMsgs = mutableListOf<MessageBean>()

}