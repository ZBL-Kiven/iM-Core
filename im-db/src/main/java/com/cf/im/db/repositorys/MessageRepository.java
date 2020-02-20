package com.cf.im.db.repositorys;

import android.util.Log;

import androidx.collection.LongSparseArray;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.dao.impl.MessageDaoImpl;
import com.cf.im.db.databases.AppDatabase;
import com.cf.im.db.domain.MessageBean;
import com.cf.im.db.domain.impl._MessageBeanImpl;
import com.cf.im.db.listener.DBListener;
import com.cf.im.db.utils.DateUtils;

import java.util.Collections;
import java.util.List;

public class MessageRepository extends BaseRepository {

    private static MessageDaoImpl getMessageDao() {
        return AppDatabase.singleton.get().getMessageDao();
    }

    public static void insertOrUpdate(String json, DBListener<_MessageBeanImpl> listener) {
        getWriteExecutor().execute(() -> {
            MessageBean bean = JSON.parseObject(json, MessageBean.class);
            MessageBean _temp = getMessageDao().queryByIdOrCallId(bean.callId, bean.id);

            if (_temp != null) {
                bean.kId = _temp.kId;
                bean.callId = _temp.callId;
                bean.localCreateTs = _temp.localCreateTs;
            } else {
                bean.localCreateTs = DateUtils.getTime(-1);
            }

            getMessageDao().insertOrUpdate(bean);
            _MessageBeanImpl data = getMessageDao().queryIdOrCallIdImpl(bean.callId, bean.id);
            listener.onSuccess(data);
        });
    }

    public static void insertOrUpdates(String json, DBListener<List<_MessageBeanImpl>> listener) {
        getWriteExecutor().execute(() -> {
            List<MessageBean> beans = JSON.parseArray(json, MessageBean.class);
            LongSparseArray<MessageBean> serviceBeans = new LongSparseArray<>();
            Collections.sort(beans, (o1, o2) -> Long.compare(o1.id, o2.id));

            long[] ids = new long[beans.size()];
            int index = 0;
            for (MessageBean bean : beans) {
                bean.localCreateTs = DateUtils.getTime(bean.localCreateTs, bean.created, bean.updated);
                serviceBeans.put(bean.id, bean);
                ids[index] = (bean.id);
                index++;
            }

            List<MessageBean> dbList = getMessageDao().queryByIds(ids);

            //如果本地存在，则更新localCreateTs 以及 kid
            for (MessageBean _temp : dbList) {
                MessageBean bean = serviceBeans.get(_temp.id);
                if (bean != null) {
                    bean.kId = _temp.kId;
                    bean.callId = _temp.callId;
                    bean.localCreateTs = _temp.localCreateTs;
                }
            }

            getMessageDao().insertOrUpdate(beans);

            List<_MessageBeanImpl> messages = getMessageDao().queryMessageByServiceIds(ids);

            beans.clear();
            serviceBeans.clear();
            dbList.clear();

            listener.onSuccess(messages);
        });
    }

    /**
     * 通过会话窗口id 查询信息（非离线消息）列表
     *
     * @param dialogId   会话窗口Id
     * @param callId     本地标识一条消息的唯一 Id
     * @param msgKey     Server 的 MsgId
     * @param limit      数量
     * @param isPositive 方向，向上取还是向下取
     */
    public static void queryMessageBy(String dialogId, String callId, long msgKey, int limit, boolean isPositive, DBListener<List<_MessageBeanImpl>> listener) {
        AppDatabase.singleton.get().getReadExecutor().execute(() -> {
            long time;
            if ("-".equals(callId) || msgKey == -1) {
                time = 0L;
            } else {
                time = getMessageDao().queryLocalCreateTsOrCallIdImpl(callId, msgKey);
            }
            // _MessageBeanImpl mpl = getMessageDao().queryIdOrCallIdImpl(callId, msgKey);
            // long kId = mpl == null ? -1 : mpl.message.kId;

            List<_MessageBeanImpl> beans = getMessageDao().queryMessageBy(dialogId, time, limit, isPositive);

            Log.e("DB___", dialogId + " " + callId + " " + msgKey + " " + beans.size());

            System.err.println("DB___  " + dialogId + " " + callId + " " + msgKey + " " + beans.size());
            listener.onSuccess(beans);
        });
    }
}
