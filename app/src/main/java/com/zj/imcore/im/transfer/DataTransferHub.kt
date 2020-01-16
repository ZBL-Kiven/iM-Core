package com.zj.imcore.im.transfer

import com.alibaba.fastjson.JSON
import com.cf.im.db.repositorys.DialogRepository
import com.cf.im.db.repositorys.MessageRepository
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
        when (d.get("type").toString()) {
            "create_message" -> {
                transforMsg(d, callId, sendingState, onFinish)
            }
        }
    }


    private fun transforMsg(d: JsonObject, callId: String?, sendingState: SendMsgState?, onFinish: () -> Unit) {
        val msg = Gson().fromJson(d.get("data").toString(), MessageBean::class.java)
        msg.callId = if (callId.isNullOrEmpty()) d.get("call_id").asString else callId
        msg.sendMsgState = sendingState?.type ?: 0
        msg.localCreateTs = System.currentTimeMillis()

        MessageRepository.insertOrUpdate(JSON.toJSONString(msg)) {
            val info = MsgInfoTransfer.transform(it)
            UIStore.postData(info)
            onFinish()
        }
    }

    fun onSendingProgressChanged(process: Int, callId: String) {
        UIStore.postData(ProgressInfo(callId, process))
    }

    fun queryDialogInDb() {
        DialogRepository.queryDialog {
            UIStore.postData(DialogTransfer.transform(it))
            UIStore.postData(DialogTransfer.getTestData())
        }
    }

    fun queryMsgInDb(uid: String, dialogId: Long) {
        MessageRepository.queryMessageBy(dialogId, -1, 20, true) {
            UIStore.postData(MsgInfoTransfer.transform(it))
        };
    }

    private fun getMockMsgs(data: MessageBean): MsgInfo {
        return MsgInfoTransfer.transform(data)
    }
}