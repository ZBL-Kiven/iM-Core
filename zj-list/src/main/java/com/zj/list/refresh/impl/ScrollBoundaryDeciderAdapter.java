package com.zj.list.refresh.impl;

import android.graphics.PointF;
import android.view.View;
import com.zj.list.refresh.interfaces.ScrollBoundaryDecider;
import com.zj.list.refresh.util.ScrollBoundaryUtil;

/**
 * 滚动边界
 * Created by ZJJ  on 2017/7/8.
 */

@SuppressWarnings("WeakerAccess")
public class ScrollBoundaryDeciderAdapter implements ScrollBoundaryDecider {

    //<editor-fold desc="Internal">
    public PointF mActionEvent;
    public ScrollBoundaryDecider boundary;
    public boolean mEnableLoadMoreWhenContentNotFull = true;

    @Override
    public boolean canRefresh(View content) {
        if (boundary != null) {
            return boundary.canRefresh(content);
        }
        return ScrollBoundaryUtil.canRefresh(content, mActionEvent);
    }

    @Override
    public boolean canLoadMore(View content) {
        if (boundary != null) {
            return boundary.canLoadMore(content);
        }
        return ScrollBoundaryUtil.canLoadMore(content, mActionEvent, mEnableLoadMoreWhenContentNotFull);
    }
}
