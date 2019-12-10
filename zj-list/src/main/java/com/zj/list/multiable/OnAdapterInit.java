package com.zj.list.multiable;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.zj.list.multiable.holder.MultiHolder;

import java.util.List;

@SuppressWarnings("unused")
public interface OnAdapterInit<T extends MultiAbleData> {

    View onCreateView(@NonNull ViewGroup parent, int viewType);

    void initData(View itemView, T data, @Nullable List<Object> payloads);

}
