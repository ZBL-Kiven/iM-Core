package com.zj.imcore.model.login

import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.base.FCApplication
import com.zj.imcore.model.teams.TeamInfo

/**
 * Created by ZJJ on 19/12/23
 *
 * login mode
 *
 * */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class LoginInfo {

    var user: LoginUserModel? = null
    var teams: List<TeamInfo>? = null
    var token: LoginTokenModel? = null

    fun saveAsSP() {
        val expires = token?.expiresIn ?: 24 * 60 * 60 * 1000L
        SPUtils_Proxy.setUserId(user?.id ?: 0)
        SPUtils_Proxy.setAccessToken(token?.accessToken)
        SPUtils_Proxy.setRefreshToken(token?.refreshToken)
        SPUtils_Proxy.setUserName(user?.name)
        SPUtils_Proxy.setUserGender(user?.gender)
        SPUtils_Proxy.setUserEmail(user?.email)
        SPUtils_Proxy.setUserTel(user?.phone)
        SPUtils_Proxy.setUserNote(user?.note)
        SPUtils_Proxy.setUserAvatar(user?.avatar)
        SPUtils_Proxy.setUserAddress(user?.profile?.address)
        SPUtils_Proxy.setExpiresIn(expires)
        FCApplication.recordNewToken(expires)
    }
}