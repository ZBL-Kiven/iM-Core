package com.zj.preview

import java.util.*


internal fun <T> runWithTryCatch(block: () -> T?): T? {
    return try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

internal fun getDuration(mediaDuration: Long): String {
    val duration = mediaDuration / 1000
    val minute = duration / 60
    val second = duration % 60
    return String.format(Locale.getDefault(), "${if (minute < 10) "0%d" else "%d"}:${if (second < 10) "0%d" else "%d"}", minute, second)
}
