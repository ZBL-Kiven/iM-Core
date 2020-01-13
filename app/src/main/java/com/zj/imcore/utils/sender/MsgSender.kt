package com.zj.imcore.utils.sender

import com.google.gson.Gson
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.im.options.mod.BaseMod
import com.zj.model.mod.MessageBean
import java.util.*

object MsgSender {

    fun sendText(sessionId: String, text: String) {
        val callId = UUID.randomUUID().toString()
        val baseSendInfo = BaseMod()
        val m = MessageBean().apply {
            this.subtype = "normal"
            this.uid = SPUtils_Proxy.getUserId("0").toInt()
            this.team_id = 1
            this.dialog_id = sessionId.toLong()
            this.text = text
            this.callId = callId
        }
        baseSendInfo.data = m
        baseSendInfo.callId = callId
        baseSendInfo.type = "create_message"
        val data = Gson().toJson(baseSendInfo)
        IMHelper.send(data, callId, 10000, false, false, null)
    }

}