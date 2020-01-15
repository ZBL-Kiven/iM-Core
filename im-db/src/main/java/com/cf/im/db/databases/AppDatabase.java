package com.cf.im.db.databases;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cf.im.db.domain.DialogBean;
import com.cf.im.db.domain.MemberBean;
import com.cf.im.db.domain.MessageBean;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(version = 1, entities = {
        MessageBean.class,
        MemberBean.class,
        DialogBean.class
}, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase implements IDatabase {

    public static BaseSingleton<IDatabase> singleton = new BaseSingleton<IDatabase>() {
        @Override
        protected IDatabase create() {
            return AppDatabase.create();
        }
    };

    //数据库配置
    private static final String DB_NAME = "cf_im2.db";

//    private static Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE memberbean "
//                    + " ADD COLUMN age INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    //写入线程
    private static final int NUMBER_OF_THREADS = 4;

    private final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private final ExecutorService databaseReadExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    @Override
    public ExecutorService getWriteExecutor() {
        return databaseWriteExecutor;
    }

    @Override
    public ExecutorService getReadExecutor() {
        return databaseReadExecutor;
    }

    private static AppDatabase create() {
        SQLiteCipherSpec cipherSpec = new SQLiteCipherSpec()  // 指定加密方式，使用默认加密可以省略
                .setPageSize(4096)
                .setKDFIteration(64000);

        WCDBOpenHelperFactory factory = new WCDBOpenHelperFactory()
                //.passphrase("passphrase".getBytes())  // 指定加密DB密钥，非加密DB去掉此行
                .cipherSpec(cipherSpec)               // 指定加密方式，使用默认加密可以省略
                .writeAheadLoggingEnabled(true)       // 打开WAL以及读写并发，可以省略让Room决定是否要打开
                .asyncCheckpointEnabled(true);        // 打开异步Checkpoint优化，不需要可以省略

        return Room.databaseBuilder(
                DB.singleton.get().getContext(),
                AppDatabase.class,
                DB_NAME)
//                .addMigrations(MIGRATION_1_2)
                //.allowMainThreadQueries() // 允许主线程执行DB操作，一般不推荐
                .openHelperFactory(factory) // 重要：使用WCDB打开Room
                .build();
    }

}
