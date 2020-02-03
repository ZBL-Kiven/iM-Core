package com.zj.imcore.apis

import com.alibaba.fastjson.JSON
import com.zbl.api.interfaces.ErrorHandler
import com.zj.im.log
import com.zj.imcore.R
import com.zj.imcore.base.FCApplication
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException

object ApiErrorHandler : ErrorHandler {

    override fun onError(throwable: Throwable) {
        if (throwable is HttpException) {
            try {
                val errorInfo = throwable.response()?.body()?.toString()
                val e = JSON.parseObject(errorInfo, ErrorData::class.java)
                if (throwable.response()?.code() == 403 || e.code == 403) {
                    FCApplication.logout("Token was expired")
                }
            } catch (e: Exception) {
                log("onHttpError ----- case: ${e.message}")
            }
        } else {
            if (throwable is UnknownHostException) {
                FCApplication.showToast(R.string.app_net_network_error)
            } else {
                log("onHttpError ----- case: ${throwable.message}")
                throw UnknownError(throwable.message)
            }
        }
    }
}

class ErrorData {
    val code: Int = 0
    val k: String = ""
    val v: String = ""
}
