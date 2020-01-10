package com.cf.im.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.cf.im.db.domain.MessageBean;

import java.util.List;

@Dao
public interface MessageDao extends IDao<MessageBean> {

    @Query("select * from messagebean where id = :id")
    MessageBean queryByIds(String id);

    /**
     * 通过会话窗口id 查询信息（非离线消息）列表
     *
     * @param dialogId  会话窗口Id
     * @param kId       数据库自增Id
     * @param limit     数量
     * @param orderDesc 排序方式
     * @return 消息列表
     */
    @Query("select * from messagebean where dialogId = :dialogId and kId = :kId order by localCreateTs desc")
    List<MessageBean> queryMessageBy(long dialogId, int kId, int limit, boolean orderDesc);

    /**
     * 查询所有离线消息
     *
     * @return 离线消息列表
     */
    List<MessageBean> queryMessageByOffline();

    /**
     * 通过会话窗口Id 查询离线消息列表
     *
     * @param dialogId 会话窗口Id
     * @return 离线消息列表
     */
    List<MessageBean> queryMessageByOfflineAndDialogId(long dialogId);

    /**
     * 通过callId 或者 serviceId 查询消息主键Id
     *
     * @param callId    自发消息Id
     * @param serviceId 服务器消息Id
     * @return 消息主键Id（自增Id）
     */
    @Query("select * from messagebean where callId = :callId or id = :serviceId")
    int queryKIdByIdOrCallId(String callId, long serviceId);

    /**
     * 通过消息的主键Id 修改数据
     *
     * @param bean 消息对象
     */
    @Update
    void update(MessageBean bean);

    /**
     * 通过主键Id 删除数据
     *
     * @param kId 主键Id s
     */
    @Query("DELETE FROM messagebean where kId = :kId")
    void remove(int kId);
}
