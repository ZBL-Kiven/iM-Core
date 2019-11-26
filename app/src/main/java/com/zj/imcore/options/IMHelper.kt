package com.zj.imcore.options

import android.app.Application
import com.zj.im.chat.core.BaseOption
import com.zj.im.chat.core.OnBuildOption
import com.zj.im.chat.exceptions.ChatException
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.interfaces.AnalyzingData
import com.zj.im.chat.interfaces.IMInterface

object IMHelper : IMInterface() {

    fun init(app: Application) {
        val option = BaseOption.create(app).logsCollectionAble { true }
            .logsFileName("IM")
            .logsFileName("aa")
            .setLogsMaxRetain(3L*24*60*60*1000)
            .build(object : OnBuildOption() {
            override fun getClient(): ClientHub {
                return IMClient()
            }

            override fun getServer(): ServerHub {
                return IMServer()
            }

            override fun onError(e: ChatException) {

            }

        })
        initChat(option)
    }

    override fun onMsgPatch(data: AnalyzingData, onFinish: () -> Unit) {
        println(" ----- ${data.getData()}")
        onFinish()
    }

    override fun progressUpdate(progress: Int, callId: String) {
        println(" ----- $callId   $progress")
    }

}