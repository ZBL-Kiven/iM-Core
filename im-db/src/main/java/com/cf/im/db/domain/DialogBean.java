package com.cf.im.db.domain;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.zj.model.interfaces.DialogIn;
import java.util.Map;

@Entity(indices = {@Index("userId"), @Index("latestTs")})
public class DialogBean implements DialogIn {

    @PrimaryKey
    public long dialogId; // 17179869197,

    public String groupId; // null,

    public int teamId; // 1,

    public long userId; // 4,

    public String type; // "p2p",

    public String name;

    public String avatar;

    //草稿
    public String draft;

    public long latestTs; // 1578991414658

    public void setInfo(Map<String, String> info) {
        this.name = info.get("name");
        this.avatar = info.get("avatar");
    }

    public long messageUserId;
    public String updated;
    public String messageText;
    public String messageSubType;

    public void setMessage(MessageBean bean) {
        if (bean != null) {
            this.messageUserId = bean.uid;
            this.updated = bean.updated;
            this.messageText = bean.text;
            this.messageSubType = bean.subtype;
        }
    }

    // ~~~~~~~~~~~ 实现前段接口

    @Override
    public long getId() {
        return dialogId;
    }

    @Override
    @NonNull
    public String getTitle() {
        return name;
    }

    @Override
    @NonNull
    public String getSubDetail() {
        return messageText;
    }

    @Override
    public long getLatestTs() {
        return latestTs;
    }

    @Override
    public long getSelfReadTs() {
        return latestTs;
    }

    @Override
    public int getUnReadCount() {
        return 1;
    }

    @Override
    public long getOtherReadTs() {
        return 1;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public boolean hasStar() {
        return false;
    }

    @Override
    public String getDraft() {
        return "";
    }

    @Override
    public boolean isShown() {
        return false;
    }

    @Override
    public long sortTs() {
        return System.currentTimeMillis();
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
        return TextUtils.isEmpty(avatar) ? "" : avatar;
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
