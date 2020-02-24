package com.zj.imcore.apis.user


import com.cf.im.db.domain.DialogBean
import com.cf.im.db.domain.MemberBean
import com.zj.imcore.model.login.LoginInfo
import com.zj.imcore.model.sign.SignInfo
import io.reactivex.Observable
import retrofit2.http.*

interface UserApiService {

    @FormUrlEncoded
    @POST("/v1/login")
    fun login(
        @Field("identity") ac: String, @Field("password") pwd: String, @Field("app_id") appId: String, @Field(
            "device_id"
        ) deviceId: String
    ): Observable<LoginInfo>

    @POST("/v1/users")
    fun sign(@Body model: SignModel): Observable<SignInfo>

    @POST("/oauth/v1/logout")
    fun logout(): Observable<String>

    @GET("/oauth/hello")
    fun ping(): Observable<String>

    @GET("/oauth/v1/refresh")
    fun refresh(): Observable<String>

    //更新用户信息
    @PATCH("/relation/v1/users/{user}")
    fun update(@Path("user") user: String, @Body obj: Any): Observable<DialogBean>;
}