package com.zj.model.interfaces

import com.zj.model.chat.TeamMembers

@Suppress("unused")
interface DialogIn {

    fun inactive(): Boolean
    fun indexSymbol(): String
    fun role(): String
    fun department(): String
    fun email(): String
    fun phone(): String
    fun name(): String
    fun tmid(): String
    fun type(): String // p2p , group
    fun updated(): Long
    fun created(): Long
    fun title(): String
    fun hidden(): Boolean
    fun avatar(): String
    fun teamId(): String
    fun dialogId(): String
    fun gender(): String

    //--- group properties ---

    fun description(): String = "" //null,
    fun getPrivate(): String = "" //false,
    fun mode(): String = "" //"admin_off",
    fun topic(): String = "" //null,
    fun leavable(): String = "" //true,

    //--- missing field ---

    fun pin(): Boolean
    fun mute(): Boolean
    fun draft(): String?
    fun subDetail(): String?
    fun unReadCount(): Long


    fun getTeamMembers(lst: List<Map<String, Any>>): List<TeamMembers>

}