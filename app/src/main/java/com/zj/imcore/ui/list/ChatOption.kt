package com.zj.imcore.ui.list

import android.graphics.Color
import android.widget.ImageView
import com.zj.imcore.R

object ChatOption {

    var mShadowY: Int = 5
    var mShadowX: Int = 5
    var mShadowRadius: Int = 2
    var mShadowColor: Int = R.color.chat_bubble_shadow
    var mBubbleColorOthers: Int = R.color.chat_bubble_other
    var mBubbleColorSelf: Int = R.color.chat_bubble_self
    var mBubbleRadius: Int = 8
    var mBubblePadding: Int = 8

    var mLookLength: Int = 8
    var mLookWidth: Int = 6
    var mLookPosition: Int = 11

    var avatarWidth: Int = 100
    var avatarHeight: Int = 100
    var avatarScaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP

    var nicknameTextSize: Float = 9f
    var nicknameTextColor: Int = Color.BLACK

    var mItemMargins: Int = 24
    var mNicknameStartMargins: Int = 15
    var mBubbleStartMargins: Int = 5
    var mBubbleTopMargins: Int = 3
}