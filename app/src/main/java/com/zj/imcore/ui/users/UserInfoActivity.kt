package com.zj.imcore.ui.users

import android.content.Context
import android.content.Intent
import com.cf.im.db.domain.MemberBean
import com.cf.im.db.repositorys.MemberRepository
import com.zj.im.mainHandler
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.ui.chat.ChatActivity
import kotlinx.android.synthetic.main.app_act_user_info_content.*

class UserInfoActivity : FCActivity() {

    companion object {

        private const val UID = "uid"
        private const val SESSION_ID = "sessionId"

        fun start(context: Context?, uid: Long?, sessionId: Long?) {
            context?.startActivity(Intent(context, UserInfoActivity::class.java).apply {
                if (uid != null) this.putExtra(UID, uid)
                if (sessionId != null) this.putExtra(SESSION_ID, sessionId)
            })
        }
    }

    override fun getContentId(): Int {
        return R.layout.app_act_user_info_content
    }

    private var sessionId = 0L
    private var userId = 0L
    private var curUser: MemberBean? = null

    override fun initView() {
        try {
            intent?.let {
                if (it.hasExtra(SESSION_ID)) sessionId = it.getLongExtra(SESSION_ID, 0)
                if (it.hasExtra(UID)) userId = it.getLongExtra(UID, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        MemberRepository.queryMembersByUserId(userId) {
            curUser = it
            mainHandler.post {
                setData()
            }
        }
    }

    override fun initData() {

    }

    private fun setData() {
        curUser?.let {
            setTitle(getString(R.string.app_act_user_info_title_default, it.name))
            app_act_user_info_btn_creat_dialog?.setOnClickListener { _ ->
                ChatActivity.start(this, it.dialogId, Constance.DIALOG_TYPE_P2P, it.uid, "", it.name)
            }
        }
    }

    override fun initListener() {

    }
}