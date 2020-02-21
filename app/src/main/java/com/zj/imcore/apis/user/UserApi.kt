package com.zj.imcore.apis.user

import com.cf.im.db.domain.DialogBean
import com.cf.im.db.domain.MemberBean
import com.zbl.api.BaseApi
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.Constance
import com.zj.imcore.Constance.getBaseUrl
import com.zj.imcore.Constance.getHeader
import com.zj.imcore.apis.APIs
import com.zj.imcore.apis.ApiErrorHandler
import com.zj.imcore.getGender
import com.zj.imcore.model.login.LoginInfo
import com.zj.imcore.model.login.UserProfileModel
import com.zj.imcore.model.sign.SignInfo
import retrofit2.HttpException
import java.util.*

object UserApi {

    private fun get(): BaseApi<UserApiService> {
        return APIs.getDefaultApi()
    }

    fun login(ac: String, pwd: String, result: (Boolean, LoginInfo?, throwAble: HttpException?) -> Unit) {
        val appId = Constance.getAppId()
        val deviceId = Constance.getDeviceId()
        get().request({ it.login(ac, pwd, appId, deviceId) }, result)
    }

    fun sign(ac: String, pwd: String, tel: String, email: String, genderIsLady: Boolean, result: (Boolean, SignInfo?, throwAble: HttpException?) -> Unit) {
        val model = SignModel(ac, pwd, tel, email, getGender(genderIsLady), UserProfileModel())
        get().call({ it.sign(model) }, result)
    }

    fun logout(result: (Boolean, String?, throwAble: HttpException?) -> Unit) {
        get().call({ it.logout() }, result)
    }

    fun refreshToken(token: String, refresh: String, isOk: (Boolean, String?, Throwable?) -> Unit) {
        get().call({ it.ping() }, isOk)
    }

    fun ping(isOk: (Boolean, String?, Throwable?) -> Unit) {
        get().call({ it.ping() }, isOk)
    }

    /////杨吉 更新用户信息
    fun updateUser(request: Map<String, Any>, result: (Boolean, DialogBean?, throwAble: HttpException?) -> Unit) {
        val name = SPUtils_Proxy.getUserName("");
        get().call({ it.update(name, request) }, result)
    }

}

data class SignModel(val name: String, val password: String, val tel: String, val email: String, val gender: String, val profile: UserProfileModel)