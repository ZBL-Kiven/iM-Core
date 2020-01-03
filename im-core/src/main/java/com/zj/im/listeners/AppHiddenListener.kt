package com.zj.im.listeners

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.ComponentCallbacks2.*
import android.content.res.Configuration

/**
 * Created by ZJJ
 */

class AppHiddenListener private constructor(private val appHiddenListener: (() -> Unit)) : ComponentCallbacks2 {

    companion object {
        fun init(context: Application?, appHiddenListener: () -> Unit): AppHiddenListener {
            val al = AppHiddenListener(appHiddenListener)
            context?.registerComponentCallbacks(al)
            return al
        }
    }

    override fun onLowMemory() {
        System.gc()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {

    }

    override fun onTrimMemory(level: Int) {
        if (level == TRIM_MEMORY_UI_HIDDEN) appHiddenListener.invoke()
    }

    fun shutDown(context: Application?) {
        context?.unregisterComponentCallbacks(this)
    }
}