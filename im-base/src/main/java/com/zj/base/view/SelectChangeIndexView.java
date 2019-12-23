package com.zj.base.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by ZJJ on 2018/7/17.
 */

public class SelectChangeIndexView extends OnSelectedChangeAnimParent {
    public SelectChangeIndexView(@NonNull Context context) {
        this(context, null, 0);
    }

    public SelectChangeIndexView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectChangeIndexView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void init() {
        if (getChildCount() == 1 && getChildAt(0) instanceof DrawableTextView)
            initData(getChildAt(0), 300, (animationView, fraction) -> ((DrawableTextView) getChildAt(0)).initData(fraction));
    }
}
