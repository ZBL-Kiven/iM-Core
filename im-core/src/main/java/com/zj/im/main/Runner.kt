package com.zj.im.main

import android.app.Application
import com.zj.im.chat.core.DataStore
import com.zj.im.chat.enums.RuntimeEfficiency
import com.zj.im.chat.enums.SendMsgState
import com.zj.im.chat.enums.SocketState
import com.zj.im.chat.exceptions.LooperInterruptedException
import com.zj.im.chat.hub.ClientHub
import com.zj.im.chat.hub.ServerHub
import com.zj.im.chat.modle.BaseMsgInfo
import com.zj.im.chat.modle.SendingUp
import com.zj.im.listeners.AppHiddenListener
import com.zj.im.listeners.DropRecoverListener
import com.zj.im.main.dispatcher.DataReceivedDispatcher
import com.zj.im.main.dispatcher.EventHub
import com.zj.im.main.impl.IMInterface
import com.zj.im.main.impl.RunningObserver
import com.zj.im.sender.OnSendBefore
import com.zj.im.sender.OnStatus
import com.zj.im.sender.SendExecutors
import com.zj.im.sender.SendingPool
import com.zj.im.utils.*
import com.zj.im.utils.Constance
import com.zj.im.utils.EfficiencyUtils
import com.zj.im.utils.TimeOutUtils
import com.zj.im.utils.log.NetRecordUtils
import com.zj.im.utils.log.logger.FileUtils
import com.zj.im.utils.log.logger.errorCollector
import com.zj.im.utils.log.logger.logUtils
import com.zj.im.utils.log.logger.printInFile
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
internal abstract class Runner<T> : RunningObserver(), OnStatus, (Boolean, BaseMsgInfo<T>, Throwable?) -> Unit {

    private var dataStore: DataStore<T>? = null
    private var msgLooper: MsgLooper? = null
    private var eventHub: EventHub<T>? = null
    private var sendingPool: SendingPool<T>? = null
    private var runningKey: String = ""
    private var curFrequency: RuntimeEfficiency = RuntimeEfficiency.MEDIUM
    private var curRunningKey: String = ""
    private var context: Application? = null
    private var isInit = false
    private var diskPathName: String = ""
    private var dropRecoverListener: DropRecoverListener? = null
    private var appHiddenListener: AppHiddenListener? = null

    protected var imi: IMInterface<T>? = null

    abstract fun initBase()

    fun init(imi: IMInterface<T>) {
        this.imi = imi
        if (isInit) {
            printInFile("ChatBase.IM", "SDK already init")
            imi.prepare()
            return
        }
        this.context = imi.option?.context
        printInFile("ChatBase.IM", " the SDK init with $runningKey")
        initUtils()
        initBase()
        initQueue()
        getServer("init and start")?.init(context)
        getClient("init.setRunnigKey and start")?.let {
            dataStore?.canSend { it.canSend() }
            dataStore?.canReceive { it.canReceived() }
        }
        initHandler()
        isInit = true
        imi.prepare()
    }

    private fun initUtils() {
        dropRecoverListener = DropRecoverListener.init(context) { onLayerChanged(false) }
        appHiddenListener = AppHiddenListener.init(context) { onLayerChanged(true) }
        imi?.let {
            val debugEnable = it.option?.debugEnable ?: false
            val logsCollectionAble = it.option?.logsCollectionAble ?: { false }
            val logsMaxRetain = it.option?.logsMaxRetain ?: Constance.MAX_RETAIN_TCP_LOG
            diskPathName = it.option?.logsFileName ?: ""
            logUtils.init(context, diskPathName, debugEnable, logsCollectionAble, logsMaxRetain)
            NetRecordUtils.init(context, diskPathName, debugEnable, logsCollectionAble, logsMaxRetain)
        }
    }

    private fun initHandler() {
        runningKey = getIncrementKey()
        curRunningKey = runningKey
        curFrequency = imi?.option?.runtimeEfficiency ?: RuntimeEfficiency.MEDIUM
        msgLooper = MsgLooper(curRunningKey, curFrequency.interval, this) {
            postError(LooperInterruptedException("thread has been destroyed!"))
        }
    }

    private fun initQueue() {
        eventHub = EventHub()
        dataStore = DataStore()
        sendingPool = SendingPool(this)
    }

    fun getClient(case: String): ClientHub<T>? {
        return imi?.getClient(case)
    }

    fun getServer(case: String): ServerHub<T>? {
        return imi?.getServer(case)
    }

    fun <R> sendTo(data: BaseMsgInfo<R>) {
        cast<BaseMsgInfo<R>, BaseMsgInfo<T>>(data)?.let {
            sendingPool?.push(it)
        }
    }

    fun <R> enqueue(data: BaseMsgInfo<R>) {
        cast<BaseMsgInfo<R>, BaseMsgInfo<T>>(data)?.let {
            msgLooper?.checkRunning(true)
            setLooperEfficiency(dataStore?.put(it) ?: 0)
        }
    }

    /**
     * send a msg
     * */
    fun send(data: T, callId: String, timeOut: Long, isResend: Boolean, isSpecialData: Boolean, ignoreConnecting: Boolean, sendBefore: OnSendBefore?) {
        DataReceivedDispatcher.pushData(BaseMsgInfo.sendingStateChange(SendMsgState.SENDING, callId, data, isResend))
        DataReceivedDispatcher.pushData(BaseMsgInfo.sendMsg(data, callId, timeOut, isResend, isSpecialData, ignoreConnecting, sendBefore, false))
    }

    private fun setLooperEfficiency(total: Int) {
        if (!StatusHub.isAlive()) return
        val e = if (DataReceivedDispatcher.isDataEnable()) {
            EfficiencyUtils.getEfficiency(total)
        } else {
            RuntimeEfficiency.MEDIUM
        }
        if (e != curFrequency) {
            curFrequency = e
            printInFile("ChatBase.SetLooperEfficiency", String.format(Constance.LOOPER_EFFICIENCY, curFrequency.name))
            msgLooper?.setFrequency(curFrequency.interval)
        }
    }

    /** ---------------------- */
    override fun run(runningKey: String) {
        if (runningKey != curRunningKey) {
            msgLooper?.shutdown()
            correctConnectionState(SocketState.CONNECTED_ERROR, "running key invalid")
            return
        }
        fun <N> with(block: () -> N): N? {
            return try {
                return block()
            } catch (e: Exception) {
                println("----- eeee run -- & ${e.message}")
                postError(e)
                null
            }
        }
        with {
            dataStore?.pop()?.let {
                eventHub?.handle(it)
            }
        }
        with {
            TimeOutUtils.updateNode()
        }
        with {
            sendingPool?.pop()?.let {
                sendingPool?.lock()
                SendExecutors(it, imi?.getServer("send"), this)
            }
        }
    }

    override fun invoke(isSuccess: Boolean, info: BaseMsgInfo<T>, e: Throwable?) {
        if (!isSuccess) {
            enqueue(BaseMsgInfo.sendingStateChange(SendMsgState.FAIL, info.callId, info.data, info.isResend))
        } else {
            printInFile("SendExecutors.send", "the data callId:${info.callId} has been send to socket")
        }
        sendingPool?.unLock()
    }

    override fun call(callId: String, progress: Int, isOK: Boolean, isCancel: Boolean) {
        if (isOK) {
            val sendState = if (!DataReceivedDispatcher.isDataEnable()) SendingUp.WAIT else SendingUp.CANCEL
            sendingPool?.setSendState(sendState, isCancel, callId)
        } else {
            enqueue(BaseMsgInfo.onProgressChange<T>(progress, callId))
        }
    }

    private fun onLayerChanged(isHidden: Boolean) {
        enqueue(BaseMsgInfo.onLayerChange<T>(isHidden))
    }

    fun correctConnectionState(state: SocketState, case: String) {
        enqueue(BaseMsgInfo.connectStateChange<T>(state, case))
    }

    fun postError(e: Throwable) {
        errorCollector.printInFile("postError", e.message, true)
        when (e) {
            is LooperInterruptedException -> {
                println("----- LooperInterruptedException!!!")
                if (!isFinishing(curRunningKey)) initHandler()
                else printInFile("ChatBase.IM.LooperInterrupted", " the MsgLooper was stopped by SDK shutDown")
            }
        }
        imi?.postError(e)
    }

    fun getLogsFolder(zipFolderName: String, zipFileName: String): String {
        if (zipFolderName.contains(".")) throw IllegalArgumentException("case: zipFolderName error : zip folder name can not contain with '.'")
        if (zipFolderName.contains(diskPathName)) throw IllegalArgumentException("case: zipFolderName error : zip folder can not create in log file")
        val path = FileUtils.getHomePath(diskPathName)
        if (path.isNotEmpty()) {
            val homeFile = File(path)
            if (homeFile.isDirectory) {
                val zipPath = FileUtils.getHomePath(zipFolderName)
                val zipName = "$zipFileName.zip"
                FileUtils.compressToZip(path, zipPath, zipName)
                val zipFile = File(zipPath, zipName)
                if (zipFile.exists() && zipFile.isFile) return zipFile.path
            }
        }
        return ""
    }

    fun deleteFormQueue(callId: String?) {
        dataStore?.deleteFormQueue(callId)
        sendingPool?.deleteFormQueue(callId)
    }

    fun queryInQueue(callId: String?): Boolean {
        return !callId.isNullOrEmpty() && dataStore?.queryInMsgQueue { it.callId == callId } == true || sendingPool?.queryInSendingQueue { it.callId == callId } == true
    }

    fun cancelMsgTimeOut(callId: String) {
        TimeOutUtils.remove(callId)
    }

    fun isFinishing(runningKey: String?): Boolean {
        return runningKey != this.runningKey
    }

    fun notify(): IMInterface<T>? {
        return imi
    }

    open fun shutDown() {
        printInFile("ChatBase.IM", " the SDK has begin shutdown with $runningKey")
        msgLooper?.shutdown()
        TimeOutUtils.clear()
        MainLooper.removeCallbacksAndMessages(null)
        getClient("shutdown call")?.shutdown()
        getServer("shutdown call")?.shutdown()
        runningKey = ""
        appHiddenListener?.shutDown(context)
        dropRecoverListener?.destroy()
        dataStore?.shutDown()
        sendingPool?.clear()
        dropRecoverListener = null
        appHiddenListener = null
        dataStore = null
        msgLooper = null
        eventHub = null
        sendingPool = null
        isInit = false
        printInFile("ChatBase.IM", " the SDK was shutdown")
    }
}