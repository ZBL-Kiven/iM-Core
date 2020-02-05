package com.zj.imcore.ui.main.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.ui.img.CacheAble
import com.zj.ui.img.cache.ImageCacheUtil.Companion.CENTER_CROP
import com.zj.imcore.R
import com.zj.imcore.base.FCApplication
import com.zj.imcore.genderIsLady
import com.zj.imcore.ui.users.UserInfoActivity
import com.zj.imcore.ui.views.LoadingButton
import com.zj.imcore.utils.img.loader.AvatarLoadUtil

class SettingFragment : BaseLinkageFragment() {

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.app_fragment_setting_content, container, false)
    }

    private var ivAvatar: ImageView? = null
    private var lbLogout: LoadingButton? = null
    private var tvName: TextView? = null
    private var tvInfoBar: TextView? = null
    private var tvEmail: TextView? = null
    private var tvPhone: TextView? = null
    private var tvNote: TextView? = null
    private var tvClearCaches: TextView? = null

    override fun onCreate() {
        super.onCreate()
        initView()
        initListener()
    }

    private fun initView() {
        ivAvatar = find(R.id.app_fragment_setting_iv_avatar)
        tvName = find(R.id.app_fragment_setting_tv_name)
        tvInfoBar = find(R.id.app_fragment_setting_tv_info_bar)
        tvEmail = find(R.id.app_fragment_setting_tv_email)
        tvPhone = find(R.id.app_fragment_setting_tv_tel)
        tvNote = find(R.id.app_fragment_setting_tv_note)
        tvClearCaches = find(R.id.app_fragment_setting_tv_clear_caches)
        lbLogout = find(R.id.app_fragment_setting_lb_logout)
    }

    private fun initData() {
        val ctx = this.context ?: return
        val userAvatar = SPUtils_Proxy.getUserAvatar("")
        val userId = SPUtils_Proxy.getUserId(0)
        val gender = SPUtils_Proxy.getUserGender(getString(R.string.app_user_info_secret))
        val userName = SPUtils_Proxy.getUserName("none")
        val userCountry = SPUtils_Proxy.getUserCountry("China")
        val userTel = SPUtils_Proxy.getUserTel(getString(R.string.app_user_info_unset))
        val userEmail = SPUtils_Proxy.getUserEmail(getString(R.string.app_user_info_unset))
        val userAddress = SPUtils_Proxy.getUserAddress("Beijing")
        val userNote = SPUtils_Proxy.getUserNote("")

        val infoBarBackground = if (genderIsLady(gender)) {
            R.mipmap.app_user_info_icon_gender_lady
        } else R.mipmap.app_user_info_icon_gender_male

        ivAvatar?.let {
            it.post {
                AvatarLoadUtil(ctx, it.width, it.height, 1f, object : CacheAble {
                    override fun getCacheName(payloads: String?): String {
                        return "$userId"
                    }

                    override fun getOriginalPath(payloads: String?): String? {
                        return userAvatar
                    }
                }, CENTER_CROP).load { path ->
                    Glide.with(ctx).load(path).error(R.mipmap.app_contact_avatar_default).into(it)
                }
            }
        }
        tvName?.text = if (!userName.isNullOrEmpty()) userName else {
            "id: $userId"
        }
        tvInfoBar?.setBackgroundResource(infoBarBackground)
        tvInfoBar?.text = StringBuilder().append(gender).append("  ").append(userAddress).append("·").append(userCountry).toString()
        tvEmail?.text = userEmail
        tvPhone?.text = userTel
        if (userNote.isNullOrEmpty()) {
            tvNote?.visibility = View.GONE
        } else {
            tvNote?.visibility = View.VISIBLE
        }
        tvNote?.text = userNote
    }

    private fun initListener() {
        ivAvatar?.setOnClickListener {
            UserInfoActivity.start(context, SPUtils_Proxy.getUserId(0))
        }
        lbLogout?.setOnClickListener {
            lbLogout?.startLoading()
            lbLogout?.isEnabled = false
            FCApplication.logout("log out form user") {
                if (it) lbLogout?.loadingSuccessful()
                else {
                    lbLogout?.loadingFailed()
                    lbLogout?.isEnabled = false
                }
            }
        }
    }

    override fun onResumed() {
        super.onResumed()
        initData()
    }
}