package com.zj.imcore.im.options

import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.hub.ClientHub
import com.zj.imcore.im.transfer.DataTransferHub

class IMClient : ClientHub<String>() {

    /**
     * When SDK receives the message, it will call
     *
     * @param data the msg data as jsonString
     *
     * @param callId the call id with sending,excepted of the message body
     *
     * @param isSpecialData ignore there, #special data over of the sdk lifecycle or connection state
     *
     * @param sendingState the msg sending status, #{@link SendMsgState}.
     *
     * @param onFinish must call it after the message handled , either the next message will wating in queue
     * */
    override fun onMsgPatch(data: String?, callId: String?, isSpecialData: Boolean, sendingState: SendMsgState?, isResent: Boolean, onFinish: () -> Unit) {
        DataTransferHub.onSocketDataReceived(data, callId, sendingState, onFinish)
    }

    /**
     * @param progress in 0..100
     *
     * @param callId the call id with sending
     * */
    override fun progressUpdate(progress: Int, callId: String) {
        DataTransferHub.onSendingProgressChanged(progress, callId)
    }
}