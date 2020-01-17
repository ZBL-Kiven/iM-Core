package com.zj.imcore.apis.members

import com.zbl.api.BaseApi
import com.zbl.api.base.BaseRetrofit
import com.zj.imcore.Constance
import com.zj.imcore.apis.ApiErrorHandler
import okhttp3.ResponseBody
import retrofit2.HttpException

object MemberApi {

    private fun get(): BaseApi<MemberApiService> {
        return BaseApi.create<MemberApiService>(ApiErrorHandler).baseUrl(Constance.getBaseUrl()).header(Constance.getHeader()).build()
    }

    fun fetchMembers(since: Long, result: (isSuccess: Boolean, data: ResponseBody?, throwable: HttpException?) -> Unit): BaseRetrofit.RequestCompo {
       return get().call({ it.fetchMembersBySince(since) }, result)
    }

}
