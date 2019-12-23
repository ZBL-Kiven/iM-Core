package com.zj.imcore.model.member.contact

import com.google.gson.annotations.SerializedName

class ContactMemberInfo {

    class IncrementalMemberModel {
        var nextTs: Int = 0
        var members: List<MemberModel>? = null
    }

    class MemberModel {
        var inactive: Boolean = false
        var role: String? = ""
        var updated: String? = ""
        var uid: Int = 0
        var name: String? = ""
        var type: String? = ""
        var created: String? = ""
        @SerializedName("index_symbol")
        var indexSymbol: String = "a"
        var hidden: Boolean = false
        var teamId: Int = 0
        var profile: MemberProfileModel? = null

        override fun equals(other: Any?): Boolean {
            return if (other !is MemberModel) false else {
                other.uid == uid
            }
        }

        override fun hashCode(): Int {
            return uid.hashCode()
        }
    }

    class MemberProfileModel {
        var avatar: String = ""
        var email: String? = ""
        var gender: String? = ""
        var title: String? = ""
    }
}