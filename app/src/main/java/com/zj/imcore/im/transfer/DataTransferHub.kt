package com.zj.imcore.im.transfer

import com.alibaba.fastjson.JSON
import com.cf.im.db.repositorys.DialogRepository
import com.cf.im.db.repositorys.MessageRepository
import com.zj.im.chat.enums.SendMsgState
import com.zj.ui.dispatcher.UIStore
import com.zj.model.chat.MsgInfo
import com.zj.model.chat.ProgressInfo
import com.zj.model.mod.SendMessageBean

object DataTransferHub {

    //todo 临时拆分
    fun onSocketDataReceived(data: String?, callId: String?, sendingState: SendMsgState?, onFinish: () -> Unit) {
        val d = JSON.parseObject(data)
        when (d["type"].toString()) {
            "create_message" -> {
                MsgInfoTransfer.transforMsg(d, callId, sendingState, onFinish)
            }
        }
    }

    fun onSendingProgressChanged(process: Int, callId: String) {
        UIStore.postData(ProgressInfo(callId, process))
    }

    fun queryDialogInDb() {
        DialogRepository.queryDialog {
            UIStore.postData(DialogTransfer.transform(it))
        }
    }

    fun queryMsgInDb(dialogId: Long) {
        MessageRepository.queryMessageBy(dialogId, "-", -1, 20, true) {
            UIStore.postData(MsgInfoTransfer.transform(it))
        }
    }

    private fun getMockMsgs(data: SendMessageBean): MsgInfo {
        return MsgInfoTransfer.transform(data)
    }
}