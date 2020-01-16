package com.cf.im.db.repositorys;


import com.cf.im.db.dao.DialogDao;
import com.cf.im.db.domain.DialogBean;
import com.cf.im.db.domain.impl._DialogBeanImpl;
import com.cf.im.db.listener.DBListener;
import com.zj.model.interfaces.DialogIn;

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

    public static void insertOrUpdate(String beans, DBListener<List<DialogIn>> listener) {
//        getWriteExecutor().execute(() -> {
//            getDialogDao().insertOrUpdate(beans);
//            listener.onSuccess(beans);
//        });
    }

    public static void queryDialog(DBListener<List<_DialogBeanImpl>> listener) {
        getReadExecutor().execute(() -> {
            List<_DialogBeanImpl> beans = getDialogDao().queryAll();
            listener.onSuccess(beans);
        });
    }

    public static void queryDialogById(String dialogId, DBListener<_DialogBeanImpl> listener) {
        getReadExecutor().execute(() -> {
            _DialogBeanImpl bean = getDialogDao().queryById(dialogId);
            listener.onSuccess(bean);
        });
    }

    public static void queryByUserId(String userId, DBListener<_DialogBeanImpl> listener) {
        getReadExecutor().execute(() -> {
            _DialogBeanImpl bean = getDialogDao().queryByUserId(userId);
            listener.onSuccess(bean);
        });
    }
}


