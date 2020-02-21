package com.zj.imcore.gui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.R
import com.zj.imcore.gui.login.LoginActivity
import com.zj.imcore.ui.main.MainActivity
import com.zj.imcore.ui.main.contact.group.GroupInfoActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_splash_activity_content)
        initData()
    }

    private fun initData() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkIsLogin()
        }, 300)
    }

    private fun checkIsLogin() {
        if (SPUtils_Proxy.getAccessToken("").isNullOrEmpty()) {
            this.startActivity(Intent(this, LoginActivity::class.java))
        } else {
            this.startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}