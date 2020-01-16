package com.zj.imcore.apis.fetcher

import com.cf.im.db.repositorys.DialogRepository
import com.cf.im.db.repositorys.MessageRepository
import com.zbl.api.BaseApi
import com.zbl.api.base.BaseRetrofit
import com.zj.im.dispatcher.UIStore
import com.zj.imcore.Constance
import com.zj.imcore.apis.ApiErrorHandler
import com.zj.imcore.im.transfer.MsgInfoTransfer
import retrofit2.HttpException

object FetcherApi {

    private fun get(): BaseApi<FetcherApiService> {
        return BaseApi.create<FetcherApiService>(ApiErrorHandler).baseUrl(Constance.getBaseUrl()).header(Constance.getHeader()).timeOut(20000).build()
    }

    fun syncDialogs(completed: (Boolean, HttpException?) -> Unit): BaseRetrofit.RequestCompo {
        return get().call({ it.fetchDialogs() }) { isSuccess: Boolean, data: String?, throwable: HttpException? ->
            if (isSuccess) {
                DialogRepository.insertOrUpdate(data) {
                    UIStore.postData(it)
                    completed.invoke(true, null)
                }
            } else {
                completed(false, throwable)
            }
        }
    }

    fun syncMessages(dialogId: String, messageId: String, limit: Int, isNewer: Boolean, completed: ((Boolean, HttpException?) -> Unit)? = null): BaseRetrofit.RequestCompo {
        val l: (isSuccess: Boolean, data: String?, throwable: HttpException?) -> Unit = { isSuccess, data, throwable ->
            if (isSuccess) {
                MessageRepository.insertOrUpdates(data) {
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