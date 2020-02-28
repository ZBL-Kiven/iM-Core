package com.zj.model.chat

import com.zj.list.multiable.MultiAbleData
import com.zj.model.interfaces.DialogIn

@Suppress("unused")
class DialogInfo(private val impl: DialogIn) : MultiAbleData<DialogInfo> {

    val inactive: Boolean; get() = impl.inactive() //false,
    val indexSymbol: String?; get() = impl.indexSymbol() //false,
    val role: String; get() = impl.role() //"owner",
    val department: String; get() = impl.department() //null,
    val email: String; get() = impl.email() //"xing.li@cityfruit.io",
    val type: String; get() = impl.type()
    val phone: String; get() = impl.phone() //"13521930955",
    val updated: Long; get() = impl.updated() //"2019-12-26T03:11:34Z",
    val name: String; get() = impl.name() //"xing.li",
    val tmid: String; get() = impl.tmid()
    val created: Long; get() = impl.created() //"2019-12-26T03:11:34Z",
    val title: String; get() = impl.title() //null,
    val hidden: Boolean; get() = impl.hidden() //false,
    val avatar: String; get() = impl.avatar() //null,
    val teamId: String; get() = impl.teamId() //1,
    val dialogId: String; get() = impl.dialogId() //4294967298,
    val gender: String; get() = impl.gender() //"male",

    /** Missing field */
    val pin: Boolean; get() = impl.pin() //false
    val mute: Boolean; get() = impl.mute() //false
    val draft: String?; get() = impl.draft()
    val subDetail: String?; get() = impl.subDetail()
    val unReadCount: Long; get() = impl.unReadCount()

    /** group properties */
    val description: String; get() = impl.description() //null,
    val mode: String; get() = impl.mode() //"admin_off",
    val topic: String; get() = impl.topic() //null,
    val leavable: Boolean; get() = impl.leavable() //true,

    /** extends */
    private val members: List<Map<String, Any>>? = null
    val profile: Map<String, Any>? = null

    fun getTeamMembers(): List<TeamMembers>? {
        return members?.let {
            impl.getTeamMembers(it)
        }
    }

    override fun compareTo(other: DialogInfo): Int {
        val isPin = impl.pin()
        val otherIsPin = other.impl.pin()
        if (isPin && !otherIsPin) {
            return 1
        } else if (!isPin && otherIsPin) {
            return -1
        }
        other.impl.updated().let { ot ->
            impl.updated().let {
                if (it > ot) return 1
                return if (it < ot) -1 else 0
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is DialogInfo) false else dialogId == other.dialogId
    }

    override fun hashCode(): Int {
        return dialogId.hashCode()
    }

    override fun isDataEquals(t: DialogInfo): Boolean {
        return inactive ==t.inactive
                && indexSymbol == t.indexSymbol
                && title ==t.title
                &&avatar==t.avatar
                &&teamId ==t.teamId
                &&draft ==t.draft
                &&description ==t.description
    }
}