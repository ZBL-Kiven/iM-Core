package com.cf.im.db.repositorys;

import com.cf.im.db.databases.AppDatabase;
import com.cf.im.db.databases.IDatabase;

import java.util.concurrent.ExecutorService;

public class BaseRepository {

    protected static IDatabase getDatabase() {
        return AppDatabase.singleton.get();
    }

    protected static ExecutorService getWriteExecutor() {
        return getDatabase().getWriteExecutor();
    }

    protected static ExecutorService getReadExecutor() {
        return getDatabase().getReadExecutor();
    }
}
