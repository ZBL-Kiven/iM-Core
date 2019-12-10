package com.zj.list.refresh.impl;

import android.annotation.SuppressLint;
import android.view.View;
import com.zj.list.refresh.interfaces.RefreshHeader;
import com.zj.list.refresh.internal.InternalAbstract;

/**
 * 刷新头部包装
 * Created by ZJJ  on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader/*, InvocationHandler*/ {


    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }
}
