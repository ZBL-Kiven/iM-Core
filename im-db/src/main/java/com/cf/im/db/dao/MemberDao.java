package com.cf.im.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.cf.im.db.domain.MemberBean;

import java.util.List;

@Dao
public interface MemberDao extends IDao<MemberBean> {

    @Query("select * from memberbean")
    List<MemberBean> getAll();

    @Transaction
    @Query("DELETE FROM memberbean")
    void clearAll();

    @Query("select * from memberbean where uid in (:uids)")
    List<MemberBean> queryByIds(long... uids);

    @Query("select * from memberbean where uid = :uids")
    MemberBean queryById(long uids);

    @Query("select * from memberbean where dialogId = :dialogId")
    MemberBean queryByDialogId(long dialogId);

    @Query("select * from memberbean where dialogId in (:dialogId)")
    List<MemberBean> queryByDialogIds(long... dialogId);

}
