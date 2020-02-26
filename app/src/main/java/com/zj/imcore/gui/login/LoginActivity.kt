package com.zj.imcore.gui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.cf.im.db.databases.AppDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.R
import com.zj.imcore.apis.user.UserApi
import com.zj.imcore.base.FCApplication
import com.zj.imcore.gui.login.pager.LoginViewPager
import com.zj.imcore.model.login.LoginInfo
import com.zj.imcore.ui.main.MainActivity
import com.zj.imcore.ui.views.LoadingButton
import retrofit2.HttpException
import java.lang.Exception
import kotlin.math.max

class LoginActivity : AppCompatActivity() {

    private var vp: LoginViewPager? = null
    private var tvGotoSign: View? = null
    private var tvGotoLogin: View? = null
    private var etLoginName: TextInputEditText? = null
    private var etLoginPassword: TextInputEditText? = null
    private var etSignName: TextInputEditText? = null
    private var etSignPwd: TextInputEditText? = null
    private var etSignPwdCof: TextInputEditText? = null
    private var etSignTell: TextInputEditText? = null
    private var etSignEmail: TextInputEditText? = null
    private var etSignDone: EditText? = null
    private var rgSignGender: RadioGroup? = null
    private var signScroller: ScrollView? = null
    private var etLoginNameLayout: TextInputLayout? = null
    private var etLoginPasswordLayout: TextInputLayout? = null
    private var etSignNameLayout: TextInputLayout? = null
    private var etSignPwdLayout: TextInputLayout? = null
    private var etSignPwdCofLayout: TextInputLayout? = null
    private var etSignTellLayout: TextInputLayout? = null
    private var etSignEmailLayout: TextInputLayout? = null
    private var tvSignGenderHint: View? = null
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
        btnLoginOrSign?.tag = tagLogin
        vp?.let {
            it.controlViewPagerSpeed(500)
            it.initData(onDataSet, onPageChanged, viewIdLogin, viewIdSign)
        }
    }

    private fun initListener() {
        btnLoginOrSign?.setOnClickListener {
            if (vp?.isScrolling == true) return@setOnClickListener
            when (it.tag) {
                tagLogin -> {
                    clearFoucs()
                    performLogin()
                }
                tagSign -> {
                    clearFoucs()
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
            scrollTo(etSignName)
            return
        }
        val pwd = etSignPwd?.text?.toString()
        if (pwd.isNullOrEmpty()) {
            etSignPwdLayout?.error = getString(R.string.app_login_warming_null_pwd)
            scrollTo(etSignPwd)
            return
        }
        val cof = etSignPwdCof?.text?.toString()
        if (cof.isNullOrEmpty()) {
            etSignPwdCofLayout?.error = getString(R.string.app_sign_warming_null_pwd_ensure)
            scrollTo(etSignPwdCof)
            return
        }
        if (pwd != cof) {
            etSignPwdLayout?.error = getString(R.string.app_sign_warming_pwd_ensure_error)
            etSignPwdCofLayout?.error = getString(R.string.app_sign_warming_pwd_ensure_error)
            scrollTo(etSignPwd)
            return
        }
        val tel = etSignTell?.text?.toString()
        if (tel.isNullOrEmpty()) {
            etSignTellLayout?.error = getString(R.string.app_login_warming_null_tell)
            scrollTo(etSignTell)
            return
        }
        val email = etSignEmail?.text?.toString()
        if (email.isNullOrEmpty()) {
            etSignEmailLayout?.error = getString(R.string.app_login_warming_null_email)
            scrollTo(etSignEmail)
            return
        }
        val genderIsLady = getCheckedGender()
        if (genderIsLady == null) {
            scrollTo(tvSignGenderHint)
            return
        }
        etSignNameLayout?.error = null
        etSignPwdLayout?.error = null
        etSignPwdCofLayout?.error = null
        btnLoginOrSign?.startLoading()
        sign(ac, pwd, tel, email, genderIsLady)
    }

    private fun scrollTo(v: View?) {
        signScroller?.let {
            val a = IntArray(2)
            v?.getLocationInWindow(a)
            val p = a[1]
            val scr = it.scrollY
            val offset = p + (scr - it.height)
            it.smoothScrollTo(0, max(0, offset))
        }
    }

    private fun login(ac: String, pwd: String) {
        fun loginFailed(throwable: HttpException?) {
            enableViews(true)
            btnLoginOrSign?.loadingFailed()
            FCApplication.showToast(throwable?.response()?.errorBody()?.string() ?: getString(R.string.app_login_failed))
        }
        enableViews(false)
        UserApi.login(ac, pwd) { isSuccess, data, throwable ->
            if (isSuccess && data != null) {
                try {
                    TeamManager.saveAsSp(data)
                    AppDatabase.singleton.get(data.user?.id ?: "default")
                    btnLoginOrSign?.loadingSuccessful()
                } catch (e: Exception) {
                    loginFailed(null)
                }
                vp?.postDelayed({
                    enableViews(true)
                    btnLoginOrSign?.reset()
                    startTeamsAct()
                }, 1000)
            } else {
                loginFailed(throwable)
            }
        }
    }


    private fun startTeamsAct() {
        TeamsActivity.startAct(this@LoginActivity)
        finish()
    }

    private fun sign(ac: String, pwd: String, tel: String, email: String, genderIsLady: Boolean) {
        enableViews(false)
        UserApi.sign(ac, pwd, tel, email, genderIsLady) { isSuccess, data, throwable ->
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

    private fun clearFoucs() {
        etLoginName?.clearFocus()
        etLoginPassword?.clearFocus()
        etSignName?.clearFocus()
        etSignPwd?.clearFocus()
        etSignPwdCof?.clearFocus()
        etSignTell?.clearFocus()
        etSignEmail?.clearFocus()
    }

    private val onPageChanged: (v: View?) -> Unit = { v ->
        when (v?.id) {
            R.id.app_act_login_ll_content -> {
                btnLoginOrSign?.text = getString(R.string.app_login_text_login)
                btnLoginOrSign?.tag = tagLogin
            }
            R.id.app_act_sign_ll_content -> {
                btnLoginOrSign?.text = getString(R.string.app_login_text_sign)
                btnLoginOrSign?.tag = tagSign
            }
        }
    }
    private val onDataSet: (v: View?) -> Unit = { v ->
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
                if (etSignTell == null) {
                    etSignTell = v.findViewById(R.id.app_act_sign_et_tell)
                    etSignTell?.setOnFocusChangeListener { _, i ->
                        if (i) etSignTellLayout?.error = null
                    }
                }
                if (etSignEmail == null) {
                    etSignEmail = v.findViewById(R.id.app_act_sign_et_email)
                    etSignEmail?.setOnFocusChangeListener { _, i ->
                        if (i) etSignEmailLayout?.error = null
                    }
                }
                if (rgSignGender == null) {
                    rgSignGender = v.findViewById(R.id.app_act_sign_rg_gender)
                    rgSignGender?.setOnCheckedChangeListener { _, _ ->
                        if (tvSignGenderHint?.isSelected == true) tvSignGenderHint?.isSelected = false
                        clearFoucs()
                    }
                }
                if (etSignDone == null) {
                    etSignDone = v.findViewById(R.id.app_act_sign_et_done)
                    etSignDone?.setOnEditorActionListener { tv, _, _ ->
                        if (tv.imeActionId == resources.getInteger(R.integer.app_action_sign)) {
                            btnLoginOrSign?.callOnClick()
                        }
                        return@setOnEditorActionListener false
                    }
                }
                if (signScroller == null) {
                    signScroller = v.findViewById(R.id.app_act_sign_scr_content)
                }
                if (etSignNameLayout == null) etSignNameLayout = v.findViewById(R.id.app_act_sign_et_account_layout)
                if (etSignPwdLayout == null) etSignPwdLayout = v.findViewById(R.id.app_act_sign_et_password_layout)
                if (etSignPwdCofLayout == null) etSignPwdCofLayout = v.findViewById(R.id.app_act_sign_et_password_ensure_layout)
                if (etSignTellLayout == null) etSignTellLayout = v.findViewById(R.id.app_act_sign_et_tell_layout)
                if (etSignEmailLayout == null) etSignEmailLayout = v.findViewById(R.id.app_act_sign_et_email_layout)
                if (tvSignGenderHint == null) tvSignGenderHint = v.findViewById(R.id.app_act_sign_tv_gender_hint)
                if (tvGotoLogin == null) {
                    tvGotoLogin = v.findViewById(R.id.app_act_sign_tv_goto_login)
                    tvGotoLogin?.setOnClickListener { _ ->
                        vp?.setCurrentItem(0, true)
                    }
                }
            }
        }
    }

    private fun getCheckedGender(): Boolean? {
        val gender = rgSignGender?.checkedRadioButtonId?.let {
            when (it) {
                R.id.app_act_sign_rb1 -> false
                R.id.app_act_sign_rb2 -> true
                else -> null
            }
        }
        if (gender == null) {
            tvSignGenderHint?.isSelected = true
        }
        return gender
    }

    override fun onDestroy() {
        vp?.destroy()
        super.onDestroy()
    }
}