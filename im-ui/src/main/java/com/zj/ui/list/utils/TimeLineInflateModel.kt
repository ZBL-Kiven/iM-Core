package com.zj.ui.list.utils

import android.content.Context
import com.zj.ui.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object TimeLineInflateModel {

    fun inflateTimeLine(ctx: Context, dataTime: Long, lastTime: Long, maxDiffTimeStamp: Long): String? {
        return if (abs(lastTime - dataTime) > maxDiffTimeStamp) {
            getTimeString(ctx, dataTime)
        } else null
    }

    fun getTimeString(ctx: Context, timestamp: Long?): String {
        val result: String
        val weekNames = arrayOf(ctx.getString(R.string.sunday), ctx.getString(R.string.monday), ctx.getString(R.string.tuesday), ctx.getString(R.string.wednesday), ctx.getString(R.string.thursday), ctx.getString(R.string.friday), ctx.getString(R.string.saturday))
        val hourTimeFormat = ctx.getString(R.string.hour_time_format)
        val monthTimeFormat = ctx.getString(R.string.month_time_format)
        val yearTimeFormat = ctx.getString(R.string.year_time_format)
        try {
            val todayCalendar = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp!!

            if (todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                result = if (todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                    when (todayCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) {
                        0 -> getTime(timestamp, hourTimeFormat)
                        1 -> "${ctx.getString(R.string.yesterday)} ${getTime(timestamp, hourTimeFormat)}"
                        2, 3, 4, 5, 6 -> {
                            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                            "${weekNames[dayOfWeek - 1]} ${getTime(timestamp, hourTimeFormat)}"
                        }
                        else -> getTime(timestamp, monthTimeFormat)
                    }
                } else {
                    getTime(timestamp, monthTimeFormat)
                }
            } else {
                result = getTime(timestamp, yearTimeFormat)
            }
            return result
        } catch (e: Exception) {
            return ""
        }

    }

    private fun getTime(time: Long, pattern: String): String {
        val date = Date(time)
        return dateFormat(date, pattern)
    }

    private fun dateFormat(date: Date, pattern: String): String {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }

}