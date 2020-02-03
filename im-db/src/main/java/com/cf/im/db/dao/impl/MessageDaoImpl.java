package com.cf.im.db.dao.impl;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.cf.im.db.dao.MessageDao;
import com.cf.im.db.domain.MessageBean;
import com.cf.im.db.domain.impl._MessageBeanImpl;

import java.util.List;
import java.util.Locale;

@Dao
public abstract class MessageDaoImpl implements MessageDao {

    /**
     * 通过会话窗口id 查询信息（非离线消息）列表
     *
     * @param dialogId   会话窗口Id
     * @param kId        本地数据库消息的主键 Id
     * @param limit      数量
     * @param isPositive 排序方式
     * @return 消息列表
     */
    public List<_MessageBeanImpl> queryMessageBy(long dialogId, long kId, int limit, boolean isPositive) {
        String oritation;
        if (isPositive) oritation = ">";
        else oritation = "<";
        String sql;
        if (kId <= 0) {
            String sqlFormat = "select * from messagebean where dialogId = %d order by localCreateTs desc limit %d";
            sql = String.format(Locale.getDefault(), sqlFormat, dialogId, limit);
        } else {
            String sqlFormat = "select * from messagebean where dialogId = %d and kid %s %d order by localCreateTs desc limit %d";
            sql = String.format(Locale.getDefault(), sqlFormat, dialogId, oritation, kId, limit);
        }
        SimpleSQLiteQuery sqLiteQuery = new SimpleSQLiteQuery(sql);
        return queryBySqlImpl(sqLiteQuery);
    }

    @RawQuery
    public abstract List<_MessageBeanImpl> queryBySqlImpl(SupportSQLiteQuery sql);

    /**
     * 通过callId 或者 serviceId 查询消息主键Id
     *
     * @param callId    自发消息Id
     * @param serviceId 服务器消息Id
     * @return 消息主键Id（自增Id）
     */
    @Transaction
    @Query("select * from messagebean where callId = :callId or id = :serviceId limit 1")
    public abstract _MessageBeanImpl queryIdOrCallIdImpl(String callId, long serviceId);

    @Query("select * from messagebean where id in (:id)")
    public abstract List<MessageBean> queryByIds(long... id);

    @Transaction
    @Query("select * from messagebean where id in (:id)")
    public abstract List<_MessageBeanImpl> queryMessageByServiceIds(long... id);

}
