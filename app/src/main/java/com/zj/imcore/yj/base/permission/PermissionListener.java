package com.zj.imcore.yj.base.permission;

import java.util.List;

public interface PermissionListener {
    void onSuccess();

    void failed(List<ResponsePermission> permission);
}
