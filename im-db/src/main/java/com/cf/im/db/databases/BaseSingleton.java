package com.cf.im.db.databases;

/**
 * 单例工具类
 *
 * @author yangji
 */
public abstract class BaseSingleton<T> {
    private volatile T mInstance;

    protected abstract T create(String... arg);

    public final T get(String... arg) {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create(arg);
            }
            return mInstance;
        }
    }

    public final synchronized void exit() {
        if (mInstance != null) {
            //这里有可能做回收操作
            mInstance = null;
        }
    }

}