package com.zj.imcore.mod

import com.google.gson.annotations.SerializedName
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.img.CacheAble
import com.zj.imcore.msgIsSelf
import com.zj.imcore.utils.img.ImageLoaderPayLoads
import com.zj.list.multiable.MultiAbleData
import org.msgpack.annotation.Ignore

import java.util.UUID

@Suppress("unused", "SpellCheckingInspection")
class MsgInfo : MultiAbleData<MsgInfo>, CacheAble {

    @SerializedName("vchannel_id")
    var vChannelId: String? = null

    @SerializedName("subtype")
    var subType: String? = null

    @SerializedName("subtype_detail")
    var subTypeDetail: String? = null

    @SerializedName("text")
    var text: String? = null

    @SerializedName("created_ts")
    var createdTs: Long = 0

    @SerializedName("uid")
    var uid: String? = null

    //发送消息的机器人 id
    @SerializedName("robot_id")
    var robotId: String? = null

    //消息所回复的那条消息的 key
    @SerializedName("refer_key")
    var referKey: String? = null

    @SerializedName("star_id")
    var starId: String? = null

    @SerializedName("file")
    var file: MsgFileInfo? = null

    //消息中的表情包
    @SerializedName("image")
    var image: MsgImageInfo? = null

    //消息中的语音
    @SerializedName("voice")
    var voice: MsgVoiceInfo? = null

    //消息是否被删除
    @SerializedName("deleted")
    var deleted: Boolean = false

    //消息颜色
    @SerializedName("text_color")
    var textcolor: String? = null

    //时间颜色
    @SerializedName("ts_color")
    var tscolor: String? = null

    //气泡颜色
    @SerializedName("bubble_color")
    var bubblecolor: String? = null

    @SerializedName("local_created_ts")
    var localCreatedTs: Long = 0

    //发送时带的参数，用于 reply 回执
    @SerializedName("call_id")
    var callId: String? = null     //+

    @SerializedName("key")
    var key: String = UUID.randomUUID().toString()


    /**----- packing ignore -----*/

    //发送状态
    @Ignore
    @SerializedName("sending_state")
    private var sendingState = "NONE"       // +

    //如果是发送的文件，需要保存文件的本地路径，用于重发
    @Ignore
    @SerializedName("local_file_path")
    var localFilePath: String? = ""

    /** -------- db ignore properties ------- */

    @Ignore
    var timeLineString: String? = null
    @Ignore
    var userAvatar: String? = null
    @Ignore
    var userNickname: String? = null

    /** -------- db ignore properties ------- */


    fun isSelf(): Boolean {
        return msgIsSelf(uid)
    }

    fun setSendState(state: SendMsgState) {
        sendingState = state.name
    }

    fun getSendState(): SendMsgState? {
        return SendMsgState.parseStateByType(sendingState)
    }

    override fun compareTo(other: MsgInfo?): Int {
        other?.localCreatedTs?.let { ot ->
            localCreatedTs.let {
                if (it > ot) return 1
                return if (it < ot) -1 else 0
            }
        }
        return -1
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is MsgInfo) false else key == other.key || callId == other.callId
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun getCacheName(payloads: String?): String {
        return when (payloads) {
            ImageLoaderPayLoads.AVATAR -> uid ?: "defalt"
            ImageLoaderPayLoads.CONVERSATION -> "conversation"
            else -> ""
        }
    }

    override fun getOriginalPath(payloads: String?): String {
        return when (payloads) {
            ImageLoaderPayLoads.AVATAR -> userAvatar ?: "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=226862559,425820995&fm=26&gp=0.jpg"
            ImageLoaderPayLoads.CONVERSATION -> image?.url ?: "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1061931856,2496159034&fm=26&gp=0.jpg"
            else -> ""
        }
    }

    private var storageFolderPath: String = ""
}
