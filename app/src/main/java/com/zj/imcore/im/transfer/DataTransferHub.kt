package com.zj.imcore.im.transfer

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.dispatcher.UIStore
import com.zj.model.chat.MsgInfo
import com.zj.model.chat.ProgressInfo
import com.zj.model.mod.MessageBean

object DataTransferHub {

    //todo 临时拆分
    fun onSocketDataReceived(data: String?, callId: String?, sendingState: SendMsgState?, onFinish: () -> Unit) {
        val d = Gson().fromJson(data, JsonObject::class.java)
        val msg = Gson().fromJson(d.get("data").toString(), MessageBean::class.java)
        mCacheMsgs.firstOrNull { it == msg }?.let {
            msg.callId = it.callId
            msg.localCreateTs = System.currentTimeMillis()
        } ?: {
            msg.callId = if (callId.isNullOrEmpty()) d.get("call_id").asString else callId
            msg.sendMsgState = sendingState?.type ?: 0
            msg.localCreateTs = System.currentTimeMillis()
        }.invoke()
        mCacheMsgs.add(msg)
        UIStore.postData(getMockMsgs(msg))
        onFinish()
    }

    fun onSendingProgressChanged(process: Int, callId: String) {
        UIStore.postData(ProgressInfo(callId, process))
    }

    fun queryDialogInDb() {
        UIStore.postData(DialogTransfer.getTestData())
    }

    fun queryMsgInDb(uid: String, dialogId: String) {
        UIStore.postData(mCacheMsgs)
    }

    private fun getMockMsgs(data: MessageBean): MsgInfo {
        return MsgInfoTransfer.transform(data)
    }

    val mCacheMsgs = mutableListOf<MessageBean>()

}