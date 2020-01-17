package com.cf.im.db.repositorys;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.dao.DialogDao;
import com.cf.im.db.domain.DialogBean;
import com.cf.im.db.domain.impl._DialogBeanImpl;
import com.cf.im.db.listener.DBListener;

import java.util.ArrayList;
import java.util.List;

public class DialogRepository extends BaseRepository {

    private static DialogDao getDialogDao() {
        return getDatabase().getDialogDao();
    }

    public static void insertOrUpdate(String json, DBListener<_DialogBeanImpl> listener) {
        getWriteExecutor().execute(() -> {
            DialogBean bean = JSON.parseObject(json, DialogBean.class);
            getDialogDao().insertOrUpdate(bean);
            _DialogBeanImpl _temp = new _DialogBeanImpl();
            _temp.bean = bean;
            listener.onSuccess(_temp);
        });
    }

    public static void insertOrUpdates(String json, DBListener<List<_DialogBeanImpl>> listener) {
        getWriteExecutor().execute(() -> {
            List<DialogBean> beans = JSON.parseArray(json, DialogBean.class);
            getDialogDao().insertOrUpdate(beans);
            List<_DialogBeanImpl> list = new ArrayList<>();
            _DialogBeanImpl _temp;
            for (DialogBean bean : beans) {
                _temp = new _DialogBeanImpl();
                _temp.bean = bean;
                list.add(_temp);
            }

            listener.onSuccess(list);
        });
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


