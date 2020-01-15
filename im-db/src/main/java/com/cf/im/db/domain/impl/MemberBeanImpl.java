package com.cf.im.db.domain.impl;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memberbean")
public class MemberBeanImpl {
    /**
     * 用户ID
     * sample：false
     */
    @PrimaryKey()
    public int uid;

    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 名称
     */
    public String name;
}
