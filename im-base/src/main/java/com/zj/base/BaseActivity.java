package com.zj.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.zj.base.view.BaseTitleView;
import com.zj.loading.BaseLoadingView;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by ZJJ on 2019/12/11.
 */

@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

    public View rootView, titleLine;
    private BaseLoadingView blvLoading;
    private FrameLayout flContent;
    private BaseTitleView baseTitleView;
    protected WeakReference<BaseActivity> weakReference;
    private static final int MSG_PERMISSION = 0x01a9;

    private Lifecycle lifecycle;

    private enum Lifecycle {

        CREATE(-3), START(-2), RESUME(-1), PAUSE(1), STOP(2), DESTROY(3);
        int level;

        Lifecycle(int l) {
            this.level = l;
        }
    }

    protected abstract int getContentId();

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    protected void initTitleBar(BaseTitleView baseTitleView) {

    }

    protected void callRefresh() {

    }

    protected void showTitleBar(boolean show) {
        if (show && baseTitleView != null) {
            baseTitleView.setVisibility(View.VISIBLE);
            initTitleBar(baseTitleView);
        } else {
            if (baseTitleView != null) {
                baseTitleView.setVisibility(View.GONE);

            }
        }
    }

    protected void showTitleLine(boolean show) {
        if (show && baseTitleView != null) {
            titleLine.setVisibility(View.VISIBLE);
        } else {
            titleLine.setVisibility(View.INVISIBLE);
        }
    }

    public BaseTitleView getBaseTitleView() {
        return baseTitleView;
    }

    protected void setTitle(String s) {
        baseTitleView.setTitle(s + "");
    }

    public BaseLoadingView getBlvLoading() {
        return blvLoading;
    }

    /**
     * set loading
     */
    public void hint(final BaseLoadingView.DisplayMode mode, final String hint, final boolean showingOnActive) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            blvLoading.setMode(mode, hint, showingOnActive);
        } else {
            runOnUiThread(() -> blvLoading.setMode(mode, hint, showingOnActive));
        }
    }

    protected WeakReference<BaseActivity> getWeakContext() {
        if (weakReference == null) weakReference = new WeakReference<>(this);
        return weakReference;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycle = Lifecycle.CREATE;
        View v = LayoutInflater.from(getWeakContext().get()).inflate(R.layout.activity_base, null, false);
        setContentView(v);
        initBaseView();
    }

    private void initBaseView() {
        flContent = f(R.id.base_flContent);
        blvLoading = f(R.id.base_blvLoading);
        baseTitleView = f(R.id.base_title);
        titleLine = f(R.id.base_titleLine);
        initTitleBar(baseTitleView);
        blvLoading.setRefreshListener(this::callRefresh);
        rootView = LayoutInflater.from(getWeakContext().get()).inflate(getContentId(), flContent, true);
        initView();
        initData();
        initListener();
    }

    /**
     * cheek your App Permissions
     *
     * @param permissions the permissions with you wanna get,use @see PermissionTools.PermissionManifest
     */
    public void cheekSelfPermission(Map<String, Boolean> permissions/*, @NonNull OnPermissionCheckListener listener*/) {
//        permissionTools.setPermissions(permissions).checkPermissions(listener);
    }

    public void cheekSelfPermission(String[] permissions, boolean[] forces/*, @NonNull OnPermissionCheckListener listener*/) {
//        permissionTools.setPermissions(permissions, forces).checkPermissions(listener);
    }

    public void cheekSelfPermission(boolean force, String[] permissions/*, @NonNull OnPermissionCheckListener listener*/) {
//        permissionTools.setPermission(force, permissions).checkPermissions(listener);
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T f(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T find(int id) {
        return (T) rootView.findViewById(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        cheekPermissionWithHandlerDelay(requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        permissionTools.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        lifecycle = Lifecycle.DESTROY;
        weakReference.clear();
        weakReference = null;
        flContent.clearDisappearingChildren();
        flContent.removeAllViews();
        if (rootView instanceof ViewGroup) {
            ((ViewGroup) rootView).removeAllViews();
        }
        rootView = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycle = Lifecycle.RESUME;
    }

    @Override
    protected void onPause() {
        lifecycle = Lifecycle.PAUSE;
        super.onPause();
    }

    @Override
    protected void onStart() {
        lifecycle = Lifecycle.START;
        super.onStart();
    }

    @Override
    protected void onStop() {
        lifecycle = Lifecycle.STOP;
        super.onStop();
    }

    public boolean isStart() {
        return lifecycle.level >= Lifecycle.START.level && lifecycle.level <= Lifecycle.STOP.level;
    }

    public boolean isDestroy() {
        return lifecycle == Lifecycle.DESTROY;
    }

    public boolean isResume() {
        return lifecycle == Lifecycle.RESUME;
    }

    public boolean isPause() {
        return lifecycle.level >= Lifecycle.PAUSE.level;
    }

    public boolean isStop() {
        return lifecycle.level >= Lifecycle.STOP.level;
    }

    public boolean isCreate() {
        return lifecycle.level > Lifecycle.CREATE.level && lifecycle.level < Lifecycle.DESTROY.level;
    }

}
