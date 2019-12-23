@file:Suppress("unused")

package com.zj.imcore.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.zj.base.BaseApplication
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.BuildConfig
import com.zj.imcore.apis.user.UserApi
import com.zj.imcore.gui.LoginActivity

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

        @SuppressLint("HardwareIds")
        fun getDeviceId(): String {
            return Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun showToast(s: String) {
            Toast.makeText(application, s, Toast.LENGTH_SHORT).show()
        }

        fun ping() {
            UserApi.ping { isOk, data, t ->
                println("a----- $isOk  $data    ${t?.message}")
            }
        }
    }
}