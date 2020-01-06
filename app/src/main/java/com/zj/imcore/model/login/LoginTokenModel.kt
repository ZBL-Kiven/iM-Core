package com.zj.imcore.model.login

import com.google.gson.annotations.SerializedName

class LoginTokenModel {
    @SerializedName("access_token")
    var accessToken: String? = ""
    @SerializedName("refresh_token")
    var refreshToken: String? = ""
    @SerializedName("expires_in")
    var expiresIn: Long = 0
    @SerializedName("token_type")
    var tokenType: String? = ""
    var scope: Array<String>? = null
}