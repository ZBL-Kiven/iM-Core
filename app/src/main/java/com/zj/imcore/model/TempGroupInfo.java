package com.zj.imcore.model;


import java.util.ArrayList;
import java.util.List;


/**
 * 模拟使用
 *
 * @author yangji
 */
public class TempGroupInfo implements GroupInfo {

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

    public static class MembersBean implements GroupInfo.GroupMember {

        public String tmid = "XXXXX";

        @Override
        public String getTmId() {
            return tmid;
        }
    }
}
