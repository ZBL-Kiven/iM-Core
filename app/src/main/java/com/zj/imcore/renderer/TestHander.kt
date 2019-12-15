package com.zj.imcore.renderer

import com.zj.im.store.interfaces.EventCallBack
import com.zj.imcore.mod.MsgImageInfo
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.mod.MsgReceivedInfo
import com.zj.imcore.ui.enums.MsgType
import com.zj.imcore.userId
import com.zj.imcore.utils.img.ImageLoaderPayLoads
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
        val d = data?.msgInfo
        d?.localCreatedTs = data?.createTs ?: 0
        val r = Random()
//        if (r.nextInt(100) > 90) {
            d?.subType = MsgType.STICKER.name
            d?.text = "this is img type msg"
            d?.image = MsgImageInfo().apply {
                this.url = d?.getOriginalPath(ImageLoaderPayLoads.CONVERSATION)
                this.width = 1920
                this.height = 1200
                this.size = 776380
            }
//        } else {
//            d?.subType = MsgType.NORMAL.name
//        }
        d?.uid = if (data?.isSelf == true) userId else "bbb"
        completed(d)
    }
}
