package com.cf.im.db.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;

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
    public int uid;

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
     * sample：仫佬
     */
    public String title;

    public int age;

    /**
     * 填充 profile 内容
     *
     * @param profile 服务器返回Member 里面的 profile 对象
     */
    public void setProfile(HashMap<String, String> profile) {
        this.gender = profile.get("gender");
        this.email = profile.get("email");
        this.title = profile.get("title");
        age = gender.getBytes().length;
    }
    //####### profile MAP END #########


}
