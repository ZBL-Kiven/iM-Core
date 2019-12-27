package com.zj.list.refresh.interfaces;

import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;

/**
 * 刷新内容组件
 * Created by ZJJ  on 2017/5/26.
 */
public interface RefreshContent {

    @NonNull
    View getView();
    @NonNull
    View getScrollableView();
    void onActionDown(MotionEvent e);
    void setUpComponent(RefreshKernel kernel, View fixedHeader, View fixedFooter);
    void setScrollBoundaryDecider(ScrollBoundaryDecider boundary);
    void setEnableLoadMoreWhenContentNotFull(boolean enable);
    void moveSpinner(int spinner);
    boolean canRefresh();
    boolean canLoadMore();

    AnimatorUpdateListener scrollContentWhenFinished(int spinner);
}