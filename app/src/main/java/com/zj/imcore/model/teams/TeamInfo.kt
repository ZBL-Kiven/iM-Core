package com.zj.imcore.model.teams

import java.io.Serializable

class TeamInfo : Serializable {

    var inactive: Boolean = false //false,
    var description: String? = "" //"卓越智视科技有限公司",
    var updated: String? = "" //"2020-01-15T08:02:18Z",
    var name: String? = "" //"卓越智视",
    var created: String? = "" //"2020-01-15T08:02:18Z",
    var subdomain: String? = "" //"toptensor",
    var id: String? = "" //2,
    var email_domain: String? = "" //"toptensor.io"
    var member: TeamMembers? = null
}