package com.cf.im.db.domain.impl;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.cf.im.db.domain.DialogBean;
import com.cf.im.db.domain.MessageBean;
import com.zj.model.interfaces.DialogIn;

public class _DialogBeanImpl implements DialogIn {

    @Embedded
    public DialogBean bean;

    @Relation(parentColumn = "dialogId", entityColumn = "dialogId", entity = MessageBean.class)
    public _DialogMessageBean msg;

    @Override
    @NonNull
    public long getId() {
        return bean.dialogId;
    }

    @Override
    @NonNull
    public String getTitle() {
        return bean.name;
    }

    @Override
    @NonNull
    public String getSubDetail() {
        return msg == null ? "" : msg.text;
    }

    // TODO: 2020/1/16 yj
    @Override
    public long getLatestTs() {
        return msg != null ? System.currentTimeMillis() : System.currentTimeMillis();
    }

    @Override
    public long getSelfReadTs() {
        return 0;
    }

    @Override
    public int getUnReadCount() {
        return 0;
    }

    @Override
    public long getOtherReadTs() {
        return 0;
    }

    @Override
    public long getUserId() {
        return bean.userId;
    }

    @Override
    public boolean hasStar() {
        return false;
    }

    @Override
    public String getDraft() {
        return bean.draft;
    }

    @Override
    public boolean isShown() {
        return true;
    }

    @Override
    public long sortTs() {
        return bean.latestTs;
    }

    @Override
    public boolean notification() {
        return false;
    }

    @Override
    public long hideTs() {
        return 0;
    }

    @Override
    @NonNull
    public String getThumbUrl() {
        return bean.avatar;
    }

    @Override
    public boolean isPin() {
        return false;
    }

    @Override
    public boolean isMute() {
        return false;
    }

    @Override
    public boolean isDelete() {
        return false;
    }
}
