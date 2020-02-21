package com.zj.imcore.model;

import com.cf.im.db.domain.impl._MessageMemberBean;

import java.util.List;

/**
 * @author yangji
 */
public interface GroupInfo {

    interface GroupMember {
        /**
         * 用户 TeamId
         *
         * @return 用户 TeamId
         */
        String getTmId();

        String getRole();
    }

    /**
     * 获取 讨论组Id
     *
     * @return 讨论组Id
     */
    String getDialogId();

    /**
     * 获取 讨论组名称
     *
     * @return 讨论组名称
     */
    String getName();

    /**
     * 获取 讨论组 头像
     *
     * @return 讨论组头像
     */
    String getAvatar();

    /**
     * 获取主题
     *
     * @return 主题
     */
    String getTopic();

    /**
     * 讨论组的用户
     *
     * @return 讨论组 用户群体
     */
    List<GroupMember> getMembers();

}
