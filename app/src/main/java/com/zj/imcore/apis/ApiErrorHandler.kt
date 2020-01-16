package com.zj.imcore.apis

import com.google.gson.Gson
import com.zbl.api.interfaces.ErrorHandler
import com.zj.im.log
import com.zj.imcore.base.FCApplication
import retrofit2.HttpException
import java.lang.Exception

object ApiErrorHandler : ErrorHandler {

    override fun onError(throwable: Throwable) {
        if (throwable is HttpException) {
            try {
                val errorInfo = throwable.response()?.body()?.toString()
                val e = Gson().fromJson(errorInfo, ErrorData::class.java)
                when (e.code) {
                    403->{
                        FCApplication.logout("Token was expired")
                    }

                }
            } catch (e: Exception) {
                log("onHttpError ----- case: ${e.message}")
            }
        }
    }
}

class ErrorData {
    val code: Int = 0
    val k: String = ""
    val v: String = ""
}
