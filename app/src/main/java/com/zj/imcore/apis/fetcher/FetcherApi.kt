package com.zj.imcore.apis.fetcher

import com.cf.im.db.repositorys.DialogRepository
import com.cf.im.db.repositorys.MessageRepository
import com.zbl.api.BaseApi
import com.zbl.api.base.BaseRetrofit
import com.zj.ui.dispatcher.UIStore
import com.zj.imcore.apis.APIs.getDefaultApi
import com.zj.imcore.im.transfer.DialogTransfer
import com.zj.imcore.im.transfer.MsgInfoTransfer
import okhttp3.ResponseBody
import retrofit2.HttpException

object FetcherApi {

    private fun get(): BaseApi<FetcherApiService> {
        return getDefaultApi(20000)
    }

    fun syncDialogs(ts: Long, completed: (Boolean, HttpException?) -> Unit): BaseRetrofit.RequestCompo {
        return get().call({ it.fetchDialogs(ts) }) { isSuccess: Boolean, data: ResponseBody?, throwable: HttpException? ->
            if (isSuccess) {
                val d = data?.string()
                DialogRepository.insertOrUpdates(d) {
                    UIStore.postData(DialogTransfer.transform(it))
                    completed.invoke(true, null)
                }
            } else {
                completed(false, throwable)
            }
        }
    }

    fun syncMessages(dialogId: String, messageId: Long, limit: Int, isNewer: Boolean, completed: ((Boolean, HttpException?) -> Unit)? = null): BaseRetrofit.RequestCompo {
        val l: (isSuccess: Boolean, data: ResponseBody?, throwable: HttpException?) -> Unit = { isSuccess, data, throwable ->
            if (isSuccess) {
                val d = data?.string()
                MessageRepository.insertOrUpdates(d) {
                    UIStore.postData(MsgInfoTransfer.transform(it))
                    completed?.invoke(true, null)
                }
            } else {
                completed?.invoke(false, throwable)
            }
        }
        return get().call({ if (isNewer) it.fetchNewerMsg(dialogId, messageId, limit) else it.fetchOlderMsg(dialogId, messageId, limit) }, l)
    }
}