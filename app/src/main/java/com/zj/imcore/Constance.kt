package com.zj.imcore

import com.zbl.api.interceptor.HeaderProvider
import com.zbl.api.interceptor.UrlProvider
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.base.FCApplication


object Constance {

    fun getBaseUrl(): UrlProvider {
        return object : UrlProvider() {
            override fun url(): String {
                return Environment.getEnv().url
            }
        }
    }

    fun getHeader(): HeaderProvider {
        return object : HeaderProvider {
            override fun headers(): Map<String, String> {
                return hashMapOf<String, String>().apply {
                    this["Content-Type"] = "application/json"
                    this["Authorization"] = "Bearer ${SPUtils_Proxy.getAccessToken("")}"
                }
            }
        }
    }

    fun getAppId(): String {
        return Environment.getEnv().appId
    }

    fun getDeviceId(): String {
        return FCApplication.getDeviceId()
    }

    const val REG_CODE_COVERSATION_FRAGMENT_DIALOG = 11121
    const val REG_CODE_CHAT_ACTIVITY_MESSAGE = 11101
    const val REG_CODE_FRAGMENT_CONTACT = 11203
}