package com.zj.imcore.model.login

import java.io.Serializable

class LoginUserModel : Serializable {
    var id: String = ""
    var name: String? = ""
    var inactive: Boolean = false
    var phone: String? = ""
    var email: String? = ""
    var avatar: String? = ""
    var gender: String? = ""
    var created: String? = ""
    var updated: String? = ""
    var note: String? = ""

    var profile: UserProfileModel? = null

}