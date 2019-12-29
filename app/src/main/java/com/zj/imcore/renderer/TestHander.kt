package com.zj.imcore.renderer

import com.zj.model.mod.*
import com.zj.im.store.interfaces.EventCallBack
import com.zj.model.interfaces.MessageIn

class TestHandler : EventCallBack<MessageIn, MsgInfo> {

    override fun compare(a: MessageIn, b: MessageIn): Int {
        return when {
            a.localCreatedTs() > b.localCreatedTs() -> 1
            a.localCreatedTs() < b.localCreatedTs() -> -1
            else -> 0
        }
    }

    override fun handle(data: MessageIn, completed: (MsgInfo?) -> Unit) {
        val d = MsgInfo(data)
        completed(d)
    }


//    override fun getOriginalPath(payloads: String?): String {
//        return when (payloads) {
//            Payloads.AVATAR -> impl.getAvatarUrl() ?: "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=226862559,425820995&fm=26&gp=0.jpg"
//            Payloads.CONVERSATION_STICKER -> impl.getStickerUrl() ?: "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1028355655,2899485655&fm=26&gp=0.jpg"
//            Payloads.CONVERSATION_IMAGE -> impl.getImageUrl() ?: "http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg"
//            Payloads.CONVERSATION_VIDEO -> impl.getVideoThumb() ?: "http://img0.imgtn.bdimg.com/it/u=2458227883,4095122505&fm=26&gp=0.jpg"
//            else -> ""
//        }
//    }

//    d?.localCreatedTs = data?.createTs ?: 0
//    val r = Random()
//    when (r.nextInt(6)) {
//        1 -> {
//            d?.subType = MsgType.STICKER.toString()
//            d?.text = "this is img type msg"
//            d?.image = MsgImageInfo().apply {
//                this.url = d?.getOriginalPath(Payloads.CONVERSATION_STICKER)
//                this.width = 400
//                this.height = 400
//                this.size = 776380
//            }
//        }
//        2 -> {
//            d?.subType = MsgType.VOICE.toString()
//            d?.voice = MsgVoiceInfo()
//            d?.voice?.url = ""
//        }
//        3 -> {
//            d?.subType = MsgType.INFO.toString()
//            d?.text = "this is an info type msg"
//        }
//        4 -> {
//            d?.subType = MsgType.NORMAL.toString()
//        }
//        5 -> {
//            d?.subType = MsgType.FILE.toString()
//            d?.subTypeDetail = MsgSubtype.VIDEO.toString()
//            d?.file = MsgFileInfo().apply {
//                this.imageUrl = d?.getOriginalPath(Payloads.CONVERSATION_VIDEO)
//                this.width = 1024
//                this.height = 775
//                this.size = 776380
//            }
//        }
//        else -> {
//            d?.subType = MsgType.FILE.toString()
//            d?.subTypeDetail = MsgSubtype.IMAGE.toString()
//            d?.file = MsgFileInfo().apply {
//                this.imageUrl = d?.getOriginalPath(Payloads.CONVERSATION_IMAGE)
//                this.width = 670
//                this.height = 471
//                this.size = 776380
//            }
//        }
//    }
//    d?.uid = if (data?.isSelf == true) userId else "bbb"
}
