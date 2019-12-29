package com.zj.imcore.ui.list.model

import com.zj.imcore.enums.MsgSubtype
import com.zj.model.mod.MsgInfo
import com.zj.imcore.enums.MsgType
import com.zj.imcore.ui.list.model.sub.*
import java.lang.NullPointerException

/**
 * Created by ZJJ on 19/12/12
 *
 * return the view model which the want
 * */
@Suppress("unused")
object ModHub {

    fun getMode(msgInfo: MsgInfo?): BaseItemMod {
        msgInfo?.impl?.subType()?.let {
            return when {
                MsgType.INFO.eq(it) -> InfoMod()
                MsgType.STICKER.eq(it) -> StickerMod()
                MsgType.VOICE.eq(it) -> VoiceMod()
                MsgType.NORMAL.eq(it) -> NormalMod()
                MsgType.FILE.eq(it) -> {
                    msgInfo.impl.subTypeDetail()?.let { sub ->
                        when {
                            MsgSubtype.FILE.eq(sub) -> FileMod()
                            MsgSubtype.IMAGE.eq(sub) -> ImageMod()
                            MsgSubtype.VIDEO.eq(sub) -> VideoMod()
                            else -> throw TypeCastException("the message type $it is not supported!")
                        }
                    } ?: throw NullPointerException("please check the message ${msgInfo.impl.key()} sub-type-detail was null?")
                }
                else -> throw TypeCastException("the message type $it is not supported!")
            }
        } ?: throw NullPointerException("please check the message ${msgInfo?.impl?.key()} sub-type was null?")
    }

    const val REFRESH_AVATAR = "refreshAvatar"
    const val REFRESH_BUBBLE = "refreshBubble"
    const val REFRESH_SENDING_STATE = "refreshSendingState"
    const val REFRESH_SENDING_PROGRESS = "refreshProgress"
    const val REFRESH_NICKNAME = "refreshNickname"
    const val REFRESH_TIMELINE = "refreshTimeline"
}