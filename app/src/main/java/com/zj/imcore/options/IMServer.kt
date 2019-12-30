package com.zj.imcore.options

import android.app.Service
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.interfaces.ConnectCallBack
import com.zj.im.chat.interfaces.HeartBeatsCallBack

class IMServer : ServerHub<String>() {

    override fun init() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connect(address: String?, port: Int?, socketTimeOut: Int?, callBack: ConnectCallBack?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun send(params: Map<String, Any>, callBack: HeartBeatsCallBack?): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shutdown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeSocket() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}