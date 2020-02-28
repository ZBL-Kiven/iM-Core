package com.cf.im.db.dao;

import androidx.room.Query;

import com.cf.im.db.domain.MessageBean;

import java.util.List;

public interface MessageDao extends IDao<MessageBean> {
    /**
     * 通过主键Id 删除数据
     *
     * @param kId 主键Id s
     */
    @Query("DELETE FROM messagebean where kId = :kId")
    void remove(long kId);

    /**
     * 根据 主键ID 获取消息
     *
     * @param kId 主键 ID
     * @return 消息体
     */
    @Query("select * from messagebean where kId = :kId")
    MessageBean queryById(long kId);

    /**
     * 查询所有离线消息
     *
     * @return 离线消息列表
     */
    @Query("select * from messagebean")
    List<MessageBean> queryMessageByOffline();

    /**
     * 通过会话窗口Id 查询离线消息列表
     *
     * @param dialogId 会话窗口Id
     * @return 离线消息列表
     */
    @Query("select * from messagebean where dialogId = :dialogId")
    List<MessageBean> queryMessageByOfflineAndDialogId(String dialogId);

    /**
     * 通过callId 或者 serviceId 查询消息主键Id
     *
     * @param callId    自发消息Id
     * @param serviceId 服务器消息Id
     * @return 消息主键Id（自增Id）
     */
    @Query("select kId from messagebean where callId = :callId or id = :serviceId limit 1")
    int queryKIdByIdOrCallId(String callId, String serviceId);

    /**
     * 通过callId 或者 serviceId 查询消息主键Id
     *
     * @param callId    自发消息Id
     * @param serviceId 服务器消息Id
     * @return 消息主键Id（自增Id）
     */
    @Query("select * from messagebean where callId = :callId or id = :serviceId limit 1")
    MessageBean queryByIdOrCallId(String callId, String serviceId);

    /**
     * 通过callId 或者 serviceId 查询消息主键Id
     *
     * @param callId    自发消息Id
     * @param serviceId 服务器消息Id
     * @return 消息主键Id（自增Id）
     */
    @Query("select * from messagebean where callId = :callId or id = :serviceId limit 1")
    MessageBean queryIdOrCallId(String callId, String serviceId);

    @Query("select * from messagebean where dialogId = :dialogId order by localCreateTs desc limit 1")
    MessageBean queryLast(String dialogId);

    /**查询一个所以 dialog 消息的分组信息*/
    @Query("select dialogId from messageBean where teamId = :teamId")
    List<String> queryDialogIdsByMessages(String teamId);
}
