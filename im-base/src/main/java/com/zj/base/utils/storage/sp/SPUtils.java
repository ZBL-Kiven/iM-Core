package com.zj.base.utils.storage.sp;

import com.zbl.processor.handler.annotate.SPBinding;

@SuppressWarnings("unused")
@SPBinding()
class SPUtils {
    public static String accessToken;
    public static String refreshToken;
    public static String loginInfo;
    public static String curTeamId;
    public static Long expiresIn;
    public static String userId;
    public static Long dialogSyncSince;
}
