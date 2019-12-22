package com.zj.base

import android.app.Application

open class BaseApplication : Application() {

    companion object {
        lateinit var application: Application

    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}