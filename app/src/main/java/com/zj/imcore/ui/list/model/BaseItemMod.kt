package com.zj.imcore.ui.list.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.zj.ui.list.views.ChatItemView
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCApplication
import java.lang.Exception

/**
 * Created by ZJJ on 19/12/12
 *
 * extends to custom any msg item;
 * */
@Suppress("unused")
abstract class BaseItemMod {

    abstract fun initData(context: Context, view: ChatItemView, data: MsgInfo, payloads: List<Any>?)

    protected fun isSelf(uid:Long): Boolean {
        return FCApplication.isSelf(uid)
    }

    protected fun getColor(context: Context, c: Int): Int {
        return try {
            ContextCompat.getColor(context, c)
        } catch (e:Exception) {
            c
        }
    }

    protected fun dpToPx(context: Context, dipValue: Float): Int {
        val scale = context.applicationContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    protected fun spToPx(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

}