package com.cf.im.db.databases;

import android.content.Context;

public class DB {

    public static BaseSingleton<DB> singleton = new BaseSingleton<DB>() {
        @Override
        protected DB create() {
            return new DB();
        }
    };

    private Context mContext;

    private DB() {
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public Context getContext() {
        return mContext;
    }
}
