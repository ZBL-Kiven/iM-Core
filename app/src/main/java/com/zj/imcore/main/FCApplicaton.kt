@file:Suppress("unused")

package com.zj.imcore.main

import android.app.Activity
import android.content.Intent
import com.zj.base.BaseApplication
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.BuildConfig

class FCApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        SPUtils_Proxy.init(BuildConfig.APPLICATION_ID, this)
    }

    companion object {

        fun logout(act: Activity) {
            SPUtils_Proxy.clear()
            val intent = Intent(act, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            act.startActivity(intent)
        }

    }

}