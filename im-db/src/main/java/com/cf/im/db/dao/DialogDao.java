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
    List<DialogBean> queryDialogByIds(String... dialogId);

    /**
     * 根据dialogId 获取dialog 信息
     *
     * @param dialogId dialogId
     * @return 会话信息
     */
    @Transaction
    @Query("select * from dialogbean where dialogId = :dialogId limit 1")
    DialogBean queryById(String dialogId);

    /**
     * 根据dialogId 获取dialog 信息
     *
     * @param tmId tmid
     * @return 会话信息
     */
    @Transaction
    @Query("select * from dialogbean where tmId = :tmId limit 1")
    DialogBean queryByTmId(String tmId);

    /**
     * 查询所有会话列表
     *
     * @return 会话列表
     */
    @Transaction
    @Query("select * from dialogbean")
    List<DialogBean> queryAll();

    /**
     * 查询会话
     *
     * @param type group 群组、p2p 个人
     * @return
     */
    @Transaction
    @Query("select * from dialogbean where type = :type")
    List<DialogBean> queryDialogByType(String type);

}
