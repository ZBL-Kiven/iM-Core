package com.zj.imcore.im.obtain

import com.cf.im.db.repositorys.MessageRepository
import com.zj.ui.dispatcher.UIStore
import com.zj.imcore.apis.fetcher.FetcherApi
import com.zj.imcore.im.transfer.MsgInfoTransfer
import com.zj.model.chat.MsgInfo
import retrofit2.HttpException

object MessageObtainUtils {

    fun fetchNewerMessage(firstMsg: MsgInfo, limit: Int, isNewer: Boolean, observer: (b: Boolean, e: HttpException?) -> Unit) {
        MessageRepository.queryMessageBy(firstMsg.dialogId, firstMsg.callId, firstMsg.key, limit, isNewer) {
            if (!it.isNullOrEmpty()) {
                val lst = MsgInfoTransfer.transform(it)
                UIStore.postData(lst)
                observer.invoke(true, null)
            } else {
                fetchNewerMessageFormServer(firstMsg, limit, isNewer, observer)
            }
        }
    }

    private fun fetchNewerMessageFormServer(firstMsg: MsgInfo, limit: Int, isNewer: Boolean, observer: (b: Boolean, e: HttpException?) -> Unit) {
        FetcherApi.syncMessages(firstMsg.dialogId, firstMsg.key, limit, isNewer, observer)
    }
}