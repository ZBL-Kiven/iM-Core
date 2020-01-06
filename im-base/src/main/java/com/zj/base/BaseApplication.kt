package com.zj.base

import android.app.Activity
import android.app.Application

@Suppress("unused")
open class BaseApplication : Application() {

    companion object {
        lateinit var application: Application

        fun <T : Activity> getCurAct(): T? {
            @Suppress("UNCHECKED_CAST") return (application as? BaseApplication?)?.curAct as? T
        }

        fun getAct(): Activity? {
            return (application as? BaseApplication?)?.curAct
        }

        fun clearAct() {
            val acts = (application as? BaseApplication)?.activities
            acts?.forEach {
                it.finish()
                acts.remove(it)
            }
        }
    }

    private var activities: LinkedHashSet<Activity>? = null

    private var curAct: Activity? = null

    override fun onCreate() {
        super.onCreate()
        application = this
        this.registerActivityLifecycleCallbacks(object : ActLifecycleImpl() {
            override fun onActivityResumed(activity: Activity) {
                curAct = activity
                activities?.add(activity)
            }

            override fun onActivityStopped(activity: Activity) {
                if (curAct == activity) curAct = null
                activities?.remove(activity)
            }
        })
    }
}