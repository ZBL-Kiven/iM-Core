package com.cf.im.db.databases;

/**
 * 单例工具类
 *
 * @author yangji
 */
public abstract class BaseSingleton<T> {
    private volatile T mInstance;

    protected abstract T create();

    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }

    public void exit() {
        mInstance = null;
    }
}