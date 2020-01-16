package com.cf.im.db.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.zj.model.mod.MessageBean;

import java.util.Map;

@Entity(indices = {@Index("userId"), @Index("latestTs")})
public class DialogBean {

    @PrimaryKey
    public long dialogId; // 17179869197,

    public String groupId; // null,

    public int teamId; // 1,

    public String userId; // 4,

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
}
