package com.zj.imcore.model.login

class LoginUserModel {
    var id: Int = 0
    var name: String? = ""
    var inactive: Boolean = false
    var email: String? = ""
    var avatar: String? = ""
    var gender: String? = ""
    var created: String? = ""
    var updated: String? = ""
    var note: String? = ""

    var profile: UserProfileModel? = null

}