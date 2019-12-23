package com.zbl.api.base

import com.zbl.api.interceptor.HeaderProvider
import com.zbl.api.interceptor.UrlProvider
import com.zbl.api.interfaces.ApiFactory
import com.zbl.api.retrofit.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream

class RetrofitFactory<T> internal constructor(private val timeout: Long, private val header: HeaderProvider? = null, private val urlProvider: UrlProvider?, private val certificate: Array<InputStream>? = null, private val factory: ApiFactory<T>? = null) {

    private val getOkHttpClient: OkHttpClient; get() = factory?.getOkHttpClient ?: BaseHttpClient(header, urlProvider).getHttpClient(timeout, certificate)

    private val getJsonConverter: Converter.Factory; get() = factory?.getJsonConverter ?: GsonConverterFactory.create()

    private val getCallAdapterFactory: CallAdapter.Factory; get() = factory?.getCallAdapterFactory ?: RxJava2CallAdapterFactory.create()

    private val mRetrofit: Retrofit; get() = factory?.mRetrofit ?: initRetrofit()

    internal fun createService(cls: Class<T>): T {
        return factory?.createService(mRetrofit, cls) ?: mRetrofit.create(cls)
    }

    private fun initRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
        retrofit.baseUrl(urlProvider?.url() ?: "http://127.0.0.1")
        retrofit.client(getOkHttpClient)
        retrofit.addConverterFactory(getJsonConverter)
        retrofit.addCallAdapterFactory(getCallAdapterFactory)
        return retrofit.build()
    }
}
