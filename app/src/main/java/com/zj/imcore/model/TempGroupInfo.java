package com.zj.imcore.model;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;


/**
 * 模拟使用
 *
 * @author yangji
 */
public class TempGroupInfo implements GroupInfo {


    /**
     * inactive : false
     * description : null
     * private : false
     * updated : 2020-02-20T08:07:43Z
     * name : from API
     * mode : admin_off
     * type : group
     * created : 2020-02-20T08:07:43Z
     * topic : null
     * avatar : null
     * leavable : true
     * team_id : =bw52O
     * dialog_id : =bw53e
     * members : [{"tmId":"=bw53i","role":"owner"},{"tmId":"=bw53j","role":"normal"},{"tmId":"=bw53k","role":"normal"}]
     */

    public boolean inactive;
    public Object description;
    @JSONField(name = "private")
    public boolean privateX;
    public String updated;
    public String name;
    public String mode;
    public String type;
    public String created;
    public Object topic;
    public Object avatar;
    public boolean leavable;
    public String team_id;
    public String dialog_id;
    public List<MembersBean> members;

    @Override
    public String getName() {
        return "测试";
    }

    @Override
    public String getAvatar() {
        return "http://b-ssl.duitang.com/uploads/item/201707/10/20170710210234_y3Kf5.jpeg";
    }

    @Override
    public String getTopic() {
        return "汇集天下精英";
    }

    @Override
    public String getDialogId() {
        return "=bw53e";
    }

    @Override
    public List<GroupMember> getMembers() {
        List<GroupMember> groupMembers = new ArrayList<>();
        groupMembers.add(new MembersBean());
        return groupMembers;
    }

    public static class MembersBean implements GroupMember {

        public String tmid;
        public String role;

        @Override
        public String getTmId() {
            return tmid;
        }

        @Override
        public String getRole() {
            return role;
        }
    }

}
