package com.zj.model.chat

import com.zj.ui.list.interfaces.InfoImpl
import com.zj.model.Payloads
import com.zj.model.interfaces.MessageIn

@Suppress("unused", "SpellCheckingInspection")
class MsgInfo(private val impl: MessageIn) : InfoImpl<MsgInfo> {

    val dialogId: String
        get() {
            return impl.dialogId()
        }

    val subType: String?; get() = impl.subType()

    val subTypeDetail: String?; get() = impl.subTypeDetail()

    val text: String?; get() = impl.text()

    val createdTs: Long; get() = impl.createdTs()

    val tmid: String; get() = impl.tmid()

    val referKey: String; get() = impl.referKey()

    val starId: String?; get() = impl.starId()

    val deleted: Boolean; get() = impl.deleted()

    val localCreatedTs: Long; get() = impl.localCreatedTs()

    val callId: String?; get() = impl.callId()

    val key: String; get() = impl.key() ?: ""

    val avatarUrl: String?; get() = impl.getAvatarUrl()

    val name: String?; get() = impl.getName()

    /**------- */

    val stickerUrl: String?; get() = impl.getStickerUrl()

    val stickerWidth: Int; get() = impl.getStickerWidth()

    val stickerHeight: Int; get() = impl.getStickerHeight()

    /**------- */

    val imageUrl: String?; get() = impl.getImageUrl()
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
    val localFilePath: String? = ""

    /** -------- db ignore properties ------- */

    /** -------- db ignore properties ------- */

    var timeLineString: String? = null

    /** -------- db ignore properties ------- */

    override fun compareTo(other: MsgInfo): Int {
        other.impl.localCreatedTs().let { ot ->
            impl.localCreatedTs().let {
                if (it > ot) return 1
                return if (it < ot) -1 else 0
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is MsgInfo) false else {
            val okey = other.impl.key()
            val oCallId = other.impl.callId()
            ((key.isNotEmpty()) && key == okey) || (!callId.isNullOrEmpty() && callId == oCallId)
        }
    }

    override fun hashCode(): Int {
        return impl.callId().hashCode()
    }

    override fun isDataEquals(t: MsgInfo): Boolean {
        return text == t.text && key == t.key && callId == t.callId && name == t.name && referKey == t.referKey && avatarUrl == t.avatarUrl && sendingState == t.sendingState && localFilePath == t.localFilePath
    }

    override fun getCacheName(payloads: String?): String {
        return impl.tmid()
    }

    override fun getOriginalPath(payloads: String?): String? {
        return when (payloads) {
            Payloads.MEMBERS_AVATAR -> avatarUrl
            Payloads.BUBBLE_STICKER -> stickerUrl
            Payloads.BUBBLE_IMAGE -> imageUrl
            Payloads.BUBBLE_VIDEO -> videoThumb
            else -> ""
        }
    }
}
