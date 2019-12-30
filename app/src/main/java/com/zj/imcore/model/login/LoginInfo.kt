package com.zj.imcore.model.login

import com.zj.base.utils.storage.sp.SPUtils_Proxy

/**
 * Created by ZJJ on 19/12/23
 *
 * login mode
 *
 * */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class LoginInfo {

    var user: LoginUserModel? = null
    var token: LoginTokenModel? = null

    fun saveAsSP() {
        SPUtils_Proxy.setUserId(user?.id?.toString())
        SPUtils_Proxy.setAccessToken(token?.accessToken)
        SPUtils_Proxy.setRefreshToken(token?.refreshToken)
        SPUtils_Proxy.setUserName(user?.name)
        SPUtils_Proxy.setUserGender(user?.gender)
        SPUtils_Proxy.setUserEmail(user?.email)
        SPUtils_Proxy.setUserTel(user?.phone)
        SPUtils_Proxy.setUserNote(user?.note)
        SPUtils_Proxy.setUserAvatar(user?.avatar)
        SPUtils_Proxy.setUserAddress(user?.profile?.address)
    }
}