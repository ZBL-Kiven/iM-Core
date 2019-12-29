package com.zj.imcore.ui.list.model.sub

import android.content.Context
import com.zj.im.list.views.ChatItemView
import com.zj.model.mod.MsgInfo
import com.zj.imcore.ui.list.model.BaseItemMod

/**
 * Created by ZJJ on 19/12/12
 *
 * the system info type of msg view model
 * */
class InfoMod : BaseItemMod() {

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        if (payloads.isNullOrEmpty()) {
            view.getInfoLineView()?.text = data.impl.text()
        }
    }

}