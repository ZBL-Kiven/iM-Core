package com.zj.imcore.ui.list

import android.graphics.Color
import android.widget.ImageView
import com.zj.imcore.R

object ChatOption {

    var shadowY: Float = 5f
    var shadowX: Float = 5f
    var shadowRadius: Float = 2f
    var shadowColor: Int = R.color.chat_bubble_shadow
    var bubbleColorOthers: Int = R.color.chat_bubble_other
    var bubbleColorSelf: Int = R.color.chat_bubble_self
    var bubbleRadius: Float = 8f
    var bubblePadding: Float = 8f

    var lookLength: Float = 8f
    var lookWidth: Float = 6f
    var lookPosition: Float = 11f

    var avatarWidth: Float = 40f
    var avatarHeight: Float = 40f
    var avatarScaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP

    var nicknameTextSize: Float = 10f
    var nicknameTextColor: Int = Color.BLACK
    var nicknameStartMargins: Float = 8f

    var itemMarginStart: Float = 10f
    var itemMarginEnd: Float = 10f
    var itemMarginTop: Float = 5f
    var itemMarginBottom: Float = 5f
    var bubbleStartMargins: Float = 3f
    var bubbleTopMargins: Float = 1f

    var timeLineTextSize: Float = 12f
    var timeLineTextColor: Int = R.color.chat_text_time_line
    var timeLineTopMargin: Float = 0f
    var timeLineBottomMargin: Float = 20f

    var infoLineTextSize: Float = 12f
    var infoLineTextColor: Int = R.color.chat_text_info_line
    var infoLineTopMargin: Float = 10f
    var infoLineBottomMargin: Float = 10f

    var maximumDiffDisplayTime: Long = 1 * 1000

    /**
     * max normal text width
     * */
    var NORMAL_MSG_MAX_WIDTH = 208f


}