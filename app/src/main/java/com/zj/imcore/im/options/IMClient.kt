package com.zj.imcore.im.options

import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.modle.SocketConnInfo
import com.zj.imcore.base.FCApplication
import com.zj.imcore.im.transfer.DataTransferHub

class IMClient : ClientHub<String>() {

    override fun getConnectionInfo(get: (SocketConnInfo?) -> Unit) {
        val token = SPUtils_Proxy.getAccessToken("")
        if (token.isNullOrEmpty()) {
            FCApplication.logout("you've been logout because of token invalid")
            return
        }
        val addr = "ws://106.75.100.103:8000/wand/$token"
        get(SocketConnInfo(addr, 0, 10000))
    }

    override fun onMsgPatch(data: String?, callId: String?, isSpecialData: Boolean, sendingState: SendMsgState?, isResent: Boolean, onFinish: () -> Unit) {
        DataTransferHub.onSocketDataReceived(data, callId, sendingState, onFinish)
    }

    override fun progressUpdate(progress: Int, callId: String) {
        DataTransferHub.onSendingProgressChanged(progress, callId)
    }
}