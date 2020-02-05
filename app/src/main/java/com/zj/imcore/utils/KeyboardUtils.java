/**
 * Project:  Nagini
 * Filename: KeyboardUtils.java
 * <p>
 * Created by Kyno on 8/11/16.
 * Copyright (c) 2016. Bearyinnovative. All rights reserved.
 */
package com.zj.imcore.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入键盘工具类
 * showKeyBoard： 显示键盘
 * closeKeyBoard： 关闭键盘
 * isKeyBoardShowing： 键盘是否显示
 */
public class KeyboardUtils {
    public static void showKeyBoard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void closeKeyBoard(Context context, IBinder ibinder) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ibinder, 0);
    }

    public static boolean isKeyBoardShowing(Context context) {
        return ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .isActive();
    }

    public static void closeKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
