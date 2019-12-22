package com.zj.imcore.main

import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zj.base.BaseActivity
import com.zj.imcore.R
import com.zj.imcore.ui.views.LoadingButton
import com.zj.imcore.ui.views.pager.LoginViewPager


class LoginActivity : BaseActivity(), (View?) -> Unit {

    private var vp: LoginViewPager? = null
    private var tvGotoSign: View? = null
    private var tvGotoLogin: View? = null
    private var etLoginName: TextInputEditText? = null
    private var etLoginPassword: TextInputEditText? = null
    private var etSignName: TextInputEditText? = null
    private var etSignPwd: TextInputEditText? = null
    private var etSignPwdCof: TextInputEditText? = null
    private var etLoginNameLayout: TextInputLayout? = null
    private var etLoginPasswordLayout: TextInputLayout? = null
    private var etSignNameLayout: TextInputLayout? = null
    private var etSignPwdLayout: TextInputLayout? = null
    private var etSignPwdCofLayout: TextInputLayout? = null
    private var btnLoginOrSign: LoadingButton? = null

    private val tagLogin = "tagLogin"
    private val tagSign = "tagSign"

    private var viewIdLogin = R.layout.app_login_vp_item_login
    private var viewIdSign = R.layout.app_login_vp_item_sign

    override fun getContentId(): Int {
        return R.layout.app_login_activity_content
    }

    override fun setTitle(): String? {
        return null
    }

    override fun initView() {
        vp = find(R.id.app_act_login_vp)
        btnLoginOrSign = find(R.id.app_act_login_btn)
    }

    override fun initData() {
        vp?.let {
            it.controlViewPagerSpeed(500)
            it.initData(this, viewIdLogin, viewIdSign)
        }

    }

    override fun initListener() {
        btnLoginOrSign?.setOnClickListener {
            if (vp?.isScrolling == true) return@setOnClickListener
            when (it.tag) {
                tagLogin -> {
                    performLogin()
                }
                tagSign -> {
                    performSign()
                }
            }
        }
    }

    private fun performLogin() {
        val ac = etLoginName?.text?.toString()
        if (ac.isNullOrEmpty()) {
            etLoginNameLayout?.error = getString(R.string.app_login_warming_null_account)
            return
        }
        val pwd = etLoginPassword?.text?.toString()
        if (pwd.isNullOrEmpty()) {
            etLoginPasswordLayout?.error = getString(R.string.app_login_warming_null_pwd)
            return
        }
        btnLoginOrSign?.startLoading()
        login(ac, pwd)
    }

    private fun performSign() {
        val ac = etSignName?.text?.toString()
        if (ac.isNullOrEmpty()) {
            etSignNameLayout?.error = getString(R.string.app_login_warming_null_account)
            return
        }
        val pwd = etSignPwd?.text?.toString()
        if (pwd.isNullOrEmpty()) {
            etSignPwdLayout?.error = getString(R.string.app_login_warming_null_pwd)
            return
        }
        val cof = etSignPwdCof?.text?.toString()
        if (cof.isNullOrEmpty()) {
            etSignPwdCofLayout?.error = getString(R.string.app_sign_warming_null_pwd_ensure)
            return
        }
        if (pwd != cof) {
            etSignPwdLayout?.error = getString(R.string.app_sign_warming_pwd_ensure_error)
            etSignPwdCofLayout?.error = getString(R.string.app_sign_warming_pwd_ensure_error)
            return
        }
        sign(ac, pwd)
    }

    private fun login(ac: String, pwd: String) {

    }

    private fun sign(ac: String, pwd: String) {

    }

    override fun invoke(v: View?) {
        when (v?.id) {
            R.id.app_act_login_ll_content -> {
                if (etLoginName == null) etLoginName = v.findViewById(R.id.app_act_login_et_account)
                if (etLoginPassword == null) etLoginPassword = v.findViewById(R.id.app_act_login_et_password)
                if (etLoginNameLayout == null) etLoginNameLayout = v.findViewById(R.id.app_act_login_et_account_layout)
                if (etLoginPasswordLayout == null) etLoginPasswordLayout = v.findViewById(R.id.app_act_login_et_password_layout)
                if (tvGotoSign == null) {
                    tvGotoSign = v.findViewById(R.id.app_act_login_tv_goto_sign)
                    tvGotoSign?.setOnClickListener { _ ->
                        vp?.setCurrentItem(1, true)
                    }
                }
                btnLoginOrSign?.text = getString(R.string.app_login_text_login)
                btnLoginOrSign?.tag = tagLogin
            }
            R.id.app_act_sign_ll_content -> {
                if (etSignName == null) etSignName = v.findViewById(R.id.app_act_sign_et_account)
                if (etSignPwd == null) etSignPwd = v.findViewById(R.id.app_act_sign_et_password)
                if (etSignPwdCof == null) etSignPwdCof = v.findViewById(R.id.app_act_sign_et_password_ensure)
                if (etSignNameLayout == null) etSignNameLayout = v.findViewById(R.id.app_act_sign_et_account_layout)
                if (etSignPwdLayout == null) etSignPwdLayout = v.findViewById(R.id.app_act_sign_et_password_layout)
                if (etSignPwdCofLayout == null) etSignPwdCofLayout = v.findViewById(R.id.app_act_sign_et_password_layout)
                if (tvGotoLogin == null) {
                    tvGotoLogin = v.findViewById(R.id.app_act_sign_tv_goto_login)
                    tvGotoLogin?.setOnClickListener { _ ->
                        vp?.setCurrentItem(0, true)
                    }
                }
                btnLoginOrSign?.text = getString(R.string.app_login_text_sign)
                btnLoginOrSign?.tag = tagSign
            }
        }
    }

    override fun onDestroy() {
        vp?.destroy()
        super.onDestroy()
    }
}