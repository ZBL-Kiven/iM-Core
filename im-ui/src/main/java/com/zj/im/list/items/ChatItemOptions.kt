package com.zj.im.list.items

import android.widget.ImageView

@Suppress("unused")
abstract class ChatItemOptions {

    abstract fun getLook(): BubbleLayout.Look
    abstract fun getShadowY(): Int
    abstract fun getShadowX(): Int
    abstract fun getShadowRadius(): Int
    abstract fun getShadowColor(): Int
    abstract fun getBubbleColor(): Int
    abstract fun getBubbleRadius(): Int
    abstract fun getLookLength(): Int
    abstract fun getLookWidth(): Int
    abstract fun getLookPosition(): Int
    abstract fun getAvatarWidth(): Int
    abstract fun getAvatarHeight(): Int
    abstract fun getAvatarScaleType(): ImageView.ScaleType
    abstract fun getNicknameTextSize(): Float
    abstract fun getNicknameTextColor(): Int
    abstract fun getItemMargins(): Int
    abstract fun getNicknameStartMargins(): Int
    abstract fun getBubbleStartMargins(): Int
    abstract fun getBubbleTopMargins(): Int
    abstract fun getBubblePadding(): Int

}
