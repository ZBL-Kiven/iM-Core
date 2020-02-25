package com.zj.base.utils.storage.sp;

import com.zbl.processor.handler.annotate.SPBinding;

@SuppressWarnings("unused")
@SPBinding()
class SPUtils {
    public static String accessToken;
    public static String refreshToken;
    public static String userName;
    public static String userTel;
    public static String userEmail;
    public static String userNote;
    public static String userId;
    public static String tmId;
    public static String userAvatar;
    public static String userGender;
    public static String userAddress;
    public static String userCountry;
    public static String userCreateTs;
    public static String userUpdateTs;
    public static Long expiresIn;

    public static Long dialogSyncSince;
}
