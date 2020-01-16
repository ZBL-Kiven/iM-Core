@file:Suppress("unused")

package com.zj.imcore.base

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.cf.im.db.databases.AppDatabase
import com.cf.im.db.databases.DB
import com.zj.base.BaseApplication
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.im.log
import com.zj.im.mainHandler
import com.zj.imcore.BuildConfig
import com.zj.imcore.apis.user.UserApi
import com.zj.imcore.gui.SplashActivity
import java.lang.Exception

class FCApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        SPUtils_Proxy.init(BuildConfig.APPLICATION_ID, this)
        DB.singleton.get().init(this)
        AppDatabase.singleton.get();
        AppDatabase.singleton.get("userId")
    }

    companion object {

        fun logout(case: String?, done: ((isOK: Boolean) -> Unit)? = null) {
            if (SPUtils_Proxy.getAccessToken("") == null) {
                SPUtils_Proxy.clear()
                return
            }
            UserApi.logout { _, _, throwAble ->
                val isSuccess = when (throwAble?.response()?.code() ?: 0) {
                    in 200..299, 401 -> true
                    else -> false
                }
                if (isSuccess) {
                    SPUtils_Proxy.clear()
                } else {
                    log("${throwAble?.response()?.errorBody()?.string()}")
                }
                done?.invoke(isSuccess)
                if (isSuccess) {
                    mainHandler.postDelayed({
                        val ctx = application
                        clearAct()
                        val intent = Intent(ctx, SplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ctx.startActivity(intent)
                        case?.let {
                            Toast.makeText(application, it, Toast.LENGTH_SHORT).show()
                        }
                    }, 700)
                }
            }
        }

        fun isSelf(uid: String?): Boolean {
            return !uid.isNullOrEmpty() && uid == SPUtils_Proxy.getUserId("")
        }

        @SuppressLint("HardwareIds")
        fun getDeviceId(): String {
            return Settings.Secure.getString(
                application.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }

        fun showToast(s: String) {
            Toast.makeText(application, s, Toast.LENGTH_SHORT).show()
        }

        fun ping() {
            UserApi.ping { isOk, data, t ->
                println("a----- $isOk  $data    ${t?.message}")
            }
        }

        fun recordNewToken(expiresIn: Long) {
            try {
                val alarmManager =
                    application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val pendingIntentSet = PendingIntent.getBroadcast(
                    application,
                    0,
                    Intent("repeatAlarm"),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.set(AlarmManager.RTC_WAKEUP, expiresIn, pendingIntentSet)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun refreshToken() {
            val token = SPUtils_Proxy.getAccessToken("")
            val refresh = SPUtils_Proxy.getRefreshToken("")
            UserApi.refreshToken(token, refresh) { b, s, throwable ->

            }
        }

        fun getCacheDir(): String {
            return "${application.externalCacheDir?.absolutePath}/temp"
        }
    }
}