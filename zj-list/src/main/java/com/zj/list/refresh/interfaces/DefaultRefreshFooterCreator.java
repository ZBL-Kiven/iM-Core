package com.zj.list.refresh.interfaces;

import android.content.Context;
import androidx.annotation.NonNull;
import com.zj.list.refresh.views.RefreshLayout;

/**
 * 默认Footer创建器
 * Created by ZJJ  on 2018/1/26.
 */

public interface DefaultRefreshFooterCreator {
    @NonNull
    RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout);
}
