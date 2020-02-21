package com.zj.imcore.ui.users

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cf.im.db.domain.DialogBean
import com.cf.im.db.repositorys.DialogRepository
import com.zj.album.AlbumIns
import com.zj.album.options.AlbumOptions
import com.zj.base.view.BaseTitleView
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.ui.chat.ChatActivity

class UserInfoActivity : FCActivity() {

    companion object {

        private const val UID = "uid"
        private const val CHAT_MOD = "chatMode"

        fun start(context: Context?, tmId: String?, isChatMod: Boolean = false) {
            context?.startActivity(Intent(context, UserInfoActivity::class.java).apply {
                if (tmId != null) this.putExtra(UID, tmId)
                this.putExtra(CHAT_MOD, isChatMod)
            })
        }
    }

    override fun getContentId(): Int {
        return R.layout.app_act_user_info_content
    }

    private var tmId: String = ""
    private var isChatMod = false
    private var curUser: DialogBean? = null

    //view
    private var ivUserAvatar: ImageView? = null
    private var tvUserNickName: TextView? = null
    private var tvUserName: TextView? = null
    private var titleView: BaseTitleView? = null
    private var tvUserDescribe: TextView? = null
    private var btnSend: View? = null

    override fun initView() {
        ivUserAvatar = findViewById(R.id.app_act_user_info_iv_avatar)
        tvUserNickName = findViewById(R.id.app_act_user_info_tv_nick_name)
        tvUserName = findViewById(R.id.app_act_user_info_tv_user_name)
        btnSend = findViewById(R.id.app_act_user_info_btn_send_msg)
        titleView = findViewById(R.id.app_act_uer_info_title)
        titleView?.setLeftIcon(R.mipmap.back)

        tvUserDescribe = findViewById(R.id.app_act_user_info_tv_describe)
        try {
            intent?.let {
                if (it.hasExtra(UID)) tmId = it.getStringExtra(UID) ?: ""
                if (it.hasExtra(CHAT_MOD)) isChatMod = it.getBooleanExtra(CHAT_MOD, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initData() {
        DialogRepository.queryDialogById(tmId) {
            curUser = it
            runOnUiThread {
                setData()
            }
        }
    }

    private fun setData() {
        curUser?.let { member ->
            titleView?.setTitle(getString(R.string.app_act_user_info_title_default, member.name))
            ivUserAvatar?.let { view ->
                Glide.with(view).load(member.avatar).error(R.mipmap.app_contact_avatar_default)
                    .into(view)
            }
            tvUserNickName?.text = member.get("nickname") ?: ""
            tvUserName?.text = member.name ?: ""
            tvUserDescribe?.text = member.get("describe") ?: ""
            btnSend?.visibility = if (isFriends()) View.GONE else View.VISIBLE
        }
    }

    override fun initListener() {
        titleView?.setLeftClickListener {
            onBackPressed()
        }

        //        tvUserName?.setOnClickListener {
        //            //设置用户名称
        //            if (isOneself()) {
        //                return@setOnClickListener
        //            }
        //            EditTextActivity.startActivity(this, getString(R.string.app_act_user_info_user_name_hint), tvUserName?.text?.toString() ?: "", 1, EditTextActivity.TYPE_USER_NAME)
        //        }

        tvUserNickName?.setOnClickListener {
            //设置用户昵称
            if (isFriends()) {
                return@setOnClickListener
            }

            EditTextActivity.startActivity(
                this,
                curUser?.dialogId() ?: "",
                getString(R.string.app_act_user_info_user_nickname_hint),
                tvUserNickName?.text.toString(),
                10,
                EditTextActivity.TYPE_USER_NICK_NAME
            )
        }

        tvUserDescribe?.setOnClickListener {
            if (isFriends()) {
                return@setOnClickListener
            }
            EditTextActivity.startActivity(
                this,
                curUser?.dialogId() ?: "",
                getString(R.string.app_act_user_info_user_describe_hint),
                tvUserDescribe?.text.toString(),
                1,
                EditTextActivity.TYPE_USER_DESCRIBE
            )
        }

        ivUserAvatar?.setOnClickListener {
            if (isFriends()) {
                return@setOnClickListener
            }
            //设置用户头像
            AlbumIns.with(this).mimeTypes(AlbumOptions.ofImage()).maxSelectedCount(1)
                .imgSizeRange(1024, Long.MAX_VALUE).start { isSelected, data ->
                    if (isSelected && !data.isNullOrEmpty()) {
                        ivUserAvatar?.let {
                            Glide.with(it).load(data[0].path)
                                .error(R.mipmap.app_contact_avatar_default)
                                .into(it)
                        }
                    }
                }
        }

        btnSend?.setOnClickListener { _ ->
            curUser?.let {
                if (isChatMod) finish() else ChatActivity.start(
                    this,
                    it.dialogId,
                    Constance.DIALOG_TYPE_P2P,
                    "$it.uid",
                    "",
                    it.name
                )
            } ?: finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK != resultCode) {
            return
        }

        data?.let {
            val content = it.getStringExtra(EditTextActivity.KEY_CONTENT)
            when (requestCode) {
                EditTextActivity.TYPE_USER_NAME -> {
                    tvUserName?.text = content ?: ""
                }
                EditTextActivity.TYPE_USER_NICK_NAME -> {
                    tvUserNickName?.text = content ?: ""
                }
                EditTextActivity.TYPE_USER_DESCRIBE -> {
                    tvUserDescribe?.text = content ?: ""
                }
            }
        }
    }

    private fun isFriends(): Boolean {
        // SPUtils_Proxy.getUserId(0) != curUser?.uid
        return false;
    }
}