package com.zj.imcore.ui.list

import android.graphics.Color
import android.widget.ImageView
import com.zj.imcore.R

/**
 * Created by ZJJ on 19/12/12
 *
 * the chat list item options ,
 *
 * configure almost all of properties for Bubble  here,
 *
 * attributes will be automatically converted to pixel units when used.
 * */
object ChatOption {

    var bubbleTextSize = 14f
    var bubbleTextColor = R.color.chat_text

    var videoPlayViewWidth = 33f
    var videoPlayViewHeight = 33f
    var videoPlayViewSource = R.mipmap.bubble_item_video_play

    var isPrintErrorAble = true

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
    var avatarRadius: Float = 3f
    var avatarQuality: Float = 1.0f
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

    /**
     * img size
     **/
    var IMAGE_MSG_MAX_WIDTH = 145f
    var IMAGE_MSG_MAX_HEIGHT = 217f
    var IMAGE_MSG_MIN_SCALE = 0.23f
    var IMAGE_MSG_QUALITY = .5f

    /**
     * sticker size
     **/
    var STICKER_MSG_MAX_WIDTH = 95f
    var STICKER_MSG_MAX_HEIGHT = 147f
    var STICKER_MSG_MIN_SCALE = 0.23f
    var STICKER_MSG_QUALITY = .5f
}