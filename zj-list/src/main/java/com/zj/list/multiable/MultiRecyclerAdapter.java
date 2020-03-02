package com.zj.list.multiable;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.zj.list.multiable.holder.MultiHolder;
import java.util.List;

@SuppressWarnings("unused")
public abstract class MultiRecyclerAdapter<T extends MultiAbleData<T>> extends RecyclerView.Adapter<MultiHolder> implements AdapterDataSet<T>, OnAdapterInit<T> {

    private MultiDataSource<T, MultiRecyclerAdapter<T>> multiDataSource;

    public MultiRecyclerAdapter() {
        multiDataSource = new MultiDataSource<>(this);
    }

    public final MultiDataSource<T, MultiRecyclerAdapter<T>> data() {
        return multiDataSource;
    }

    public List<T> getData() {
        return multiDataSource.getData();
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
        initData(holder.itemView, getData().get(position), position, payloads);
    }

    @Override
    public final int getItemCount() {
        return multiDataSource.getSize();
    }


    @Override
    public final RecyclerView.Adapter<?> getAdapter() {
        return this;
    }

    public void clear() {
        multiDataSource.clearAll();
    }
}
