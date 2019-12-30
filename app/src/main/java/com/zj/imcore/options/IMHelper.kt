package com.zj.imcore.options

import android.app.Application
import com.zj.im.chat.core.BaseOption
import com.zj.im.chat.core.OnBuildOption
import com.zj.im.chat.exceptions.ChatException
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.interfaces.AnalyzingData
import com.zj.im.main.IMInterface
import com.zj.im.UIHelper
import com.zj.im.chat.hub.ServerHub
/**
 * @property getClient return your custom client for sdk {@see ClientHub}
 *
 * @property getServer return your custom server for sdk {@see ServerHub}
 *
 * @property onError handler the sdk errors with runtime
 *
 * @property prepare on SDK init prepare
 *
 * @property shutdown it called when SDK was shutdown
 *
 * @property onLayerChanged it called when SDK was changed form foreground / background
 * */

object IMHelper : IMInterface<String>() {

    fun init(app: Application) {
        val option = BaseOption.create(app).logsCollectionAble { true }.logsFileName("IM").logsFileName("aa").setLogsMaxRetain(3L * 24 * 60 * 60 * 1000).build()
        initChat(option)
    }

    override fun onMsgPatch(data: AnalyzingData<String>, onFinish: () -> Unit) {
        UIHelper.postReceiveData(data)
        onFinish()
    }

    override fun progressUpdate(progress: Int, callId: String) {
        println(" ----- $callId   $progress")
    }

    override fun getClient(): ClientHub {
        return IMClient()
    }

    override fun getServer(): ServerHub<String> {
        return IMServer()
    }

    override fun onError(e: ChatException) {

    }

}