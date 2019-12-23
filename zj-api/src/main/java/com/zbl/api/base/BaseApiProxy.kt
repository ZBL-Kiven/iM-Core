@file:Suppress("unused")

package com.zbl.api.base

import com.zbl.api.BaseApi
import com.zbl.api.interceptor.HeaderProvider
import com.zbl.api.interceptor.UrlProvider
import com.zbl.api.interfaces.ApiFactory
import com.zbl.api.interfaces.ErrorHandler
import java.io.InputStream

class BaseApiProxy<T : Any, ERROR_HANDLER : ErrorHandler>(private val clazz: Class<T>, private val handler: ERROR_HANDLER? = null) {

    private var timeOut: Long = 10000
    private var header: HeaderProvider? = null
    private var baseUrl: UrlProvider? = null
    private var certificate: Array<InputStream>? = null

    fun certificate(certificate: Array<InputStream>): BaseApiProxy<T, ERROR_HANDLER> {
        this.certificate = certificate
        return this
    }

    fun baseUrl(url: UrlProvider): BaseApiProxy<T, ERROR_HANDLER> {
        this.baseUrl = url
        return this
    }

    fun header(header: HeaderProvider): BaseApiProxy<T, ERROR_HANDLER> {
        this.header = header
        return this
    }

    fun timeOut(timeOut: Long): BaseApiProxy<T, ERROR_HANDLER> {
        this.timeOut = timeOut
        return this
    }

    fun build(): BaseApi<T> {
        return BaseApi(clazz, RetrofitFactory(timeOut, header, baseUrl, certificate, null), handler)
    }


    fun build(factory: ApiFactory<T>): BaseApi<T> {
        return BaseApi(clazz, RetrofitFactory(timeOut, header, baseUrl, certificate, factory), handler)
    }
}
