package com.cf.im.db.repositorys;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.dao.impl.MessageDaoImpl;
import com.cf.im.db.databases.AppDatabase;
import com.cf.im.db.domain.MessageBean;
import com.cf.im.db.domain.impl._MessageBeanImpl;
import com.cf.im.db.listener.DBListener;

import java.util.List;

public class MessageRepository extends BaseRepository {


    private static MessageDaoImpl getMessageDao() {
        return AppDatabase.singleton.get().getMessageDao();
    }

    public static void insertOrUpdate(String json, DBListener<_MessageBeanImpl> listener) {
        getWriteExecutor().execute(() -> {
            MessageBean bean = JSON.parseObject(json, MessageBean.class);
            bean.kId = getMessageDao().queryKIdByIdOrCallId(bean.callId, bean.id);
            getMessageDao().insertOrUpdate(bean);
            _MessageBeanImpl data = getMessageDao().queryIdOrCallIdImpl(bean.callId, bean.id);
            listener.onSuccess(data);
        });
    }

    public static void insertOrUpdates(String json, DBListener<List<_MessageBeanImpl>> listener) {
        getWriteExecutor().execute(() -> {
           
        });
    }

    /**
     * 通过会话窗口id 查询信息（非离线消息）列表
     *
     * @param dialogId 会话窗口Id
     * @param kId      数据库自增Id
     * @param limit    数量
     * @param desc     排序方式
     */
    public static void queryMessageBy(long dialogId, int kId, int limit, boolean desc, DBListener<List<_MessageBeanImpl>> listener) {
        AppDatabase.singleton.get().getReadExecutor().execute(() -> {
            List<_MessageBeanImpl> beans = getMessageDao().queryMessageBy(dialogId, kId, limit, desc);
            listener.onSuccess(beans);
        });
    }
}
