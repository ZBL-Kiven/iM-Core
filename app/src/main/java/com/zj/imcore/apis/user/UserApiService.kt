package com.zj.imcore.apis.user


import com.zj.imcore.model.login.LoginInfo
import com.zj.imcore.model.sign.SignInfo
import io.reactivex.Observable
import retrofit2.http.*

interface UserApiService {

    @FormUrlEncoded
    @POST("/v1/login")
    fun login(@Field("identity") ac: String, @Field("password") pwd: String, @Field("app_id") appId: String, @Field("device_id") deviceId: String): Observable<LoginInfo>

    @POST("/v1/users")
    fun sign(@Body model: SignModel): Observable<SignInfo>

    @GET("/oauth/hello")
    fun ping():Observable<String>
}