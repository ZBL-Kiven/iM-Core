package com.zj.imcore;

import com.zj.imcore.utils.unity.DateUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Test
    public void checkSyncs() {
        List<Integer> data = new ArrayList<>(10);
        List<Integer> curData = new ArrayList<>(10);

        //0
        data.add(1);
        curData.add(2);

//        //1
        data.add(1);
//        curData.add(2);
//
//        //2
//        data.add(2);
//        curData.add(2);
//
//        //3
//        data.add(2);
//        curData.add(2);
//
//        //4
//        data.add(2);
//        curData.add(2);
//
//        //5
//        data.add(3);
//        curData.add(3);
//
//        //6
//        data.add(5);
//        curData.add(5);
//
//        //7
//        data.add(5);
//        curData.add(5);
//
//        //8
//        data.add(5);
//        curData.add(5);
//
//        //9
//        data.add(6);
//        curData.add(6);

        syncData(data, curData);
    }

    private void syncData(List<?> data, List<?> curData) {
        int nlen = data.size();
        int olen = curData.size();
        int previousIndex = 0;
        int step = Math.max(nlen, olen);
        for (int i = 0; i < step; i++) {
            boolean eq = false;
            boolean in = i < olen && i < nlen;
            if (in) {
                eq = curData.get(i).equals(data.get(i));
            }
            boolean nextEq = false;
            boolean nextIn = i + 1 < olen && i + 1 < nlen;
            if (nextIn) {
                nextEq = curData.get(i + 1).equals(data.get(i + 1));
            }
            if (in) {
                if (nextIn) {
                    if (eq) {
                        previousIndex = i + 1;
                    } else {
                        if (nextEq) {
                            if (i - previousIndex > 0) {
                                System.out.println("test ----- 1rangeChange  ===-> p = " + previousIndex + "   i = " + i);
                            } else {
                                System.out.println("test ----- 1change  ===-> " + previousIndex);
                            }
                            previousIndex = i + 1;
                        }
                    }
                } else {
                    if(!eq) {
                        if (i - previousIndex > 0) {
                            System.out.println("test ----- 2rangeChange  ===-> p = " + previousIndex + "   i = " + i);
                        } else {
                            System.out.println("test ----- 2change  ===-> " + previousIndex);
                        }
                        previousIndex = i + 1;
                    }
                }
            } else {
                if (i == step - 1) {
                    if (i >= olen)
                        if (i - previousIndex > 0) {
                            System.out.println("test ----- rangeInsert  ===-> p = " + previousIndex + "   i = " + (i + 1));
                        } else {
                            System.out.println("test ----- insert  ===-> " + previousIndex);
                        }
                    if (i >= nlen) {
                        if (i - previousIndex > 0) {
                            System.out.println("test ----- rangeRemove  ===-> p = " + previousIndex + "   i = " + (i + 1));
                        } else {
                            System.out.println("test ----- remove  ===-> " + previousIndex);
                        }
                    }
                }
            }
        }
    }
}