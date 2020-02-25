package com.zj.imcore.ui.main.contact;

import android.content.Context;

import com.zj.imcore.R;
import com.zj.imcore.yj.base.ui.BasePopupWindow;

/**
 * @author yangji
 */
public class MainMenuPopupWindow extends BasePopupWindow {

    public interface Listener {
        /**
         * 创建 讨论组
         */
        void openCreateGroup();
    }

    private Listener listener;

    public MainMenuPopupWindow setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public MainMenuPopupWindow(Context context) {
        super(context);
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.app_act_main_menu;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        findViewById(R.id.tv_app_act_main_menu_tv_crate_group).setOnClickListener(v -> {
            if (listener != null) {
                listener.openCreateGroup();
                dismiss();
            }
        });
    }
}
