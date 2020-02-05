package com.zj.ui.emotionboard.interfaces

import android.view.View
import com.zj.ui.emotionboard.data.Emoticon
import com.zj.ui.emotionboard.data.EmoticonPack

interface EmoticonsToolBar {

    fun setToolBarItemClickListener(listener: OnToolBarItemClickListener?)

    fun selectEmotionPack(pack: EmoticonPack<out Emoticon>)

    fun setPackList(packs: List<EmoticonPack<out Emoticon>>)

    fun addFixedToolItemView(view: View?, isRight: Boolean)

    fun notifyDataChanged()
}

interface OnToolBarItemClickListener {
    fun onToolBarItemClick(pack: EmoticonPack<out Emoticon>)
}