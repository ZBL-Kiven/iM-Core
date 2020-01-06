package com.zj.imcore.im.options

import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.hub.ClientHub
import com.zj.imcore.im.transfer.DataTransferHub

class IMClient : ClientHub<String>() {

    override fun onMsgPatch(data: String?, callId: String?, isSpecialData: Boolean, sendingState: SendMsgState?, isResent: Boolean, onFinish: () -> Unit) {
        DataTransferHub.onSocketDataReceived(data, callId, sendingState, onFinish)
    }

    override fun progressUpdate(progress: Int, callId: String) {
        DataTransferHub.onSendingProgressChanged(progress, callId)
    }
}