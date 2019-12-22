package com.zbl.api.utils

import android.util.Log
import com.zbl.api.BuildConfig

internal object LogUtils {

    private val debugAble; get() = BuildConfig.DEBUG
    private const val TAG = " base.api ==> "

    fun d(s: String) {
        if (debugAble) Log.d(TAG, s)
    }

    fun e(s: String) {
        if (debugAble) Log.e(TAG, s)
    }

}
