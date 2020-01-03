package com.zj.imcore.options

import android.app.Application
import com.zj.im.UIHelper
import com.zj.im.chat.core.BaseOption
import com.zj.im.chat.hub.ClientHub
import com.zj.im.main.impl.IMInterface
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.hub.ServerHub

object IMHelper : IMInterface<String>() {

    fun init(app: Application) {
        val option = BaseOption.create(app).logsCollectionAble { true }.logsFileName("IM").logsFileName("aa").setLogsMaxRetain(3L * 24 * 60 * 60 * 1000).build()
        initChat(option)
    }

    override fun getClient(): ClientHub<String> {
        return IMClient()
    }

    override fun getServer(): ServerHub<String> {
        return IMServer()
    }

    override fun onError(e: Throwable) {

    }
}