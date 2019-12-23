package com.zj.imcore.ui.main.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.im.registerMsgReceivedListener
import com.zj.im.store.interfaces.DataListener
import com.zj.im_model.mod.MsgInfo
import com.zj.im_model.mod.MsgReceivedInfo
import com.zj.imcore.R
import com.zj.imcore.options.IMHelper
import com.zj.imcore.renderer.TestHandler

class SettingFragment : BaseLinkageFragment() {

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.app_fragment_setting_content, container, false)
    }

}