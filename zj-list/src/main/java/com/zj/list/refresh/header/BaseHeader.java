package com.zj.list.refresh.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.zj.list.refresh.interfaces.RefreshHeader;
import com.zj.list.refresh.internal.InternalAbstract;

/**
 * Created by zhaojie on 2018/4/2.
 * <p>
 * the custom header bases
 */

@SuppressWarnings("unused")
public abstract class BaseHeader extends InternalAbstract implements RefreshHeader {


    public BaseHeader(@NonNull View wrapper) {
        super(wrapper);
        initView(wrapper.getContext());
    }

    public BaseHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected abstract void initView(Context context);
}
