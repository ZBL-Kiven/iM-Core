package com.zj.imcore.im.transfer

import com.alibaba.fastjson.JSON
import com.cf.im.db.repositorys.DialogRepository
import com.cf.im.db.repositorys.MessageRepository
import com.zj.im.chat.enums.SendMsgState
import com.zj.ui.dispatcher.UIStore
import com.zj.model.chat.ProgressInfo

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

    fun queryDialogInDb(teamId: String, onGotEmpty: () -> Unit) {
        MessageRepository.queryDialogIdsByMessages(teamId) {
            if (it.isNullOrEmpty()) {
                onGotEmpty()
                return@queryDialogIdsByMessages
            }
            DialogRepository.queryDialogsByMessageIds(it) { dialogs ->
                if (dialogs.isNullOrEmpty()) {
                    onGotEmpty()
                    return@queryDialogsByMessageIds
                }
                UIStore.postData(DialogTransfer.transform(dialogs))
            }
        }
    }

    fun queryMsgInDb(dialogId: String) {
        MessageRepository.queryMessageBy(dialogId, "-", null, 20, true) {
            UIStore.postData(MsgInfoTransfer.transform(it))
        }
    }
}