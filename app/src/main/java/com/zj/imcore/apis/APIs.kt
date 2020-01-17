package com.zj.imcore.apis

import com.zbl.api.BaseApi
import com.zbl.api.interceptor.HeaderProvider
import com.zbl.api.interceptor.UrlProvider
import com.zbl.api.interfaces.ApiFactory
import com.zj.imcore.Constance
import com.zj.imcore.apis.converter.FastJsonConverterFactory
import retrofit2.Converter

object APIs {

    inline fun <reified T : Any> getDefaultApi(baseUrl: UrlProvider = Constance.getBaseUrl(), header: HeaderProvider = Constance.getHeader(), timeOut: Long = 5000): BaseApi<T> {
        return BaseApi.create<T>(ApiErrorHandler).baseUrl(baseUrl).header(header).timeOut(timeOut).build(object : ApiFactory<T>() {
            override val getJsonConverter: Converter.Factory?
                get() = FastJsonConverterFactory.create()
        })
    }

    inline fun <reified T : Any> getDefaultApi(timeOut: Long = 5000): BaseApi<T> {
        val baseUrl: UrlProvider = Constance.getBaseUrl()
        val header: HeaderProvider = Constance.getHeader()
        return BaseApi.create<T>(ApiErrorHandler).baseUrl(baseUrl).header(header).timeOut(timeOut).build(object : ApiFactory<T>() {
            override val getJsonConverter: Converter.Factory?
                get() = FastJsonConverterFactory.create()
        })
    }
}