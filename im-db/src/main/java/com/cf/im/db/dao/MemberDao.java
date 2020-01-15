package com.cf.im.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.cf.im.db.domain.MemberBean;

import java.util.List;

@Dao
public interface MemberDao extends IDao<MemberBean> {

    @Query("select * from memberbean")
    List<MemberBean> getAll();

    @Query("DELETE FROM memberbean")
    void clearAll();

    @Query("select * from memberbean where uid in (:uids)")
    List<MemberBean> queryByIds(Long[] uids);

}
