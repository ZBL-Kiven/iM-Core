package com.zbl.api.base

import com.zbl.api.BuildConfig
import com.zbl.api.interceptor.HeaderProvider
import com.zbl.api.interceptor.HttpLoggingInterceptor
import com.zbl.api.interceptor.Interceptor
import com.zbl.api.interceptor.UrlProvider
import com.zbl.api.utils.SSLParams
import com.zbl.api.utils.TrustAllCerts
import com.zbl.api.utils.TrustAllHostnameVerifier
import com.zbl.api.utils.getSslSocketFactory
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

class BaseHttpClient(private val header: HeaderProvider? = null, private val url: UrlProvider?) {

    fun getHttpClient(timeout: Long, certificate: Array<InputStream>? = null): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS)
        builder.readTimeout(timeout * 2, TimeUnit.MILLISECONDS)
        builder.writeTimeout(timeout, TimeUnit.MILLISECONDS)
        builder.addInterceptor(Interceptor(header, url))
        builder.buildSSLSocketFactory(certificate)
        val sslTrustManager = TrustAllHostnameVerifier()
        builder.hostnameVerifier(sslTrustManager)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        }
        return builder.build()
    }

    private fun OkHttpClient.Builder.buildSSLSocketFactory(certificate: Array<InputStream>? = null): OkHttpClient.Builder {
        if (!certificate.isNullOrEmpty()) {
            val sslParams: SSLParams? = getSslSocketFactory(certificate, null, null)
            val sslSocketFactory = sslParams?.sSLSocketFactory
            val trustManager = sslParams?.trustManager
            if (sslSocketFactory != null && trustManager != null)
                return this.sslSocketFactory(sslSocketFactory, trustManager)

        }
        val sslFactory = TrustAllCerts.createSSLSocketFactory() ?: return this
        return this.sslSocketFactory(sslFactory, TrustAllCerts)
    }
}