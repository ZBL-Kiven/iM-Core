package com.cf.im.db.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    //2020-01-03T09:38:11Z
    public final static String TIME_FORMAT_UTC_YMD_T_HMS = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Nullable
    public static Date getDate(String pattern, String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static long dateToLong(String format, String timeStr) {
        if (timeStr == null) {
            return 0L;
        }
        Date date = getDate(format, timeStr);
        return date == null ? 0L : date.getTime();
    }


    public static long getTime(long time, String... timeStr) {
        if (time > 0) {
            return time;
        }
        if (timeStr == null) {
            Log.e("DB___", "生成了时间");
            return System.currentTimeMillis();
        }
        for (String s : timeStr) {
            long timeTs = dateToLong(TIME_FORMAT_UTC_YMD_T_HMS, s);
            if (timeTs > 0) {
                return timeTs;
            }
        }
        return System.currentTimeMillis();
    }
}
