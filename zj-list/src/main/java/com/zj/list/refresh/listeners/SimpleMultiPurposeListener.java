package com.zj.list.refresh.listeners;

import androidx.annotation.NonNull;
import com.zj.list.refresh.constance.RefreshState;
import com.zj.list.refresh.interfaces.RefreshFooter;
import com.zj.list.refresh.interfaces.RefreshHeader;
import com.zj.list.refresh.interfaces.RefreshLayoutIn;
import com.zj.list.refresh.views.RefreshLayout;

/**
 * 多功能监听器
 * Created by ZJJ  on 2017/5/26.
 */

public class SimpleMultiPurposeListener implements OnMultiPurposeListener {

    @Override
    public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {

    }

    @Override
    public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int headerHeight, int maxDragHeight) {

    }

    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayoutIn refreshLayoutIn) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayoutIn refreshLayoutIn) {

    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }

}
