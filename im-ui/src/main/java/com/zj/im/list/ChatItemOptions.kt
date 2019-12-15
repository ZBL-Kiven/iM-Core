package com.zj.im.list

import android.widget.ImageView

abstract class ChatItemOptions {

    /**
     * options of avatar
     * */
    abstract fun getAvatarWidth(): Float

    abstract fun getAvatarHeight(): Float

    abstract fun getAvatarScaleType(): ImageView.ScaleType

    /**
     * options of avatar
     */
    abstract fun getNicknameTextColor(): Int

    abstract fun getNicknameStartMargins(): Float

    abstract fun getNicknameTextSize(): Float

    /**
     * margins of full item
     */
    abstract fun getItemMarginStart(): Float

    abstract fun getItemMarginEnd(): Float

    abstract fun getItemMarginTop(): Float

    abstract fun getItemMarginBottom(): Float

    /**
     * options of bubble base
     */
    abstract fun getBubbleColor(): Int

    abstract fun getBubbleRadius(): Float

    abstract fun getBubbleStartMargins(): Float

    abstract fun getBubbleTopMargins(): Float

    abstract fun getBubblePadding(): Float

    /**
     * options of bubble extends
     */
    abstract fun getShadowY(): Float

    abstract fun getShadowX(): Float

    abstract fun getShadowRadius(): Float

    abstract fun getShadowColor(): Int

    /**
     * options of triangle
     */
    abstract fun getLookLength(): Float

    abstract fun getLookWidth(): Float

    abstract fun getLookPosition(): Float

    /**
     * time line configuration
     */
    abstract fun getTimeLineTextSize(): Float

    abstract fun getTimeLineTextColor(): Int

    abstract fun getTimeLineTopMargin(): Float

    abstract fun getTimeLineBottomMargin(): Float

    /**
     * info line configuration
     */
    abstract fun getInfoLineTextSize(): Float

    abstract fun getInfoLineTextColor(): Int

    abstract fun getInfoLineTopMargin(): Float

    abstract fun getInfoLineBottomMargin(): Float

    /**
     * debug line
     * */
    abstract fun isPrintErrorAble(): Boolean
}
