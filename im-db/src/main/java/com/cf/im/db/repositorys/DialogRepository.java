package com.cf.im.db.repositorys;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.dao.DialogDao;
import com.cf.im.db.domain.DialogBean;
import com.cf.im.db.listener.DBListener;

import java.util.List;

public class DialogRepository extends BaseRepository {

    private static DialogDao getDialogDao() {
        return getDatabase().getDialogDao();
    }

    public static void insertOrUpdate(String json, DBListener<DialogBean> listener) {
        getWriteExecutor().execute(() -> {
            DialogBean bean = JSON.parseObject(json, DialogBean.class);
            //更新数据库
            getDialogDao().insertOrUpdate(bean);
            listener.onSuccess(bean);
        });
    }

    public static void insertOrUpdate(DialogBean bean, DBListener<DialogBean> listener) {
        getWriteExecutor().execute(() -> {
            //更新数据库
            getDialogDao().insertOrUpdate(bean);
            listener.onSuccess(bean);
        });
    }

    public static void insertOrUpdates(String json, DBListener<List<DialogBean>> listener) {
        getWriteExecutor().execute(() -> {
            List<DialogBean> beans = JSON.parseArray(JSON.parseObject(json).getString("dialogs"), DialogBean.class);
            getDialogDao().insertOrUpdate(beans);
            listener.onSuccess(beans);
        });
    }

    public static void queryGroupDialog(DBListener<List<DialogBean>> listener) {
        getReadExecutor().execute(() -> {
            listener.onSuccess(getDialogDao().queryDialogByType("group"));
        });
    }

    public static void queryP2p(DBListener<List<DialogBean>> listener) {
        getReadExecutor().execute(() -> {
            List<DialogBean> beans = getDialogDao().queryDialogByType("p2p");
            listener.onSuccess(beans);
        });
    }

    public static void queryDialog(DBListener<List<DialogBean>> listener) {
        getReadExecutor().execute(() -> {
            List<DialogBean> beans = getDialogDao().queryAll();
            listener.onSuccess(beans);
        });
    }

    public static void queryDialogById(String dialogId, DBListener<DialogBean> listener) {
        getReadExecutor().execute(() -> {
            DialogBean bean = getDialogDao().queryById(dialogId);
            listener.onSuccess(bean);
        });
    }

    public static void queryDialogByTmId(String tmId, DBListener<DialogBean> listener) {
        getReadExecutor().execute(() -> {
            DialogBean bean = getDialogDao().queryByTmId(tmId);
            listener.onSuccess(bean);
        });
    }

    public static void queryDialogByTmIds(String[] tmId, DBListener<List<DialogBean>> listener) {
        getReadExecutor().execute(() -> {
            List<DialogBean> beans = getDialogDao().queryByTmIds(tmId);
            listener.onSuccess(beans);
        });
    }

    public static void queryByDialogId(String dialogId, DBListener<DialogBean> listener) {
        getReadExecutor().execute(() -> {
            DialogBean bean = getDialogDao().queryDialogById(dialogId);
            listener.onSuccess(bean);
        });
    }
}


