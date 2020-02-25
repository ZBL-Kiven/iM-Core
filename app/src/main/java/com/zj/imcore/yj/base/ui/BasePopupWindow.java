package com.zj.imcore.yj.base.ui;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

/**
 * @author yangji
 */
public abstract class BasePopupWindow extends ContextWrapper {

    private PopupWindow popupWindow;
    private View contentView;

    public BasePopupWindow(Context context) {
        super(context);
        init();
        initView();
        initListener();
        initData();
    }

    private void init() {
        popupWindow = new PopupWindow(getBaseContext());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        this.contentView = inflater.inflate(getLayoutRes(), null);
        popupWindow.setContentView(contentView);
    }

    protected <T extends View> T findViewById(@IdRes int idRes) {
        return contentView.findViewById(idRes);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    public void showAsDropDown(View anchor) {
        popupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        popupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        popupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public void showAsDropDown(View anchor, int gravity) {
        popupWindow.showAsDropDown(anchor, 0, 0, gravity);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        popupWindow.showAtLocation(parent, gravity, x, y);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }


}
