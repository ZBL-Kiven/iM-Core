package com.zj.imcore.model.login

class LoginTokenModel {
    var accessToken: String? = ""

    var refreshToken: String? = ""

    var expiresIn: Long = 0

    var tokenType: String? = ""
    var scope: Array<String>? = null
}