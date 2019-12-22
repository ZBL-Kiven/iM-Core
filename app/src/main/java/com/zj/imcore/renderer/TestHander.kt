package com.zj.imcore.renderer

import com.zj.im_model.mod.*
import com.zj.im.store.interfaces.EventCallBack
import com.zj.im_model.Payloads
import com.zj.imcore.enums.MsgSubtype
import com.zj.imcore.enums.MsgType
import com.zj.imcore.userId
import java.util.*

class TestHandler : EventCallBack<MsgReceivedInfo, MsgInfo> {

    override fun compare(a: MsgReceivedInfo, b: MsgReceivedInfo): Int {
        return when {
            a.createTs > b.createTs -> 1
            a.createTs < b.createTs -> -1
            else -> 0
        }
    }

    override fun handle(data: MsgReceivedInfo?, completed: (MsgInfo?) -> Unit) {
        Thread.sleep(80)
        val d = data?.msgInfo
        d?.localCreatedTs = data?.createTs ?: 0
        val r = Random()
        when (r.nextInt(6)) {
            1 -> {
                d?.subType = MsgType.STICKER.toString()
                d?.text = "this is img type msg"
                d?.image = MsgImageInfo().apply {
                    this.url = d?.getOriginalPath(Payloads.CONVERSATION_STICKER)
                    this.width = 400
                    this.height = 400
                    this.size = 776380
                }
            }
            2 -> {
                d?.subType = MsgType.VOICE.toString()
                d?.voice = MsgVoiceInfo()
                d?.voice?.url = ""
            }
            3 -> {
                d?.subType = MsgType.INFO.toString()
                d?.text = "this is an info type msg"
            }
            4 -> {
                d?.subType = MsgType.NORMAL.toString()
            }
            5 -> {
                d?.subType = MsgType.FILE.toString()
                d?.subTypeDetail = MsgSubtype.VIDEO.toString()
                d?.file = MsgFileInfo().apply {
                    this.imageUrl = d?.getOriginalPath(Payloads.CONVERSATION_VIDEO)
                    this.width = 1024
                    this.height = 775
                    this.size = 776380
                }
            }
            else -> {
                d?.subType = MsgType.FILE.toString()
                d?.subTypeDetail = MsgSubtype.IMAGE.toString()
                d?.file = MsgFileInfo().apply {
                    this.imageUrl = d?.getOriginalPath(Payloads.CONVERSATION_IMAGE)
                    this.width = 670
                    this.height = 471
                    this.size = 776380
                }
            }
        }
        d?.uid = if (data?.isSelf == true) userId else "bbb"
        completed(d)
    }
}
