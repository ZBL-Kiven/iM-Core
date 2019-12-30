package com.zj.imcore.ui.users

import android.content.Context
import android.content.Intent
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity

class UserInfoActivity : FCActivity() {

    companion object {

        private const val UID = "uid"

        fun start(context: Context?, id: String) {
            context?.startActivity(Intent(context, UserInfoActivity::class.java).apply {
                this.putExtra(UID, id)
            })
        }
    }

    override fun getContentId(): Int {
        return R.layout.app_act_user_info_content
    }

    override fun initListener() {

    }

    override fun initData() {

    }

    override fun initView() {
        setTitle("UserInfomation")
    }

}