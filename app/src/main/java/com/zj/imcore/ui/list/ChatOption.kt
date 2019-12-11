package com.zj.imcore.ui.list

import android.graphics.Color
import android.widget.ImageView
import com.zj.imcore.R

object ChatOption {

    var mShadowY: Float = 5f
    var mShadowX: Float = 5f
    var mShadowRadius: Float = 2f
    var mShadowColor: Int = R.color.chat_bubble_shadow
    var mBubbleColorOthers: Int = R.color.chat_bubble_other
    var mBubbleColorSelf: Int = R.color.chat_bubble_self
    var mBubbleRadius: Float = 8f
    var mBubblePadding: Float = 8f

    var mLookLength: Float = 8f
    var mLookWidth: Float = 6f
    var mLookPosition: Float = 11f

    var avatarWidth: Float = 40f
    var avatarHeight: Float = 40f
    var avatarScaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP

    var nicknameTextSize: Float = 10f
    var nicknameTextColor: Int = Color.BLACK
    var mNicknameStartMargins: Float = 8f

    var mItemMargins: Float = 10f
    var mBubbleStartMargins: Float = 3f
    var mBubbleTopMargins: Float = 1f

    var mTimeLineTextSize: Float = 12f
    var mTimeLineTextColor: Int = R.color.chat_text_time_line
    var mTimeLineTopMargin: Float = 0f
    var mTimeLineBottomMargin: Float = 20f

    var mInfoLineTextSize: Float = 12f
    var mInfoLineTextColor: Int = R.color.chat_text_info_line
    var mInfoLineTopMargin: Float = 10f
    var mInfoLineBottomMargin: Float = 10f

    var mMaximumDiffDisplayTime: Long = 1 * 1000
}