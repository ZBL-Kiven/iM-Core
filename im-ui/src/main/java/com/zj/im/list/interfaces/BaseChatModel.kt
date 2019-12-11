package com.zj.im.list.interfaces

import android.content.Context
import com.zj.im.list.views.ChatItemView
import com.zj.list.multiable.MultiAbleData

interface BaseChatModel<T : MultiAbleData<T>> {

    fun isInitTimeStampView(data: T): Boolean
    fun isInitInfoView(data: T): Boolean
    fun isInitBaseBubbleView(data: T): Boolean
    fun getOrientation(data: T): ChatItemView.Orientation

    fun initData(context: Context, view: ChatItemView, data: T, payloads: List<Any>?)
}