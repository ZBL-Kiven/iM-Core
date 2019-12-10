package com.zj.list.refresh.listeners;

import androidx.annotation.NonNull;
import com.zj.list.refresh.constance.RefreshState;
import com.zj.list.refresh.views.RefreshLayout;

/**
 * 刷新状态改变监听器
 * Created by ZJJ  on 2017/5/26.
 */

public interface OnStateChangedListener {
    /**
     * 状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState);
}
