package com.zj.imcore.ui.users

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cf.im.db.domain.MemberBean
import com.cf.im.db.repositorys.MemberRepository
import com.zj.album.AlbumIns
import com.zj.album.options.AlbumOptions
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.im.mainHandler
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.ui.chat.ChatActivity
import kotlinx.android.synthetic.main.app_act_user_info_content.*

class UserInfoActivity : FCActivity() {

    companion object {

        private const val UID = "uid"
        private const val CHAT_MOD = "chatMode"

        fun start(context: Context?, uid: Long?, isChatMod: Boolean = false) {
            context?.startActivity(Intent(context, UserInfoActivity::class.java).apply {
                if (uid != null) this.putExtra(UID, uid)
                this.putExtra(CHAT_MOD, isChatMod)
            })
        }
    }

    override fun getContentId(): Int {
        return R.layout.app_act_user_info_content
    }

    private var userId = 0L
    private var isChatMod = false
    private var curUser: MemberBean? = null

    //view
    private var ivUserAvatar: ImageView? = null
    private var tvUserNickName: TextView? = null
    private var tvUserName: TextView? = null
    private var tvUserDescribe: TextView? = null

    override fun initView() {
        showTitleBar(true)

        ivUserAvatar = findViewById(R.id.ivUserAvatar)
        tvUserNickName = findViewById(R.id.tvUserNickName)
        tvUserName = findViewById(R.id.tvUserName)
        tvUserDescribe = findViewById(R.id.tvUserDescribe)

        try {
            intent?.let {
                if (it.hasExtra(UID)) userId = it.getLongExtra(UID, 0)
                if (it.hasExtra(CHAT_MOD)) isChatMod = it.getBooleanExtra(CHAT_MOD, false)
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
        curUser?.let { member ->
            setTitle(getString(R.string.app_act_user_info_title_default, member.name))
            ivUserAvatar?.let { view ->
                Glide.with(view).load(member.avatar).error(R.mipmap.app_contact_avatar_default).into(view)
            }

            tvUserNickName?.text = member.title ?: ""
            tvUserName?.text = member.name ?: ""
            tvUserDescribe?.text = member.avatar ?: ""

            btnSendMsg.visibility = if (SPUtils_Proxy.getUserId(0) == curUser?.uid) View.GONE else View.VISIBLE
        }
    }

    override fun initListener() {
        tvUserName?.setOnClickListener {
            //设置用户名称
            if (SPUtils_Proxy.getUserId(0) != curUser?.uid) {
                return@setOnClickListener
            }
        }

        tvUserNickName?.setOnClickListener {
            //设置用户昵称
            if (SPUtils_Proxy.getUserId(0) != curUser?.uid) {
                return@setOnClickListener
            }
        }

        ivUserAvatar?.setOnClickListener {
            if (SPUtils_Proxy.getUserId(0) != curUser?.uid) {
                return@setOnClickListener
            }
            //设置用户头像
            AlbumIns.with(this).mimeTypes(AlbumOptions.ofImage()).maxSelectedCount(1).imgSizeRange(1024, Long.MAX_VALUE).start { isCancel, data ->
                if (!isCancel && !data.isNullOrEmpty()) {
                    ivUserAvatar?.let {
                        Glide.with(it).load(data[0].path).error(R.mipmap.app_contact_avatar_default).into(it)
                    }
                }
            }
        }

        btnSendMsg?.setOnClickListener { _ ->
            curUser?.let {
                if (isChatMod) finish() else ChatActivity.start(this, it.dialogId, Constance.DIALOG_TYPE_P2P, it.uid, "", it.name)
            } ?: finish()
        }
    }
}