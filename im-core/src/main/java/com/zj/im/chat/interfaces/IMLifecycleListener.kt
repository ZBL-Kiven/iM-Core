package com.zj.im.chat.interfaces

import com.zj.im.chat.modle.IMLifecycle

/**
 * Created by ZJJ
 */

internal interface IMLifecycleListener {
    fun onLifecycle(state: IMLifecycle)
}
