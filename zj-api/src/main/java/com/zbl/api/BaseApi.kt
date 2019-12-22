@file:Suppress("unused", "UNUSED_PARAMETER")

package com.zbl.api

import com.zbl.api.base.BaseApiProxy
import com.zbl.api.base.BaseRetrofit
import com.zbl.api.base.RetrofitFactory
import com.zbl.api.interfaces.ErrorHandler
import com.zbl.api.utils.TypeUtils
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type

class BaseApi<T : Any>(cls: Class<T>, factory: RetrofitFactory<T>?, private val errorHandler: ErrorHandler? = null) : BaseRetrofit<T>(cls, factory) {

    companion object {

        @JvmStatic
        inline fun <reified T : Any> create(): BaseApiProxy<T, *> {
            return BaseApiProxy<T, Nothing>(T::class.java)
        }

        @Suppress("unused")
        @JvmStatic
        inline fun <reified T : Any, reified ERROR_HANDLER : ErrorHandler> createE(): BaseApiProxy<T, ERROR_HANDLER> {
            return BaseApiProxy(T::class.java, ERROR_HANDLER::class.java.newInstance())
        }

        @JvmStatic
        fun <T : Any> create(cls: Class<T>): BaseApiProxy<T, *> {
            return BaseApiProxy<T, Nothing>(cls)
        }

        @JvmStatic
        fun <T : Any, ERROR_HANDLER : ErrorHandler> create(cls: Class<T>, errorHandler: Class<ERROR_HANDLER>): BaseApiProxy<T, ERROR_HANDLER> {
            return BaseApiProxy(cls)
        }
    }

    fun <R> zip(observer: (T) -> Array<Observable<*>>, onFunc: (Any, Any, Any) -> R, subscribe: ((isSuccess: Boolean, data: R, throwable: Throwable?) -> Unit)? = null) {

        val observables = observer.invoke(getService())
        val obsMap = mutableMapOf<String, Observable<*>>()
        observables.forEach {
            val t: Type = TypeUtils.getFirstClassType(it::class.java)
            val typeName = t.toString()
            obsMap[typeName] = it
        }

    }

    fun <F> request(observer: (T) -> Observable<F>, subscribeSchedulers: Scheduler = Schedulers.io(), observableSchedulers: Scheduler = AndroidSchedulers.mainThread(), subscribe: ((isSuccess: Boolean, data: F?, throwable: Throwable?) -> Unit)? = null) {
        RequestInCompo(observer(getService()), subscribeSchedulers, observableSchedulers, { data ->
            subscribe?.invoke(true, data, null)
        }, { throwable ->
            throwable?.let { e ->
                errorHandler?.onError(e);subscribe?.invoke(false, null, e)
            }
        }).init()
    }

    fun <F> call(observer: (T) -> Observable<F>, subscribeSchedulers: Scheduler = Schedulers.io(), observableSchedulers: Scheduler = AndroidSchedulers.mainThread(), subscribe: ((isSuccess: Boolean, data: F?, throwable: Throwable?) -> Unit)? = null): RequestCompo {
        val requestInCompo: RequestInCompo<F>?
        requestInCompo = RequestInCompo(observer(getService()), subscribeSchedulers, observableSchedulers, { data ->
            subscribe?.invoke(true, data, null)
        }, { throwable ->
            throwable?.let { e ->
                errorHandler?.onError(e);subscribe?.invoke(false, null, e)
            }
        })
        requestInCompo.init()
        return object : RequestCompo {
            override fun cancel() {
                requestInCompo.cancel()
            }
        }
    }
}