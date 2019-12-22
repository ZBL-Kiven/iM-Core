package com.zj.imcore.gui

import android.content.Intent
import android.widget.ImageView
import com.zj.base.BaseActivity
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.R
import com.zj.imcore.main.LoginActivity
import com.zj.imcore.main.MainActivity

class SplashActivity : BaseActivity() {

    override fun setTitle(): String? {
        return null
    }

    override fun getContentId(): Int {
        return R.layout.app_splash_activity_content
    }

    override fun initView() {
        find<ImageView>(R.id.app_splash_iv_content).setImageResource(R.mipmap.app_splash_main)
    }

    override fun initData() {
        checkIsLogin()
    }

    override fun initListener() {

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