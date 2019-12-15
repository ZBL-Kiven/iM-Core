package com.zj.imcore.ui.list.model.sub

import android.content.Context
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.ui.list.model.BaseItemMod

class InfoMod : BaseItemMod() {

    override fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?) {
        if (payloads.isNullOrEmpty()) {
            view.getInfoLineView()?.text = data.text
        }
    }

}