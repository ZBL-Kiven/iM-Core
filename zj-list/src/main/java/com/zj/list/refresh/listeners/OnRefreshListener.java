package com.zj.list.refresh.listeners;

import androidx.annotation.NonNull;
import com.zj.list.refresh.interfaces.RefreshLayoutIn;

/**
 * 刷新监听器
 * Created by ZJJ  on 2017/5/26.
 */

public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayoutIn refreshLayoutIn);
}
