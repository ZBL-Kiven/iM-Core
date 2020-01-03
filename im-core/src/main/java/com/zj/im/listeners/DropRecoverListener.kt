package com.zj.im.listeners

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Created by ZJJ
 */
class DropRecoverListener private constructor(private val context: Application?, private var interrupting: (() -> Unit)) : Application.ActivityLifecycleCallbacks {

    companion object {
        fun init(context: Application?, interrupting: () -> Unit): DropRecoverListener {
            val dl = DropRecoverListener(context, interrupting)
            context?.registerActivityLifecycleCallbacks(dl)
            return dl
        }
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {
        interrupting.invoke()
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    fun destroy() {
        context?.unregisterActivityLifecycleCallbacks(this)
    }
}
