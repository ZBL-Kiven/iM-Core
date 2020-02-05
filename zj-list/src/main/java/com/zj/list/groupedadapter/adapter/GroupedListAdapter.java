package com.zj.list.groupedadapter.adapter;

import android.content.Context;
import com.zj.list.groupedadapter.proctol.GroupedData;
import com.zj.list.holders.BaseViewHolder;

import java.util.List;

/**
 * This is an ordinary grouping adapter. Each group has a head, tail, and children.
 */
@SuppressWarnings("unused")
public abstract class GroupedListAdapter<R, T extends GroupedData<R>> extends GroupedRecyclerViewAdapter<T> {

    public GroupedListAdapter(Context context) {
        super(context);
    }

    public abstract int getHeaderResId(int viewType);

    public abstract int getFooterResId(int viewType);

    public abstract int getChildResId(int viewType);

    public abstract void bindHeader(BaseViewHolder holder, T t, int pos);

    public abstract void bindFooter(BaseViewHolder holder, T t, int pos);

    public abstract void bindChild(BaseViewHolder holder, R r, int pos);

    @Override
    public int getGroupCount() {
        return getData() == null ? 0 : getData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<R> children = getData().get(groupPosition).getChild();
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return true;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return getHeaderResId(viewType);
    }

    @Override
    public int getFooterLayout(int viewType) {
        return getFooterResId(viewType);
    }

    @Override
    public int getChildLayout(int viewType) {
        return getChildResId(viewType);
    }

    @Override
    public final void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        if (getData() != null && getData().size() > groupPosition) {
            T entity = getData().get(groupPosition);
            bindHeader(holder, entity, groupPosition);
        }
    }

    @Override
    public final void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        T entity = getData().get(groupPosition);
        bindFooter(holder, entity, groupPosition);
    }

    @Override
    public final void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        R entity = getData().get(groupPosition).getChild().get(childPosition);
        bindChild(holder, entity, childPosition);
    }
}
