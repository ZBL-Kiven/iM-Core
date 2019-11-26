package com.zj.im.chat.utils

import com.zj.im.chat.core.DataStore
import com.zj.im.chat.enums.RuntimeEfficiency
import com.zj.im.chat.hub.StatusHub.isRunningInBackground
import com.zj.im.main.ChatBase
import com.zj.im.sender.SendingPool

/**
 * created by ZJJ
 *
 * the efficiency checker ,
 *
 * the SDK may adjust frequency to adapt the current state,
 *
 * for saving the power.
 * */

internal object EfficiencyUtils {

    private const val level_SLEEP = 3

    private const val level_LOW = 5

    private const val level_MEDIUM = 8

    private const val level_HIGH = 15

    fun checkEfficiency() {
        val total = DataStore.getTotal() + SendingPool.getTotal()
        if (isRunningInBackground) {
            when (total) {
                in 0..level_SLEEP -> {
                    ChatBase.setFrequency(RuntimeEfficiency.SLEEP)
                }
                in level_SLEEP..level_LOW -> {
                    ChatBase.setFrequency(RuntimeEfficiency.LOW)
                }
                else -> ChatBase.setFrequency(RuntimeEfficiency.OVERCLOCK)
            }
        } else {
            when (total) {
                in 0..level_MEDIUM -> {
                    ChatBase.setFrequency(RuntimeEfficiency.MEDIUM)
                }
                in level_MEDIUM..level_HIGH -> {
                    ChatBase.setFrequency(RuntimeEfficiency.HIGH)
                }
                else -> {
                    ChatBase.setFrequency(RuntimeEfficiency.OVERCLOCK)
                }
            }
        }
    }
}