package com.zj.imcore.utils.unity;

import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static ThreadLocal<SimpleDateFormat> local = new ThreadLocal<>();

    @Nullable
    public static Date getDate(String partten, String timeStr) {
        if (timeStr == null) return null;
        SimpleDateFormat sdf = local.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat(partten, Locale.getDefault());
            local.set(sdf);
        }
        Date d = null;
        try {
            d = sdf.parse(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}

