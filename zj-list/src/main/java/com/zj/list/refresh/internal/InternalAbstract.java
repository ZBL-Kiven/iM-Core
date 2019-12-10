package com.zj.list.refresh.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.zj.list.refresh.constance.RefreshState;
import com.zj.list.refresh.constance.SpinnerStyle;
import com.zj.list.refresh.interfaces.RefreshInternal;
import com.zj.list.refresh.interfaces.RefreshKernel;
import com.zj.list.refresh.interfaces.RefreshLayoutIn;
import com.zj.list.refresh.views.RefreshLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Internal 初步实现
 * 实现 Header 和 Footer 时继承 InternalAbstract 的话可以少写很多接口方法
 * Created by ZJJ  on 2018/2/6.
 */

public abstract class InternalAbstract extends RelativeLayout implements RefreshInternal {

    protected View mWrapperView;
    protected SpinnerStyle mSpinnerStyle;

    public InternalAbstract(@NonNull View wrapper) {
        this(wrapper.getContext(), null, 0);
        this.mWrapperView = wrapper;
    }

    public InternalAbstract(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InternalAbstract(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @NonNull
    public View getView() {
        return mWrapperView == null ? this : mWrapperView;
    }

    /**
     * @param success 数据是否成功刷新或加载
     * @return 延迟弹回时间
     */
    @Override
    public int onFinish(@NonNull RefreshLayoutIn refreshLayout, boolean success) {
        if (mWrapperView instanceof RefreshInternal) {
            return ((RefreshInternal) mWrapperView).onFinish(refreshLayout, success);
        }
        return 0;
    }

    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int... colors) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).setPrimaryColors(colors);
        }
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        if (mSpinnerStyle != null) {
            return mSpinnerStyle;
        }
        if (mWrapperView instanceof RefreshInternal) {
            return ((RefreshInternal) mWrapperView).getSpinnerStyle();
        }
        if (mWrapperView != null) {
            ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
            if (params instanceof RefreshLayout.RefreshLayoutParams) {
                mSpinnerStyle = ((RefreshLayout.RefreshLayoutParams) params).spinnerStyle;
                if (mSpinnerStyle != null) {
                    return mSpinnerStyle;
                }
            }
            if (params != null) {
                if (params.height == 0 || params.height == MATCH_PARENT) {
                    return mSpinnerStyle = SpinnerStyle.Scale;
                }
            }
        }
        return mSpinnerStyle = SpinnerStyle.Translate;
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onInitialized(kernel, height, maxDragHeight);
        } else if (mWrapperView != null) {
            ViewGroup.LayoutParams params = mWrapperView.getLayoutParams();
            if (params instanceof RefreshLayout.RefreshLayoutParams) {
                kernel.requestDrawBackgroundFor(this, ((RefreshLayout.RefreshLayoutParams) params).backgroundColor);
            }
        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return mWrapperView instanceof RefreshInternal && ((RefreshInternal) mWrapperView).isSupportHorizontalDrag();
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onHorizontalDrag(percentX, offsetX, offsetMax);
        }
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onMoving(isDragging, percent, offset, height, maxDragHeight);
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayoutIn refreshLayout, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onReleased(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayoutIn refreshLayout, int height, int maxDragHeight) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onStartAnimator(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (mWrapperView instanceof RefreshInternal) {
            ((RefreshInternal) mWrapperView).onStateChanged(refreshLayout, oldState, newState);
        }
    }
}
