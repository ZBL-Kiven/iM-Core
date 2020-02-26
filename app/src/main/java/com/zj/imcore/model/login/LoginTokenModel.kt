package com.zj.imcore.model.login

import java.io.Serializable

class LoginTokenModel : Serializable {
    var accessToken: String? = ""

    var refreshToken: String? = ""

    var expiresIn: Long = 0

    var tokenType: String? = ""
    var scope: Array<String>? = null
}