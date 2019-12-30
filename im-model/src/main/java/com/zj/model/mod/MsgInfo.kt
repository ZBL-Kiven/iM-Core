package com.zj.model.mod

import com.zj.im.list.interfaces.InfoImpl
import com.zj.model.Payloads
import com.zj.model.interfaces.MessageIn

@Suppress("unused", "SpellCheckingInspection")
class MsgInfo(private val impl: MessageIn) : InfoImpl<MsgInfo> {

    val channelId: String?; get() = impl.channelId()

    val subType: String?; get() = impl.channelId()

    val subTypeDetail: String?; get() = impl.channelId()

    val text: String?; get() = impl.channelId()

    val createdTs: Long; get() = impl.createdTs()

    val uid: String?; get() = impl.uid()

    val referKey: String; get() = impl.referKey()

    val starId: String?; get() = impl.starId()

    val deleted: Boolean; get() = impl.deleted()

    val textColor: String?; get() = impl.textColor()

    val tsColor: String?; get() = impl.tsColor()

    val bubbleColor: String?; get() = impl.bubbleColor()

    val localCreatedTs: Long; get() = impl.localCreatedTs()

    val callId: String?; get() = impl.callId()

    val key: String; get() = impl.key()

    val avatarUrl: String?; get() = impl.getAvatarUrl()

    val name: String?; get() = impl.getName()

    /**------- */

    val stickerUrl: String?; get() = impl.getStickerUrl()

    val stickerWidth: Int; get() = impl.getStickerWidth()

    val stickerHeight: Int; get() = impl.getStickerHeight()

    /**------- */

    val imageUrl: String?; get() = impl.channelId()

    val imageWidth: Int; get() = impl.getImageWidth()
    val imageHeight: Int; get() = impl.getImageHeight()

    /**------- */

    val voiceUrl: String?; get() = impl.getVoiceUrl()

    val voiceDuration: Long; get() = impl.getVoiceDuration()

    /**------- */

    val fileUrl: String?; get() = impl.getFileUrl()

    val fileSize: Long; get() = impl.getFileSize()

    /**------- */

    val videoUrl: String?; get() = impl.getVideoUrl()

    val videoThumb: String?; get() = impl.getVideoThumb()

    val videoThumbWidth: Int; get() = impl.getVideoThumbWidth()

    val videoThumbHeight: Int; get() = impl.getVideoThumbHeight()

    val videoDuration: Long; get() = impl.getVideoDuration()

    /**----- packing ignore -----*/

    //send status
    val sendingState: Int; get() = impl.sendingState()

    //If the file is sent, the local path of the file needs to be saved for retransmission
    val localFilePath: String?; get() = impl.channelId()

    /** -------- db ignore properties ------- */

    /** -------- db ignore properties ------- */

    var timeLineString: String? = null

    /** -------- db ignore properties ------- */

    //return curUserId == this.uid
    fun isSelf(curUserId: String): Boolean {
        return impl.uid() == curUserId
    }

    override fun compareTo(other: MsgInfo): Int {
        other.impl.localCreatedTs().let { ot ->
            impl.localCreatedTs().let {
                if (it > ot) return 1
                return if (it < ot) -1 else 0
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is MsgInfo) false else impl.key() == other.impl.key() || impl.callId() == other.impl.callId()
    }

    override fun hashCode(): Int {
        var c = impl.key().hashCode()
        c = 31 * (c + impl.callId().hashCode())
        return c
    }

    override fun getCacheName(payloads: String?): String {
        return impl.uid() ?: "default"
    }

    override fun getOriginalPath(payloads: String?): String? {
        return when (payloads) {
            Payloads.MEMBERS_AVATAR -> impl.getAvatarUrl()
            Payloads.BUBBLE_STICKER -> impl.getStickerUrl()
            Payloads.BUBBLE_IMAGE -> impl.getImageUrl()
            Payloads.BUBBLE_VIDEO -> impl.getVideoThumb()
            else -> ""
        }
    }
}
