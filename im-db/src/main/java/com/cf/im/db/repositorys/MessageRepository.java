package com.cf.im.db.repositorys;

import androidx.collection.LongSparseArray;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.dao.impl.MessageDaoImpl;
import com.cf.im.db.databases.AppDatabase;
import com.cf.im.db.domain.MessageBean;
import com.cf.im.db.domain.impl._MessageBeanImpl;
import com.cf.im.db.listener.DBListener;
import com.zj.model.interfaces.MessageIn;

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
            List<MessageBean> beans = JSON.parseArray(json, MessageBean.class);
            LongSparseArray<MessageBean> serviceBeans = new LongSparseArray<>();


            long[] ids = new long[beans.size()];
            int index = 0;
            for (MessageBean bean : beans) {
                serviceBeans.put(bean.id, bean);
                ids[index] = (bean.id);
                index++;
            }

            List<MessageBean> dbList = getMessageDao().queryByIds(ids);

            for (MessageBean dbBean : dbList) {
                MessageBean bean = serviceBeans.get(dbBean.id);
                if (bean != null) {
                    bean.kId = dbBean.kId;
                }
            }

            getMessageDao().insertOrUpdate(beans);

            List<_MessageBeanImpl> messages = getMessageDao().queryMessageByServiceIds(ids);

            beans.clear();
            serviceBeans.clear();
            dbList.clear();

            beans = null;
            serviceBeans = null;
            ids = null;
            dbList = null;
            listener.onSuccess(messages);
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
