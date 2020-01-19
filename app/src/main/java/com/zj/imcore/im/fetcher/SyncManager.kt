package com.zj.imcore.im.fetcher

import com.zj.im.chat.enums.SocketState
import com.zj.imcore.base.FCApplication
import com.zj.imcore.im.options.IMHelper
import java.util.concurrent.ConcurrentHashMap

object SyncManager {

    private const val SOCKET_STATE_LISTEN = "SyncManagerSocketStateKey"
    private const val FETCH_MSG_PAUSE_CODE = 0x101a
    private const val FETCH_RETRY_COUNT = 3

    private var fetcher: Fetcher? = null
    private var isCompleted: ((String, Boolean) -> Unit)? = null
    private val fetchRecord: ConcurrentHashMap<String, Int> = ConcurrentHashMap()

    fun init() {
        IMHelper.registerSocketStateChangeListener(SOCKET_STATE_LISTEN) {
            if (it == SocketState.CONNECTED) {
                IMHelper.pause(FETCH_MSG_PAUSE_CODE)
                onFetchStart { s, b ->
                    if (b) {
                        fetchRecord.remove(s)
                        IMHelper.resume(FETCH_MSG_PAUSE_CODE)
                    } else {
                        val count = fetchRecord[s] ?: 0
                        fetchRecord[s] = count + 1
//                        if (count < FETCH_RETRY_COUNT) IMHelper.reconnect("fetcher $s failed ,retrying on $count ")
//                        else {
//                            shutdown()
//                            FCApplication.logout("fetch $s failed ,clear cache and require necessary to relogin")
//                        }
                    }
                }
            }
        }
    }

    private fun onFetchStart(isContinue: (String, Boolean) -> Unit) {
        if (fetcher != null) {
            fetcher?.shutdown()
            fetcher = null
        }
        isCompleted = isContinue
        fetcher = Fetcher { s, b ->
            isCompleted?.invoke(s, b)
        }
    }

    fun shutdown() {
        IMHelper.removeSocketStateChangeListener(SOCKET_STATE_LISTEN)
        if (fetcher != null) {
            fetcher?.shutdown()
            fetcher = null
        }
        isCompleted = null
    }
}