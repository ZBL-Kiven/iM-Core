package com.zj.imcore.yj.base.permission;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionFragment extends RequestFragment {

    private static final int PERMISSIONS_REQUEST_CODE = 42;

    private PermissionListener listener;

    private Map<String, RequestPermission> mPermissionMap;

    public void setListener(PermissionListener listener) {
        this.listener = listener;
    }


    public void requestPermissions(RequestPermission[] permissions) {
        mPermissionMap = new HashMap<>();
        String[] str = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            str[i] = permissions[i].permission;
            mPermissionMap.put(str[i], permissions[i]);
        }
        requestPermissions(str, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSIONS_REQUEST_CODE) return;
        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
    }

    private void onRequestPermissionsResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
        List<ResponsePermission> permissionList = new ArrayList<>();
        for (int i = 0, size = permissions.length; i < size; i++) {
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                //权限被拒绝
                //判断是否是强制权限，如果不是强制要求则放过
                RequestPermission permission = mPermissionMap.get(permissions[i]);
                assert permission != null;
                if (permission.mandatory) {
                    permissionList.add(new ResponsePermission(permissions[i], shouldShowRequestPermissionRationale[i]));
                }
            }
        }
        if (listener == null) {
            return;
        }
        if (permissionList.size() > 0) {
            listener.failed(permissionList);
        } else {
            listener.onSuccess();
        }
    }

}
