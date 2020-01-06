package com.zj.imcore;

import com.zj.imcore.utils.unity.DateUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UnitTest {

    public static void main(String[] args) {

    }

    // test msg create/update msgs format
    @Test
    public void TimeTransfer() {
        String timeStr = "2020-01-03T09:38:11Z";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date s = DateUtils.getDate("yyyy-MM-dd'T'HH:mm:ss'Z'", timeStr);
        assert s != null;
        System.out.println(s.getTime());
        System.out.println(format.format(s));
    }


}
