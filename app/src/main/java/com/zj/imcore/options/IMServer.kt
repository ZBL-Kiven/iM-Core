package com.zj.imcore.options

import android.app.Service
import com.zj.im.chat.hub.ServerHub

class IMServer : ServerHub() {

    override fun packParams(param: Map<String, Any>?): ByteArray {
        return TcpMessageUtility.packMap(param)
    }

    override fun unPackParams(data: ByteArray): Map<String, Any>? {
        return TcpMessageUtility.unpackMsg(data)
    }

    override fun onServiceConnected(service: Service?) {
        println(" ----- onServiceConnected")
    }

    override fun onServiceDisConnected(service: Service?) {
        println(" ----- onServiceDisConnected")
    }

}