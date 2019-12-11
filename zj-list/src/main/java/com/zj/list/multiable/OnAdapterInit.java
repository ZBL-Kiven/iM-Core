package com.zj.list.multiable;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public interface OnAdapterInit<T extends MultiAbleData> {

    View onCreateView(@NonNull ViewGroup parent, int viewType);

    void initData(@NonNull View itemView, T data, int position, @Nullable List<Object> payloads);

}
