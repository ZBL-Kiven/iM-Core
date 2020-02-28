package com.zj.imcore.ui.main

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentTransaction
import com.cf.im.db.databases.AppDatabase
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.base.view.BaseTitleView
import com.zj.base.view.SelectChangeIndexView
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.cf.managers.BaseFragmentManager
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.gui.login.TeamManager
import com.zj.imcore.ui.main.contact.ContactFragment
import com.zj.imcore.ui.main.setting.SettingFragment
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.ui.main.contact.MainMenuPopupWindow
import com.zj.imcore.ui.main.contact.group.CreateGroupActivity
import com.zj.imcore.ui.main.conversation.ConversationFragment
import java.lang.NullPointerException

class MainActivity : FCActivity() {

    private var llGroup: LinearLayout? = null
    private var baseTitleView: BaseTitleView? = null
    private var conversationFragment: ConversationFragment? = null
    private var contactFragment: ContactFragment? = null
    private var settingFragment: SettingFragment? = null
    private var fragmentManager: BaseFragmentManager? = null

    override fun getContentId(): Int {
        return R.layout.app_main_act_content
    }

    override fun initBase() {
        AppDatabase.singleton.get(SPUtils_Proxy.getUserId("default"))
        IMHelper.init(this.application)
    }

    override fun initView() {
        llGroup = find(R.id.app_act_main_ll_index_group)
        baseTitleView = find(R.id.app_act_main_title)
    }

    override fun initData() {
        conversationFragment = ConversationFragment()
        contactFragment = ContactFragment()
        settingFragment = SettingFragment()
        llGroup?.let {
            fragmentManager = object : BaseFragmentManager(this, R.id.app_act_main_fl_content, 0, it, conversationFragment, contactFragment, settingFragment) {
                override fun onViewAttach(v: View) {
                    if (v is SelectChangeIndexView) {
                        v.init()
                    }
                }

                override fun beginTransaction(isHidden: Boolean, transaction: FragmentTransaction, frgCls: Class<BaseLinkageFragment>) {
                    super.beginTransaction(isHidden, transaction, frgCls)
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                }

                override fun syncSelectState(selectId: String) {
                    super.syncSelectState(selectId)
                    setTitleBar(selectId)
                }
            }
        } ?: throw NullPointerException("the container index group must not be null")
    }

    override fun initListener() {
        baseTitleView?.setRightClickListener {
            if (fragmentManager?.getCurrentItemId() == conversationFragment?.fId) {
                MainMenuPopupWindow(this).setListener {
                    CreateGroupActivity.startCreateGroup(this, 2, 200)
                }.showAsDropDown(baseTitleView, Gravity.END)
            }
        }
    }

    private fun setTitleBar(fid: String) {
        val titleId = when (fid) {
            conversationFragment?.fId -> R.string.app_main_title_chat
            contactFragment?.fId -> R.string.app_main_title_contact
            settingFragment?.fId -> R.string.app_main_title_setting
            else -> 0
        }
        baseTitleView?.setRightIcon(if (fid == conversationFragment?.fId) R.mipmap.app_act_main_new_chat else -1)
        baseTitleView?.setTitle(titleId)
    }

    override fun onDestroy() {
        fragmentManager?.clear()
        super.onDestroy()
    }
}