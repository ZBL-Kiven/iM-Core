package com.zj.imcore.options

import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.interfaces.ConnectCallBack
import com.zj.im.chat.interfaces.SendingCallBack
import com.zj.im.chat.modle.SocketConnInfo

class IMServer : ServerHub<String>() {
    override fun init() {

    }

    override fun connect(connInfo: SocketConnInfo?, callBack: ConnectCallBack?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun send(params: String?, callId: String, callBack: SendingCallBack?): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeSocket() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}