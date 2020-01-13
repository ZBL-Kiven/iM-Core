package com.zj.list.multiable;

import androidx.annotation.Nullable;

import java.util.List;

interface AdapterDataSet<T> {

    @Nullable
    List<T> onBuildData(List<T> data);

    void onSourceSet(List<T> data, String name);

    void onDataSet(T data, Object payloads);

    void onDataChanged(List<T> data);

    void onDataCleared();
}
