package com.zj.imcore.im.options

import android.app.Application
import android.app.Notification
import com.zj.album.nModule.FileInfo
import com.zj.im.chat.core.BaseOption
import com.zj.im.chat.hub.ClientHub
import com.zj.im.main.impl.IMInterface
import com.zj.im.chat.hub.ServerHub
import com.zj.imcore.im.fetcher.SyncManager
import com.zj.imcore.utils.sender.MsgSender

object IMHelper : IMInterface<String>() {

    fun init(app: Application) {
        val option = BaseOption.create(app).debug().logsCollectionAble { true }.logsFileName("IM")
            .setLogsMaxRetain(3L * 24 * 60 * 60 * 1000)
            .setNotify(Notification())
            .build()
        initChat(option)
    }

    override fun getClient(): ClientHub<String> {
        return IMClient()
    }

    override fun getServer(): ServerHub<String> {
        return IMServer()
    }

    override fun onError(e: Throwable) {
        throw e
    }

    override fun prepare() {
        SyncManager.init()
    }

    override fun shutdown(case: String) {
        SyncManager.shutdown()
        super.shutdown(case)
    }

    fun sendTxt(sessionId: String, text: String?) {
        if (text.isNullOrEmpty()) return
        MsgSender.sendText(sessionId, text)
    }

    fun sendSticker(sessionId: String, uri: String?) {
        if (uri.isNullOrEmpty()) return
        MsgSender.sendSticker(sessionId, uri)
    }

    fun sendImage(sessionId: String, info: FileInfo?) {
        if (info == null) return
        MsgSender.sendImage(sessionId, info)
    }

    fun sendVideo(sessionId: String, info: FileInfo?) {
        if (info == null) return
        MsgSender.sendVideo(sessionId, info)
    }

}