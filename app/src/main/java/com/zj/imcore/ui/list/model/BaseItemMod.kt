package com.zj.imcore.ui.list.model

import android.content.Context
import com.zj.im.list.views.ChatItemView
import com.zj.imcore.mod.MsgInfo

/**
 * Created by ZJJ on 19/12/12
 *
 * extends to custom any msg item;
 * */
abstract class BaseItemMod {

    abstract fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?)

}