package com.zj.imcore.apis.fetcher

import com.cf.im.db.repositorys.MessageRepository
import com.zbl.api.BaseApi
import com.zbl.api.base.BaseRetrofit
import com.zj.imcore.Constance
import com.zj.imcore.apis.ApiErrorHandler
import retrofit2.HttpException

object FetcherApi {

    private fun get(): BaseApi<FetcherApiService> {
        return BaseApi.create<FetcherApiService>(ApiErrorHandler).baseUrl(Constance.getBaseUrl()).header(Constance.getHeader()).timeOut(20000).build()
    }

    fun syncDialogs(completed: (Boolean,HttpException?) -> Unit) {

    }

    fun syncMessages(dialogId: String, messageId: String, limit: Int, isNewer: Boolean, completed: (Boolean,HttpException?) -> Unit): BaseRetrofit.RequestCompo {
        if (isNewer) {
            return get().call({ it.fetchNewerMsg(dialogId, messageId, limit) }) { isSuccess: Boolean, data: String?, throwable: HttpException? ->
                if (isSuccess) {
                    MessageRepository.insertOrUpdate(data){

                    }
                } else {
                    completed(false,throwable)
                }
            }
        } else {
            return get().call({ it.fetchOlderMsg(dialogId, messageId, limit) }) { isSuccess: Boolean, data: String?, throwable: HttpException? ->
                if (isSuccess) {

                } else {
                    completed(false,throwable)
                }
            }
        }
    }
}