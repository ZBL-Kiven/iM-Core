package com.zj.base

import android.app.Activity
import android.app.Application
import java.util.concurrent.ConcurrentLinkedQueue

@Suppress("unused")
open class BaseApplication : Application() {

    private var activities: ConcurrentLinkedQueue<Activity> = ConcurrentLinkedQueue()

    private var curAct: Activity? = null

    companion object {
        lateinit var application: Application

        fun <T : Activity> getCurAct(): T? {
            @Suppress("UNCHECKED_CAST") return (application as? BaseApplication?)?.curAct as? T
        }

        fun getAct(): Activity? {
            return (application as? BaseApplication?)?.curAct
        }

        fun clearAct() {
            val acts = (application as? BaseApplication)?.activities ?: return
            repeat((0..acts.size).count()) {
                acts.poll()?.finish()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        this.registerActivityLifecycleCallbacks(object : ActLifecycleImpl() {
            override fun onActivityResumed(activity: Activity) {
                curAct = activity
                activities.add(activity)
            }

            override fun onActivityStopped(activity: Activity) {
                if (curAct == activity) curAct = null
                activities.remove(activity)
            }
        })
    }
}