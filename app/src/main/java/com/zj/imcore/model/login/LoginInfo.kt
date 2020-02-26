package com.zj.imcore.model.login

import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.base.FCApplication
import com.zj.imcore.model.teams.TeamInfo
import java.io.Serializable

/**
 * Created by ZJJ on 19/12/23
 *
 * login mode
 *
 * */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class LoginInfo : Serializable {

    var user: LoginUserModel? = null
    var teams: List<TeamInfo>? = null
    var token: LoginTokenModel? = null
}