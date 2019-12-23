package com.zj.imcore.gui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zj.imcore.R
import com.zj.imcore.apis.user.UserApi
import com.zj.imcore.base.FCApplication
import com.zj.imcore.ui.main.MainActivity
import com.zj.imcore.ui.views.LoadingButton
import com.zj.imcore.ui.views.pager.LoginViewPager

class LoginActivity : AppCompatActivity(), (View?) -> Unit {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_login_act_content)
        initView()
        initListener()
    }

    private fun initView() {
        vp = findViewById(R.id.app_act_login_vp)
        btnLoginOrSign = findViewById(R.id.app_act_login_btn)
        vp?.let {
            it.controlViewPagerSpeed(500)
            it.initData(this, viewIdLogin, viewIdSign)
        }
    }

    private fun initListener() {
        btnLoginOrSign?.setOnClickListener {
            if (vp?.isScrolling == true) return@setOnClickListener
            when (it.tag) {
                tagLogin -> {
                    etLoginName?.clearFocus()
                    etLoginPassword?.clearFocus()
                    performLogin()
                }
                tagSign -> {
                    etSignName?.clearFocus()
                    etSignPwd?.clearFocus()
                    etSignPwdCof?.clearFocus()
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
        etLoginNameLayout?.error = null
        etLoginPasswordLayout?.error = null
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
        etSignNameLayout?.error = null
        etSignPwdLayout?.error = null
        etSignPwdCofLayout?.error = null
        btnLoginOrSign?.startLoading()
        sign(ac, pwd)
    }

    private fun login(ac: String, pwd: String) {
        enableViews(false)
        UserApi.login(ac, pwd) { isSuccess, data, throwable ->
            if (isSuccess) {
                btnLoginOrSign?.loadingSuccessful()
                data?.saveAsSP()
                vp?.postDelayed({
                    enableViews(true)
                    btnLoginOrSign?.reset()
                    startMainAct()
                }, 1000)
            } else {
                enableViews(true)
                btnLoginOrSign?.loadingFailed()
                FCApplication.showToast(throwable?.response()?.errorBody()?.string() ?: getString(R.string.app_login_failed))
            }
        }
    }

    private fun startMainAct() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun sign(ac: String, pwd: String) {
        enableViews(false)
        UserApi.sign(ac, pwd, "zjj0888@gmail.com", false) { isSuccess, data, throwable ->
            if (isSuccess) {
                btnLoginOrSign?.loadingSuccessful()
                FCApplication.showToast(getString(R.string.app_sign_welcome, data?.name))
                vp?.postDelayed({
                    vp?.setCurrentItem(0, true)
                    enableViews(true)
                    btnLoginOrSign?.reset()
                }, 1000)
            } else {
                enableViews(true)
                btnLoginOrSign?.loadingFailed()
                FCApplication.showToast(throwable?.response()?.errorBody()?.string() ?: getString(R.string.app_login_failed))
            }
        }

    }

    private fun enableViews(enable: Boolean) {
        tvGotoLogin?.isEnabled = enable
        btnLoginOrSign?.isEnabled = enable
        etLoginName?.isEnabled = enable
        etLoginPassword?.isEnabled = enable
        etSignName?.isEnabled = enable
        etSignPwd?.isEnabled = enable
        etSignPwdCof?.isEnabled = enable
    }

    override fun invoke(v: View?) {
        when (v?.id) {
            R.id.app_act_login_ll_content -> {
                if (etLoginName == null) {
                    etLoginName = v.findViewById(R.id.app_act_login_et_account)
                    etLoginName?.setOnFocusChangeListener { _, i ->
                        if (i) etLoginNameLayout?.error = null
                    }
                }
                if (etLoginPassword == null) {
                    etLoginPassword = v.findViewById(R.id.app_act_login_et_password)
                    etLoginPassword?.setOnFocusChangeListener { _, i ->
                        if (i) etLoginPasswordLayout?.error = null
                    }
                }
                if (etLoginNameLayout == null) {
                    etLoginNameLayout = v.findViewById(R.id.app_act_login_et_account_layout)
                }
                if (etLoginPasswordLayout == null) {
                    etLoginPasswordLayout = v.findViewById(R.id.app_act_login_et_password_layout)
                }
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
                if (etSignName == null) {
                    etSignName = v.findViewById(R.id.app_act_sign_et_account)
                    etSignName?.setOnFocusChangeListener { _, i ->
                        if (i) etSignNameLayout?.error = null
                    }
                }
                if (etSignPwd == null) {
                    etSignPwd = v.findViewById(R.id.app_act_sign_et_password)
                    etSignPwd?.setOnFocusChangeListener { _, i ->
                        if (i) etSignPwdLayout?.error = null
                    }
                }
                if (etSignPwdCof == null) {
                    etSignPwdCof = v.findViewById(R.id.app_act_sign_et_password_ensure)
                    etSignPwdCof?.setOnFocusChangeListener { _, i ->
                        if (i) etSignPwdCofLayout?.error = null
                    }
                }
                if (etSignNameLayout == null) etSignNameLayout = v.findViewById(R.id.app_act_sign_et_account_layout)
                if (etSignPwdLayout == null) etSignPwdLayout = v.findViewById(R.id.app_act_sign_et_password_layout)
                if (etSignPwdCofLayout == null) etSignPwdCofLayout = v.findViewById(R.id.app_act_sign_et_password_ensure_layout)
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