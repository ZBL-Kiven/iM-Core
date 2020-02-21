package com.zj.imcore.yj.base.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.zj.imcore.R;


/**
 * 头像选择器
 *
 * @author yangji
 */
public abstract class BaseDialog extends DialogFragment {

    private ViewGroup rootView;

    protected <T extends View> T findViewById(@IdRes int resId) {
        return rootView.findViewById(resId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        rootView = (ViewGroup) inflater.inflate(R.layout.app_base_dialog_menu, container, false);
        inflater.inflate(getLayoutRes(), rootView, true);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // 一定要设置Background，如果不设置，window属性设置无效
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initData();
    }

    public void show(@NonNull FragmentActivity activity) {
        super.show(activity.getSupportFragmentManager(), getClass().getName());
    }

    public void show(@NonNull Fragment fragment) {
        super.show(fragment.getChildFragmentManager(), getClass().getName());
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
    }

    /**
     * 获取弹窗内容
     *
     * @return 弹窗内容
     */
    @LayoutRes
    protected abstract int getLayoutRes();

    /**
     * 初始化 event
     */
    protected abstract void initListener();

    /**
     * 加载UI
     */
    protected abstract void initView();

    /**
     * 加载内容
     */
    protected abstract void initData();
}
