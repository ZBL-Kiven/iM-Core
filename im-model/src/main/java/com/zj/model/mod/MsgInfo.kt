package com.zj.model.mod

import com.zj.im.list.interfaces.InfoImpl
import com.zj.model.Payloads
import com.zj.model.interfaces.MessageIn
import org.msgpack.annotation.Ignore

@Suppress("unused", "SpellCheckingInspection")
class MsgInfo(val impl: MessageIn) : InfoImpl<MsgInfo> {

    /** -------- db ignore properties ------- */

    @Ignore
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
