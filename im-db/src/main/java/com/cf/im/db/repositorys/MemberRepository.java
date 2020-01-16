package com.cf.im.db.repositorys;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.dao.MemberDao;
import com.cf.im.db.domain.MemberBean;
import com.cf.im.db.listener.DBListener;

import java.util.List;

public class MemberRepository extends BaseRepository {

    private static MemberDao getMemberDao() {
        return getDatabase().getMemberDao();
    }

    public static MemberBean insertOrUpdate(MemberBean bean) {
        getMemberDao().insertOrUpdate(bean);
        return bean;
    }

    public static void insertOrUpdateAll(String json, DBListener<List<MemberBean>> listener) {
        getWriteExecutor().execute(() -> {
            List<MemberBean> list = JSON.parseArray(json, MemberBean.class);
            getMemberDao().insertOrUpdate(list);
            listener.onSuccess(list);
        });
    }

    public static void insertOrUpdate(MemberBean bean, DBListener<MemberBean> listener) {
        getWriteExecutor().execute(() -> {
            getMemberDao().insertOrUpdate(bean);
            listener.onSuccess(bean);
        });
    }

    public static List<MemberBean> queryAll() {
        return getMemberDao().getAll();
    }

    public static void queryAll(DBListener<List<MemberBean>> listener) {
        getReadExecutor().execute(() -> {
            List<MemberBean> beans = getMemberDao().getAll();
            listener.onSuccess(beans);
        });
    }

    public static void queryMembersByUserId(int userId, DBListener<MemberBean> listener) {
        getReadExecutor().execute(() -> {
            MemberBean bean = getMemberDao().queryById(userId);
            listener.onSuccess(bean);
        });
    }

    public static void clearAll() {
        getWriteExecutor().execute(() -> getMemberDao().clearAll());
    }

    public static void clearAll(DBListener<Boolean> listener) {
        getWriteExecutor().execute(() -> {
            getMemberDao().clearAll();
            listener.onSuccess(true);
        });
    }
}
