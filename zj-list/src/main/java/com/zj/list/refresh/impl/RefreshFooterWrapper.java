package com.zj.list.refresh.impl;

import android.annotation.SuppressLint;
import android.view.View;
import com.zj.list.refresh.interfaces.RefreshFooter;
import com.zj.list.refresh.internal.InternalAbstract;

/**
 * 刷新底部包装
 * Created by ZJJ  on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter/*, InvocationHandler */{

    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return mWrapperView instanceof RefreshFooter && ((RefreshFooter) mWrapperView).setNoMoreData(noMoreData);
    }
}
