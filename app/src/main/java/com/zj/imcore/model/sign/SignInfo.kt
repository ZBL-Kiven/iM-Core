package com.zj.imcore.model.sign

import com.zj.imcore.model.login.UserProfileModel

@Suppress("unused")
class SignInfo{
    var id: Int = 0 //9,
    var name: String? = "" //"jj.z",
    var inactive: Boolean = false //false,
    var created: String? = "" //"2019-12-23T03:22:44Z",
    var updated: String? = "" //"2019-12-23T03:22:44Z"
    var profile: UserProfileModel? = null
}