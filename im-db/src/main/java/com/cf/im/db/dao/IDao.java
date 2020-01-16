package com.cf.im.db.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

public interface IDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(T t);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<T> entity);

    @Delete()
    void delete(T t);

    @RawQuery
    List<T> queryBySql(SupportSQLiteQuery sql);
}
