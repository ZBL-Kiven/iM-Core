package com.zj.base.utils.storage.sp;

import android.content.Context;

@SuppressWarnings({"unused", "SameParameterValue"})
class SPHelper {
    static <T> T get(String key, T defaultValue) {
        return Preference.get(key, defaultValue);
    }

    static <T> void put(String key, T t) {
        Preference.put(key, t);
    }

    static void clear() {
        Preference.clear();
    }

    static void init(String name, Context context) {
        Preference.init(name, context);
    }
}
