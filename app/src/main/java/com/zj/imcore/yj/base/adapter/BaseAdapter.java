package com.zj.imcore.yj.base.adapter;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.zj.list.adapters.BaseRecyclerAdapter;

/**
 * @author yangji
 */
public abstract class BaseAdapter<Bean> extends BaseRecyclerAdapter<ViewHolder, Bean> {

    /**
     * 获取xml layoutId 用于显示
     *
     * @param viewType 类型
     * @return 获取xml layoutId
     */
    @LayoutRes
    protected abstract int getLayoutRes(int viewType);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(this, parent, getLayoutRes(viewType));
    }

}
