package com.zj.imcore.ui.users

import android.content.Context
import android.content.Intent
import com.zj.imcore.base.FCActivity

class UserInfoActivity : FCActivity() {

    companion object {

        const val UID = "uid"

        fun start(context: Context?, id: Int) {
            context?.startActivity(Intent(context,UserInfoActivity::class.java).apply {
                this.putExtra(UID,id)
            })
        }
    }

    override fun getContentId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initListener() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}