package com.zj.imcore.apis.fetcher

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface FetcherApiService {

    @GET("/relation/v1/incremental_dialogs")
    fun fetchDialogs(@Query("since_ts") ts: Long): Observable<ResponseBody>

    @GET("/message/v1/teams/cityfruit/dialogs/{dialogId}/newer_messages")
    fun fetchNewerMsg(@Path("dialogId") dialogId: String, @Query("message_id") id: Long, @Query("limit") limit: Int): Observable<ResponseBody?>

    @GET("/message/v1/teams/cityfruit/dialogs/{dialogId}/older_messages")
    fun fetchOlderMsg(@Path("dialogId") dialogId: String, @Query("message_id") id: Long, @Query("limit") limit: Int): Observable<ResponseBody?>

}