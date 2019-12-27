package com.zj.im_model.mod

import com.zj.im.list.interfaces.InfoImpl
import com.zj.im_model.Payloads
import org.msgpack.annotation.Ignore

@Suppress("unused", "SpellCheckingInspection")
abstract class MsgInfo : InfoImpl<MsgInfo> {

    abstract fun vChannelId(): String?

    abstract fun subType(): String?

    abstract fun subTypeDetail(): String?

    abstract fun text(): String?

    abstract fun createdTs(): Long

    abstract fun uid(): String?

    abstract fun referKey(): String

    abstract fun starId(): String?

    abstract fun deleted(): Boolean

    abstract fun textColor(): String?

    abstract fun tsColor(): String?

    abstract fun bubbleColor(): String?

    abstract fun localCreatedTs(): Long

    abstract fun callId(): String?

    abstract fun key(): String

    abstract fun getAvatarUrl(): String?

    abstract fun getName(): String?

    abstract fun getStickerUrl(): String?

    abstract fun getImageUrl(): String?

    abstract fun getVoiceUrl():String?

    abstract fun getFileUrl(): String?

    abstract fun getVideoThumb(): String?

    //return curUserId == this.uid
    abstract fun isSelf(curUserId: String): Boolean

    /**----- packing ignore -----*/

    //send status
    abstract fun sendingState()

    //If the file is sent, the local path of the file needs to be saved for retransmission
    abstract fun localFilePath(): String?

    /** -------- db ignore properties ------- */

    @Ignore
    var timeLineString: String? = null

    /** -------- db ignore properties ------- */

    override fun compareTo(other: MsgInfo): Int {
        other.localCreatedTs().let { ot ->
            localCreatedTs().let {
                if (it > ot) return 1
                return if (it < ot) -1 else 0
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is MsgInfo) false else key() == other.key() || callId() == other.callId()
    }

    override fun hashCode(): Int {
        var c = key().hashCode()
        c = 31 * (c + callId().hashCode())
        return c
    }

    override fun getCacheName(payloads: String?): String {
        return when (payloads) {
            Payloads.AVATAR -> uid() ?: "default"
            Payloads.CONVERSATION -> "conversation"
            else -> ""
        }
    }

    override fun getOriginalPath(payloads: String?): String {
        return when (payloads) {
            Payloads.AVATAR -> getAvatarUrl() ?: "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=226862559,425820995&fm=26&gp=0.jpg"
            Payloads.CONVERSATION_STICKER -> getStickerUrl() ?: "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1028355655,2899485655&fm=26&gp=0.jpg"
            Payloads.CONVERSATION_IMAGE -> getImageUrl() ?: "http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg"
            Payloads.CONVERSATION_VIDEO -> getVideoThumb() ?: "http://img0.imgtn.bdimg.com/it/u=2458227883,4095122505&fm=26&gp=0.jpg"
            else -> ""
        }
    }
}
