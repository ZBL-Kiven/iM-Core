package com.zj.imcore.ui.model

import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.ui.enums.MsgType
import com.zj.imcore.ui.model.sub.*
import java.lang.NullPointerException

object ModHub {

    fun getMode(msgInfo: MsgInfo): BaseItemMod {
        msgInfo.subType?.let {
            return when {
                MsgType.INFO.eq(it) -> InfoMod()
                MsgType.STICKER.eq(it) -> ImageMod()
                MsgType.FILE.eq(it) -> FileMod()
                MsgType.VOICE.eq(it) -> VoiceMod()
                MsgType.NORMAL.eq(it) -> NormalMod()
                else -> throw TypeCastException("the message type $it is not supported!")
            }
        } ?: throw NullPointerException("please check the message ${msgInfo.key} type was null?")
    }

    const val REFRESH_AVATAR = "refreshAvatar"
    const val REFRESH_BUBBLE = "refreshBubble"
    const val REFRESH_SENDING_STATE = "refreshSendingState"
    const val REFRESH_SENDING_PROGRESS = "refreshProgress"
    const val REFRESH_NICKNAME = "refreshNickname"
    const val REFRESH_TIMELINE = "refreshTimeline"
}