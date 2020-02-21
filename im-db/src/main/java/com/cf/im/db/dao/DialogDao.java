package com.cf.im.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.cf.im.db.domain.DialogBean;

import java.util.List;

@Dao
public interface DialogDao extends IDao<DialogBean> {


    /**
     * 根据dialogId 获取dialog 信息
     *
     * @param dialogId dialogId
     * @return 会话信息
     */
    @Transaction
    @Query("select * from dialogbean where dialogId = :dialogId limit 1")
    DialogBean queryDialogById(String dialogId);

    /**
     * 根据dialogId 获取dialog 信息
     *
     * @param dialogId dialogId
     * @return 会话信息
     */
    @Transaction
    @Query("select * from dialogbean where dialogId = :dialogId limit 1")
    List<DialogBean> queryDialogByIds(long... dialogId);

    /**
     * 根据dialogId 获取dialog 信息
     *
     * @param dialogId dialogId
     * @return 会话信息
     */
    @Transaction
    @Query("select * from dialogbean where dialogId = :dialogId limit 1")
    DialogBean queryById(long dialogId);

    /**
     * 查询所有会话列表
     *
     * @return 会话列表
     */
    @Transaction
    @Query("select * from dialogbean")
    List<DialogBean> queryAll();

}
