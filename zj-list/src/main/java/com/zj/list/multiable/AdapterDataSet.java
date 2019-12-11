package com.zj.list.multiable;

import androidx.annotation.Nullable;

import java.util.List;

interface AdapterDataSet<T> {

    @Nullable
    List<T> onBuildData(List<T> data);

    void onSourceSet(String name);

    void onDataInserted(int position);

    void onDataSet(int position, Object payloads);

    void onDataRemoved(int position);

    void onDataRangeRemoved();

    void onDataCleared();

    void onDataRangeInserted(int start, int end, int count);
}
