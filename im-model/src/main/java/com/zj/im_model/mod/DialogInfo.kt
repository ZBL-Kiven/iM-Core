package com.zj.im_model.mod

interface DialogInfo {

    //每个对话的唯一标示
    var getId: String? = null

    //对话最后更新的时间
    var latestTs: Long = 0

    var readTs: Long = 0

    var unreadCount: Int = 0

    //对话的配置信息
    var preference: Preference? = null

    //对话的类型
    //1. channel 群组对话
    //2. member p2p 对话
    //3. board 公告板
    var type: String? = null

    //别人最后读过对话的时间
    var otherReadTs: Long = 0

    //member p2p 对话时, 对方的用户 id
    var userId: String? = null

    //channel 群组对话时, 群组的 id
    var channelId: String? = null

    //board 公告板时, 公告板的 id
    var boardId: String? = null

    //对话中是否有收藏的消息
    var hasStar: Boolean = false

    //对话的草稿
    var draft: String? = null

    //对话是否显示
    var shown: Boolean = true

    //对话的排序依据
    var sortTs: Long = 0

}