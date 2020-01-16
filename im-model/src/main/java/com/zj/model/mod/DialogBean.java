package com.zj.model.mod;

import java.util.Map;

public class DialogBean {

    public String type; // "p2p",

    public int teamId; // 1,

    public String dialogId; // 17179869197,

    public String userId; // 4,

    public String groupId; // null,

    public long latestTs; // 1578991414658

    public MessageBean message;

    public String name;

    public String avatar;

    public void setInfo(Map<String, String> info) {
        this.name = info.get("name");
        this.avatar = info.get("avatar");
    }
}
