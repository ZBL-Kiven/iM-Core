package com.cf.im.db.domain;

import androidx.annotation.NonNull;

import com.zj.model.interfaces.DialogIn;

public class DialogBeanImpl extends DialogBean implements DialogIn {
    @Override
    @NonNull
    public String getId() {
        return channelID;
    }

    @Override
    @NonNull
    public String getTitle() {
        return title;
    }

    @Override
    @NonNull
    public String getSubDetail() {
        return subDetail;
    }

    @Override
    public long getLatestTs() {
        return latestTs;
    }

    @Override
    public long getSelfReadTs() {
        return selfReadTs;
    }

    @Override
    public int getUnReadCount() {
        return unReadCount;
    }

    @Override
    public long getOtherReadTs() {
        return otherReadTs;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public boolean hasStar() {
        return hasStar;
    }

    @Override
    public String getDraft() {
        return draft;
    }

    @Override
    public boolean isShown() {
        return isShown;
    }

    @Override
    public long sortTs() {
        return sortTs;
    }

    @Override
    public boolean notification() {
        return notification;
    }

    @Override
    public long hideTs() {
        return hideTs;
    }

    @Override
    @NonNull
    public String getThumbUrl() {
        return thumbUrl;
    }

    @Override
    public boolean isPin() {
        return isPin;
    }

    @Override
    public boolean isMute() {
        return isMute;
    }

    @Override
    public boolean isDelete() {
        return isDelete;
    }
}
