package com.cf.im.db.repositorys;


import com.cf.im.db.dao.DialogDao;
import com.cf.im.db.domain.DialogBean;
import com.cf.im.db.domain.impl.DialogBeanImpl;
import com.cf.im.db.listener.DBListener;

import java.util.List;

public class DialogRepository extends BaseRepository {

    private static DialogDao getDialogDao() {
        return getDatabase().getDialogDao();
    }

    public static void insertOrUpdate(DialogBean bean, DBListener<DialogBean> listener) {
        getWriteExecutor().execute(() -> {
            getDialogDao().insertOrUpdate(bean);
            listener.onSuccess(bean);
        });
    }

    public static void insertOrUpdate(List<DialogBean> beans, DBListener<List<DialogBean>> listener) {
        getWriteExecutor().execute(() -> {
            getDialogDao().insertOrUpdate(beans);
            listener.onSuccess(beans);
        });
    }

    public static void queryDialog(DBListener<List<DialogBeanImpl>> listener) {
        getReadExecutor().execute(() -> {
            List<DialogBeanImpl> beans = getDialogDao().queryAll();
            listener.onSuccess(beans);
        });
    }

    public static void queryDialogById(String dialogId, DBListener<DialogBeanImpl> listener) {
        getReadExecutor().execute(() -> {
            DialogBeanImpl bean = getDialogDao().queryById(dialogId);
            listener.onSuccess(bean);
        });
    }

    public static void queryByUserId(String userId, DBListener<DialogBeanImpl> listener) {
        getReadExecutor().execute(() -> {
            DialogBeanImpl bean = getDialogDao().queryByUserId(userId);
            listener.onSuccess(bean);
        });
    }
}


