package com.zj.list.multiable;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.zj.list.multiable.holder.MultiHolder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class MultiRecyclerAdapter<T extends MultiAbleData<T>> extends RecyclerView.Adapter<MultiHolder> implements AdapterDataSet<T>, OnAdapterInit<T> {

    private MultiDataSource<T, MultiRecyclerAdapter<T>> multiDataSource;
    private List<T> dataList;

    protected boolean isEqual(T d1, T d2) {
        return d1.equals(d2);
    }

    public MultiRecyclerAdapter() {
        multiDataSource = new MultiDataSource<>(this);
        dataList = new ArrayList<>();
    }

    public final MultiDataSource<T, MultiRecyclerAdapter<T>> data() {
        return multiDataSource;
    }

    public List<T> getData() {
        return dataList;
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
        initData(holder.itemView, dataList.get(position), position, payloads);
    }

    @Override
    public final int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onSourceSet(List<T> data, String name) {
        dataList = data;
        notifyDataSetChanged();
    }

    @Override
    public void onDataSet(T data, Object payloads) {
        int index = dataList.lastIndexOf(data);
        dataList.set(index, data);
        notifyItemChanged(index, payloads);
    }

    @Override
    public void onDataChanged(List<T> data) {
        List<T> od = new ArrayList<>(dataList);
        dataList = data;
        syncData(data, od);
    }

    @Override
    public void onDataCleared() {
        dataList.clear();
        notifyDataSetChanged();
    }

    private void syncData(List<T> data, List<T> curData) {
        int nlen = data.size();
        int olen = curData.size();
        int previousIndex = 0;
        int step = Math.max(nlen, olen);
        for (int i = 0; i < step; i++) {
            boolean eq = false;
            boolean in = i < olen && i < nlen;
            if (in) {
                eq = isEqual(curData.get(i), data.get(i));
            }
            boolean nextEq = false;
            boolean nextIn = i + 1 < olen && i + 1 < nlen;
            if (nextIn) {
                nextEq = isEqual(curData.get(i + 1), data.get(i + 1));
            }
            if (in) {
                if (nextIn) {
                    if (eq) {
                        previousIndex = i + 1;
                    } else {
                        if (nextEq) {
                            if (i - previousIndex > 0) {
                                notifyItemRangeChanged(previousIndex, i);
                            } else {
                                notifyItemChanged(previousIndex);
                            }
                            previousIndex = i + 1;
                        }
                    }
                } else {
                    if (!eq) {
                        if (i - previousIndex > 0) {
                            notifyItemRangeChanged(previousIndex, i);
                        } else {
                            notifyItemChanged(previousIndex);
                        }
                        previousIndex = i + 1;
                    }
                }
            } else {
                if (i == step - 1) {
                    if (i >= olen)
                        if (i - previousIndex > 0) {
                            notifyItemRangeInserted(previousIndex + 1, step);
                        } else {
                            notifyItemInserted(previousIndex + 1);
                        }
                    if (i >= nlen) {
                        if (i - previousIndex > 0) {
                            notifyItemRangeRemoved(previousIndex, step);
                            notifyItemRangeChanged(0, step);
                        } else {
                            notifyItemRemoved(previousIndex);
                            notifyItemRangeChanged(0, step);
                        }
                    }
                }
            }
        }
    }

    public void clear() {
        multiDataSource.clearAll();
    }
}
