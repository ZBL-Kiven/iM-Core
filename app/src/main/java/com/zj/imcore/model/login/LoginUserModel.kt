package com.zj.imcore.model.login

class LoginUserModel {
    var id: Int = 0
    var name: String? = ""
    var inactive: Boolean = false
    var created: String? = ""
    var updated: String? = ""
    var profile: UserProfileModel? = null
}