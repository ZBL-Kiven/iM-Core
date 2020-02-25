package com.cf.im.db.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.zj.model.chat.TeamMembers;
import com.zj.model.interfaces.DialogIn;

import java.util.List;
import java.util.Map;

@Entity
public class DialogBean implements DialogIn {

    /**
     * inactive : false
     * role : normal
     * department : null
     * email : yan.li@cityfruit.io
     * phone : 13521930955
     * updated : 2019-12-26T03:16:26Z
     * name : yan.li
     * tmid : =bw52R
     * type : p2p
     * created : 2019-12-26T03:16:26Z
     * title : null
     * hidden : false
     * avatar : null
     * team_id : =bw52O
     * dialog_id : =sMb4px
     * gender : male
     * members : [{"tmid":"=bw53h"},{"tmid":"=bw52R"}]
     * profile : {"extra":"This is a sample which can add any kv pair here."}
     */
    @NonNull
    @PrimaryKey
    @JSONField(name = "dialog_id")
    public String dialogId;

    public boolean inactive;
    public String role;
    public String department;
    public String email;
    public String phone;
    public String updated;
    public String name;
    @JSONField(name = "tmid")
    public String tmId;
    public String type;
    public String created;
    public String title;
    public boolean hidden;

    public String avatar;

    @JSONField(name = "team_id")
    public String teamId;

    public String gender;
    public String profile;
    public String members;

    @JSONField(name = "private")
    public String privateX;

    public boolean leavable;
    public String topic;
    public String draft;
    public String description;
    public String mode;

    @Override
    public boolean inactive() {
        return false;
    }

    @Override
    public String indexSymbol() {
        return "#";
    }

    @Override
    public String role() {
        return role;
    }

    @Override
    public String department() {
        return department;
    }

    @Override
    public String email() {
        return email == null ? "" : email;
    }

    @Override
    public String phone() {
        return phone;
    }

    @Override
    public String name() {
        return name == null ? "" : name;
    }

    @Override
    public String tmid() {
        return tmId;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public long updated() {
        return 0;
    }

    @Override
    public long created() {
        return 0;
    }

    @Override
    public String title() {
        return title == null ? "" : title;
    }

    @Override
    public boolean hidden() {
        return false;
    }

    @Override
    public String avatar() {
        return avatar;
    }

    @Override
    public String teamId() {
        return teamId;
    }

    @Override
    public String dialogId() {
        return dialogId;
    }

    @Override
    public String gender() {
        return gender;
    }

    @Override
    public boolean pin() {
        return false;
    }

    @Override
    public boolean mute() {
        return false;
    }

    @Override
    public String draft() {
        return draft;
    }

    @Override
    public String subDetail() {
        return null;
    }

    @Override
    public long unReadCount() {
        return 100;
    }

    @Override
    public List<TeamMembers> getTeamMembers(List<? extends Map<String, ? extends Object>> lst) {
        return JSON.parseArray(members, TeamMembers.class);
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String mode() {
        return mode;
    }

    @Override
    public String topic() {
        return topic;
    }

    @Override
    public boolean leavable() {
        return leavable;
    }

    @Override
    public String getPrivate() {
        return privateX;
    }

    public String get(String key) {
        Object value = getProfileMap().get(key);
        return value == null ? null : value.toString();
    }

    public Map<String, Object> getProfileMap() {
        return JSON.parseObject(profile, Map.class);
    }
}
