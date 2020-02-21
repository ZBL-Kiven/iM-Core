package com.cf.im.db.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户通讯录
 */
@Entity
public class MemberBean {

    /**
     * 用户ID
     * sample：false
     */
    @PrimaryKey()
    public long uid;


    public String dialogId;

    /**
     * 用户是否活跃
     * sample：false
     */
    public boolean inactive;

    /**
     * 角色
     * sample：normal
     */
    public String role;

    /**
     * 登录用户名
     * sample：haiyang.yin
     */
    public String name;

    /**
     * 未知？
     * sample：normal
     */
    public String type;

    /**
     * 创建时间
     * sample：2019-12-19T08:32:52Z
     */
    public String created;

    /**
     * 更新时间
     * sample：2019-12-19T08:32:52Z
     */
    public String updated;

    /**
     * 未知？
     * sample：false
     */
    public boolean hidden;

    /**
     * 团队id
     * sample：1
     */
    public int teamId;

    //####### profile MAP START #########

    /**
     * 邮箱地址
     * sample：haiyang.yin@cityfruit.io
     */
    public String email;

    /**
     * 性别
     * sample：male
     */
    public String gender;

    /**
     * 职位
     * sample：仫佬
     */
    public String title;

    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 用户首字母
     */
    public String indexSymbol = "#";

    /**
     * 自己添加 个人签名
     */
    public String describe;

    /**
     * 自己添加 个人签名
     */
    public String nickname;

    /**
     * 填充 profile 内容
     *
     * @param profile 服务器返回Member 里面的 profile 对象
     */
    public void setProfile(HashMap<String, String> profile) {
        this.indexSymbol = profile.get("indexSymbol");
        this.nickname = profile.get("nickname");
        this.describe = profile.get("describe");
    }
    //####### profile MAP END #########

    public HashMap<String, String> getProfile() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("indexSymbol", indexSymbol);
        profile.put("nickname", nickname);
        profile.put("describe", describe);
        return profile;
    }

}
