package com.zj.imcore.yj.base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 或者设置app:spanCount="5"
 *
 * @author yangji
 */
public class ToolGridLayoutManager extends GridLayoutManager {

    public ToolGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ToolGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSpanCount(5);
    }

    public ToolGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

}
