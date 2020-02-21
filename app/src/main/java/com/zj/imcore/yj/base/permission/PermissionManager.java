package com.zj.imcore.yj.base.permission;


import android.app.Dialog;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class PermissionManager {

    private PermissionFragment mFragment;

    private RequestPermission[] mPermissions;

    public PermissionManager(FragmentActivity activity) {
        mFragment = RequestFragment.injectIfNeededIn(activity, PermissionFragment.class);
    }

    public PermissionManager(View view) {
        mFragment = RequestFragment.injectIfNeededIn(view.getContext(), PermissionFragment.class);
    }

    public PermissionManager(Dialog dialog) {
        mFragment = RequestFragment.injectIfNeededIn(dialog.getOwnerActivity(), PermissionFragment.class);
    }

    public PermissionManager(Fragment fragment) {
        mFragment = RequestFragment.injectIfNeededIn(fragment, PermissionFragment.class);
    }

    public PermissionManager request(String... permissions) {
        RequestPermission[] beans = new RequestPermission[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            beans[i] = new RequestPermission(permissions[i], true);
        }
        this.mPermissions = beans;
        return this;
    }

    public PermissionManager request(RequestPermission... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public void exec(PermissionListener listener) {
        mFragment.setListener(listener);
        mFragment.requestPermissions(mPermissions);
    }
}
