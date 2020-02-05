package com.zj.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by ZJJ on 2019/12/11.
 */

@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

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

    public void initBase() {
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    protected void callRefresh() {

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
        setContentView(getContentId());
        initBase();
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
    protected <T extends View> T find(int id) {
        return (T) findViewById(id);
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
        if (weakReference != null) weakReference.clear();
        weakReference = null;
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
