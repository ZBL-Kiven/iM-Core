package com.zj.list.multiable;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

interface AdapterDataSet<T> {

    RecyclerView.Adapter<?> getAdapter();

    @Nullable
    List<T> onBuildData(List<T> data);
}
