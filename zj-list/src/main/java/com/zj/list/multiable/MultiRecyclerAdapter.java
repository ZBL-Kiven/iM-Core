package com.zj.list.multiable;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.zj.list.multiable.holder.MultiHolder;


import java.util.List;

@SuppressWarnings("unused")
public abstract class MultiRecyclerAdapter<T extends MultiAbleData<T>> extends RecyclerView.Adapter<MultiHolder> implements AdapterDataSet, OnAdapterInit<T> {

    private MultiDataSource<T, MultiRecyclerAdapter<T>> multiDataSource;

    public MultiRecyclerAdapter() {
        multiDataSource = new MultiDataSource<>(this);
    }

    public final MultiDataSource<T, MultiRecyclerAdapter<T>> data() {
        return multiDataSource;
    }

    @NonNull
    @Override
    public final MultiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = onCreateView(parent, viewType);
        return MultiHolder.create(v);
    }

    @Override
    public final void onBindViewHolder(@NonNull MultiHolder holder, int position) {
        this.onBindViewHolder(holder, position, null);
    }

    @Override
    public final void onBindViewHolder(@NonNull MultiHolder holder, int position, @Nullable List<Object> payloads) {
        initData(holder.itemView, data().getDataWithPosition(position), payloads);
    }

    @Override
    public final int getItemCount() {
        return data().getCount();
    }


    @Override
    public final void onSourceSet(String name) {
        notifyDataSetChanged();
    }

    @Override
    public void onDataInserted(int position) {
        notifyItemInserted(position);
        if (position < getItemCount()) {
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public void onDataSet(int position, Object payloads) {
        notifyItemChanged(position, payloads);
    }

    @Override
    public void onDataRemoved(int position) {
        notifyItemRemoved(position);
        if (position < getItemCount()) {
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public void onDataRangeRemoved() {
        notifyDataSetChanged();
    }


    @Override
    public void onDataCleared() {
        notifyDataSetChanged();
    }

    @Override
    public void onDataRangeInserted(int start, int end, int count) {
        notifyItemRangeInserted(start, end);
        if (end < count) {
            notifyItemRangeChanged(end, count);
        }
    }
}
