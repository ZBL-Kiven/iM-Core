package com.cf.im.db.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

public interface IDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T t);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<T> entity);

    @Update
    void update(T t);

    @Delete()
    void delete(T t);
}
