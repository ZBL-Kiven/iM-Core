package com.zj.list.refresh.util;

import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.zj.list.refresh.interfaces.RefreshKernel;
import com.zj.list.refresh.interfaces.RefreshLayoutIn;
import com.zj.list.refresh.listeners.CoordinatorLayoutListener;

/**
 * Design 兼容包缺省尝试
 * Created by ZJJ  on 2018/1/29.
 */

public class DesignUtil {

    public static void checkCoordinatorLayout(View content, RefreshKernel kernel, CoordinatorLayoutListener listener) {
        try {//try 不能删除，不然会出现兼容性问题
            if (content instanceof CoordinatorLayout) {
                kernel.getRefreshLayout().setEnableNestedScroll(false);
                wrapperCoordinatorLayout(((ViewGroup) content), kernel.getRefreshLayout(), listener);
            }
        } catch (Throwable ignored) {
        }
    }

    private static void wrapperCoordinatorLayout(ViewGroup layout, final RefreshLayoutIn refreshLayoutIn, final CoordinatorLayoutListener listener) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if (view instanceof AppBarLayout) {
                ((AppBarLayout) view).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        listener.onCoordinatorUpdate(verticalOffset >= 0, refreshLayoutIn.isEnableLoadMore() && (appBarLayout.getTotalScrollRange() + verticalOffset) <= 0);
                    }
                });
            }
        }
    }
}
