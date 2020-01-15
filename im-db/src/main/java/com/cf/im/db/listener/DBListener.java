package com.cf.im.db.listener;

public interface DBListener<T> {

    void onSuccess(T t);

}
