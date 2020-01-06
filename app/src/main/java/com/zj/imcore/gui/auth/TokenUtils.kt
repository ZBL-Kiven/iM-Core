package com.zj.imcore.gui.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zj.imcore.base.FCApplication

class TokenRefreshUtils : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.fc.auth.refreshToken") {
            FCApplication.refreshToken()
        }
    }
}