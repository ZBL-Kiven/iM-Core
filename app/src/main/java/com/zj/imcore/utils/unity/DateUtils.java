package com.zj.imcore.utils.unity;

import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    @Nullable
    public static Date getDate(String partten, String timeStr) {
        SimpleDateFormat sdf  = new SimpleDateFormat(partten, Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}

