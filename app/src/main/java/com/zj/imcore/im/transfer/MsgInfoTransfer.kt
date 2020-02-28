package com.zj.imcore.im.transfer

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.cf.im.db.domain.MessageBean
import com.cf.im.db.domain.impl._MessageBeanImpl
import com.cf.im.db.repositorys.MessageRepository
import com.zj.im.chat.enums.SendMsgState
import com.zj.ui.dispatcher.UIStore
import com.zj.imcore.core.notification.NotificationManager
import com.zj.model.chat.MsgInfo

object MsgInfoTransfer {

    fun transforMsg(d: JSONObject, callId: String?, sendingState: SendMsgState?, onFinish: () -> Unit) {
        val msg = JSON.parseObject(d["data"].toString(), MessageBean::class.java)
        msg.callId = if (callId.isNullOrEmpty()) d["call_id"].toString() else callId
        msg.sendMsgState = sendingState?.type ?: 0

        MessageRepository.insertOrUpdate(JSON.toJSONString(msg)) {
            val info = transform(it)
            NotificationManager.singleton.get().sendMessageNotification(info)
            UIStore.postData(info)
            onFinish()
        }
    }

    fun transform(beans: List<_MessageBeanImpl>): MutableList<MsgInfo> {
        return beans.mapTo(arrayListOf()) {
            transform(it)
        }
    }

    fun transform(bean: _MessageBeanImpl): MsgInfo {
        return MsgInfo(bean)
    }
}