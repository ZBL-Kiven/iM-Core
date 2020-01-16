package com.zj.imcore.ui.chat.func

import com.zj.album.AlbumIns
import com.zj.album.options.AlbumOptions
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.ui.chat.ChatActivity

import com.zj.imcore.ui.chat.func.FuncGridView.*

class FuncEventListener(val context: ChatActivity) : FuncsAdapter.OnItemClickListener {

    private val sessionId = context.getSessionId()

    override fun onClick(id: Int) {
        when (id) {
            FUNC_ITEM_ID_PIC -> {
                AlbumIns.with(context).mimeTypes(AlbumOptions.ofAll()).simultaneousSelection(true).setOriginalPolymorphism(true).maxSelectedCount(9).imgSizeRange(1024, Long.MAX_VALUE).videoSizeRange(1024, Long.MAX_VALUE).start { isCancel, data ->
                    if (isCancel) {
                        context.resetEmotationBar()
                    } else {
                        data?.forEach {
                            if (it.isImage || it.isGif) {
                                IMHelper.sendImage(sessionId, it)
                            } else if (it.isVideo) {
                                IMHelper.sendVideo(sessionId, it)
                            }
                        }
                    }
                }
            }
            FUNC_ITEM_ID_TAKE_PIC -> {

            }
            FUNC_ITEM_ID_AUDIO -> {

            }
            FUNC_ITEM_ID_FILE -> {

            }
        }
    }
}
