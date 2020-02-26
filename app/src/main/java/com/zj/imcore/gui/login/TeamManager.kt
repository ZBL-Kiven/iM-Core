package com.zj.imcore.gui.login

import com.alibaba.fastjson.JSON
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.base.FCApplication
import com.zj.imcore.model.login.LoginInfo
import com.zj.imcore.model.teams.TeamInfo
import com.zj.imcore.model.teams.TeamMembers

object TeamManager {

    private var info: LoginInfo? = null
        get() {
            if (field == null) {
                val s = SPUtils_Proxy.getLoginInfo("-")
                val obj = JSON.parseObject(s, LoginInfo::class.java)
                if (obj == null) {
                    getCachedInfoError()
                } else {
                    field = obj
                }
            }
            return field
        }

    fun saveAsSp(info: LoginInfo) {
        val infoStr = JSON.toJSONString(info)
        SPUtils_Proxy.setLoginInfo(infoStr)
    }

    fun getCurrentTeamId(): String? {
        return SPUtils_Proxy.getCurTeamId("")
    }

    fun getTeamUser(): TeamMembers? {
        return getCurrentTeam()?.teamMember
    }

    fun getTmId(): String {
        return getTeamUser()?.tmid?:""
    }

    private fun getCurrentTeam(): TeamInfo? {
        val tmId = getCurrentTeamId()
        if (tmId.isNullOrEmpty()) {
            FCApplication.selectionTeams()
            return null
        }
        var curTeam: TeamInfo? = null
        info?.teams?.forEach {
            if (it.id == tmId) {
                curTeam = it
                return@forEach
            }
        } ?: getCachedInfoError()
        return curTeam
    }

    fun changeCurTeam(id: String) {
        SPUtils_Proxy.setCurTeamId(id)
    }

    private fun getCachedInfoError() {
        FCApplication.logout("getCachedUserIsNull")
    }

    fun getTeams(): List<TeamInfo> {
        return info?.teams ?: arrayListOf()
    }
}