package com.zj.imcore.apis.members

import com.zbl.api.BaseApi
import com.zj.imcore.Constance
import com.zj.imcore.model.member.contact.ContactMemberInfo
import retrofit2.HttpException

object MemberApi {

    private fun get(): BaseApi<MemberApiService> {
        return BaseApi.create<MemberApiService>().baseUrl(Constance.getBaseUrl()).header(Constance.getHeader()).build()
    }

    fun fetchMembers(since: Long, result: (isSuccess: Boolean, data: ContactMemberInfo.IncrementalMemberModel?, throwable: HttpException?) -> Unit) {
        get().request({ it.fetchMembersBySince(since) }, result)
    }

}
