package com.cf.db.app;

import android.app.Application;

import com.cf.im.db.databases.AppDatabase;
import com.cf.im.db.databases.DB;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DB.singleton.get().init(this);
        AppDatabase.singleton.get();
    }
}
