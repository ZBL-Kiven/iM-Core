package com.cf.im.db.domain;

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
}
