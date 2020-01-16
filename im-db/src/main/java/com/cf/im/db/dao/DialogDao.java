package com.cf.im.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.cf.im.db.domain.DialogBean;
import com.cf.im.db.domain.impl.DialogBeanImpl;

import java.util.List;

@Dao
public interface DialogDao extends IDao<DialogBean> {

    /**
     * 根据dialogId 获取dialog 信息
     *
     * @param channelId dialogId
     * @return 会话信息
     */
    @Query("select * from dialogbean where channelID = :channelId limit 1")
    DialogBeanImpl queryById(String channelId);

    /**
     * 根据dialogId 获取dialog 信息
     *
     * @param userId 对方用户Id
     * @return 会话信息
     */
    @Query("select * from dialogbean where userId = :userId limit 1")
    DialogBeanImpl queryByUserId(String userId);

    /**
     * 查询所有会话列表
     *
     * @return 会话列表
     */
    @Query("select * from dialogbean")
    List<DialogBeanImpl> queryAll();

}
