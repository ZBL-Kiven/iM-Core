package com.zj.imcore.permission;

import androidx.annotation.Nullable;

public class RequestPermission {

    /**
     * 权限
     */
    public final String permission;

    /**
     * 是否强制要求权限，如果不是强制要求，点击拒绝也可以进行下一步
     */
    public final boolean mandatory;

    /**
     * 请求权限
     *
     * @param permission 权限
     * @param mandatory  是否强制要求；如果false（不是强制）点击拒绝也可以进行下一步操作
     */
    public RequestPermission(@Nullable String permission, boolean mandatory) {
        this.permission = permission;
        this.mandatory = mandatory;
    }

    /**
     * 请求权限
     *
     * @param permission 权限
     */
    public RequestPermission(String permission) {
        this(permission, true);
    }
}
