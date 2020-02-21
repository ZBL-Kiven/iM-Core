package com.zj.imcore.yj.base.adapter;

import androidx.annotation.LayoutRes;

/**
 * 简易适配器
 *
 * @author yangji
 */
public abstract class BaseSimpleAdapter<Bean> extends BaseAdapter<Bean> {

    private final int layoutRes;

    public BaseSimpleAdapter(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    @Override
    protected int getLayoutRes(int viewType) {
        return layoutRes;
    }

}
