package com.cf.im.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.cf.im.db.domain.MemberBean;

import java.util.List;

@Dao
public interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MemberBean entity);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<MemberBean> entity);

    @Query("select * from memberbean")
    List<MemberBean> getAll();

    @Query("DELETE FROM memberbean")
    void clearAll();

    @Update
    void update(MemberBean entity);
}
