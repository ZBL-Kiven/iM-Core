package com.zj.imcore.apis.members

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface MemberApiService {

    @Suppress("SpellCheckingInspection")
    @GET("/relation/v1/teams/cityfruit/incremental_members")
    fun fetchMembersBySince(@Query("since_ts") sinceTs: Long): Observable<ResponseBody>
}