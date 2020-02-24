package com.zj.imcore.yj.base.permission;

public class ResponsePermission {
    /**
     * 是否被永久拒绝
     */
    public boolean permanentRefused;

    /**
     * 当前权限
     */
    public String permission;

    public ResponsePermission(String permission, boolean permanentRefused) {
        this.permission = permission;
        this.permanentRefused = !permanentRefused;
    }
}
