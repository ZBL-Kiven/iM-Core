package com.zj.list.multiable.holder;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public final class MultiHolder extends RecyclerView.ViewHolder {

    private MultiHolder(@NonNull View itemView) {
        super(itemView);
    }

    @NonNull
    public static MultiHolder create(@NonNull View v) {
        return new MultiHolder(v);
    }
}
