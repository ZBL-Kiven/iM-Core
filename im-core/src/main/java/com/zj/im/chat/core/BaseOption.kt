package com.zj.im.chat.core

import android.app.Application
import android.app.Notification
import com.zj.im.chat.enums.RuntimeEfficiency
import com.zj.im.chat.exceptions.ExceptionHandler
import com.zj.im.chat.exceptions.NecessaryAttributeEmptyException
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.hub.StatusHub
import com.zj.im.chat.hub.StatusHub.isRunningInBackground
import com.zj.im.main.ChatBase
import com.zj.im.main.ChatBase.checkInit
import com.zj.im.sender.SendObject
import com.zj.im.sender.SendingPool
import com.zj.im.utils.log.logger.printInFile

/**
 * Created by ZJJ
 *
 * the IM SDK options ,used for #ChatBase
 *
 * @see OptionProxy
 *
 * @param notification start a foreground service to keeping active for sdk when the app was running in background
 *
 * @param sessionId the foreground service session id
 *
 * @param runtimeEfficiency the sdk efficiency level for sdk , {@link RuntimeEfficiency#interval }
 *
 * @param logsCollectionAble set is need collection runtime logs , {@link logsFileName} and saved in somewhere
 *
 * @param buildOption made some rules to sdk , {@see OnBuildOption}
 */

class BaseOption internal constructor(val context: Application, private val notification: Notification? = null, private val sessionId: Int?, private val runtimeEfficiency: RuntimeEfficiency, val logsCollectionAble: () -> Boolean, val logsFileName: String, val logsMaxRetain: Long, val buildOption: OnBuildOption) {

    companion object {
        @Suppress("unused")
        fun create(context: Application): OptionProxy {
            return OptionProxy(context)
        }
    }

    private var mClient: ClientHub? = null

    internal fun getClient(where: String): ClientHub? {
        if (!ChatBase.isFinishing(runningKey) && mClient == null) {
            ChatBase.postError(NecessaryAttributeEmptyException("at $where.getClient() \n clientHub == null ,you must call register a client hub before eventHub working"))
        }
        return mClient
    }

    private var mServer: ServerHub? = null

    internal fun getServer(where: String): ServerHub? {
        if (!ChatBase.isFinishing(runningKey) && mServer == null) {
            ChatBase.postError(NecessaryAttributeEmptyException("at  $where.getServer() \n serverHub == null ,you must call register a server hub before eventHub working"))
        }
        return mServer
    }

    private var runningKey: String = ""
    private var curEfficiency: Long = RuntimeEfficiency.SLEEP.interval

    internal fun init(runningKey: String) {
        this.runningKey = runningKey
        mClient = buildOption.getClient()
        if (mClient == null) throw NullPointerException("you can't register a null client in there!")
        else mClient?.initRunningKey(runningKey)
        mServer = buildOption.getServer()
        if (mServer == null) throw NullPointerException("you can't register a null server in there!")
        else mServer?.init();mServer?.initRunningKey(runningKey)
        mClient?.setLifecycleListener(StatusHub)
        DataStore.init()
        SendingPool.init()
        DataStore.canAuth { return@canAuth getClient("canAuth")?.canAuth() ?: false }
        DataStore.canSend { return@canSend getClient("canSend")?.canSend() ?: false }
        DataStore.canReceive { return@canReceive getClient("canReceive")?.canReceived() ?: false }
        DataStore.isHeartBeatsOrAuthResponse {
            return@isHeartBeatsOrAuthResponse getClient("isHeartBeatsOrAuthResponse")?.filterHeartBeatsOrAuthResponse(it) ?: Triple(first = false, second = false, third = null)
        }
        initMsgHandler(runningKey)
    }

    internal fun initMsgHandler(runningKey: String) {
        printInFile("BaseOption", "the message handler init")
        curEfficiency = runtimeEfficiency.interval
        MsgHandler.init(runningKey, curEfficiency)
    }

    internal fun setFrequency(runtimeEfficiency: RuntimeEfficiency) {
        if (runtimeEfficiency.interval == curEfficiency) return
        curEfficiency = runtimeEfficiency.interval
        printInFile("BaseOption.onFrequencyChanged", "the SDK work efficiency has been changed to level-${runtimeEfficiency.name}")
        MsgHandler.setFrequency(curEfficiency)
    }

    internal fun sendToSocket(sendObject: SendObject) {
        checkInit("sendToSocket")
        getClient("sendToSocket")?.sendToSocket(sendObject)
    }

    internal fun shutDown() {
        runningKey = ""
        buildOption.shutdown()
        mClient?.shutDown()
        mServer?.shutdown()
        mClient = null
        mServer = null
    }

    internal fun pause(code: Int) {
        getClient("pause")?.onPause(code)
    }

    internal fun resume(code: Int) {
        getClient("resume")?.onResume(code)
    }

    internal fun onLayerChanged(isHidden: Boolean) {
        try {
            buildOption.onLayerChanged(isHidden)
        } catch (t: Exception) {
            ExceptionHandler.postError(t)
        } catch (t: java.lang.Exception) {
            ExceptionHandler.postError(t)
        }
        val changedNames = if (isHidden) {
            arrayOf("foreground", "background")
        } else {
            arrayOf("background", "foreground")
        }
        printInFile("BaseOption.onLayerChanged", "the task running changed form ${changedNames[0]} to ${changedNames[1]} ")
        notification?.let {
            val service = getServer("BaseOption.onLayerChanged")?.getService("onLayerChanged", true)
            isRunningInBackground = if (isHidden) {
                service?.startForeground(sessionId ?: -1, it)
                true
            } else {
                service?.stopForeground(true)
                false
            }
        }
    }
}
