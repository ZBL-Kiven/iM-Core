package com.zj.imcore.model.teams

import com.zj.imcore.model.login.UserProfileModel

class TeamMembers {
    var inactive: Boolean = false //false,
    var role: String? = "" //"normal",
    var department: String? = "" //null,
    var email: String? = "" //"xing.li@cityfruit.io",
    var phone: String? = "" //"13521930955",
    var updated: String? = "" //"2019-12-25T12:19:50Z",
    var uid: Long = 0L //2,
    var name: String? = "" //"w",
    var type: String? = "" //"normal",
    var created: String? = "" //"2019-12-25T12:19:50Z",
    var title: String? = "" //null,
    var hidden: Boolean = false //false,
    var id: Long = 0L //2,
    var avatar: String? = "" //null,
    var team_id: String? = "" //2,
    var gender: String? = "" //"male",
    var profile: UserProfileModel? = null
    var tmid: String? = null
}