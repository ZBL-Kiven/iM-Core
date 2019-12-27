package com.zj.im_model.mod

import com.google.gson.annotations.SerializedName

import io.realm.RealmObject

/**
 * Project:  SunPeople-Android
 * Filename: Preference.java
 *
 * Created by YangZeFeng on 2016/8/30.
 * Copyright (c) 2016. cityfruit. All rights reserved.
 */
open class Preference : RealmObject() {
    //是否提醒
    @SerializedName("notification")
    var notification: String? = null

    //是否隐藏
    @SerializedName("hide")
    var hide: String? = null

    //是否🔝
    @SerializedName("pin")
    var pin: String? = null

    //隐藏时的时间, 如果有新消息大于这个时间需要设置为不隐藏
    @SerializedName("hide_ts")
    var hideTs: Long = 0
}