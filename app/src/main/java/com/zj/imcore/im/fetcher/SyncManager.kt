package com.zj.imcore.im.fetcher

import com.zj.im.chat.enums.SocketState
import com.zj.imcore.im.options.IMHelper

object SyncManager {

    private const val SOCKET_STATE_LISTEN = "SyncManagerSocketStateKey"
    private const val FETCH_MSG_PAUSE_CODE = 0x101a

    private var fetcher: Fetcher? = null
    private var isCompleted: ((Boolean) -> Unit)? = null

    fun init() {
        IMHelper.registerSocketStateChangeListener(SOCKET_STATE_LISTEN) {
            if (it == SocketState.CONNECTED) {
                IMHelper.pause(FETCH_MSG_PAUSE_CODE)
                onFetchStart { s ->
                    if (s) IMHelper.resume(FETCH_MSG_PAUSE_CODE)
                    else IMHelper
                }
            }
        }
    }

    private fun onFetchStart(isContinue: (Boolean) -> Unit) {
        if (fetcher != null) {
            fetcher?.shutdown()
        }
        isCompleted = isContinue
        fetcher = Fetcher {
            isCompleted?.invoke(it)
        }
    }

    fun shutdown() {
        if (fetcher != null) {
            fetcher?.shutdown()
        }
        isCompleted = null
        IMHelper.removeSocketStateChangeListener(SOCKET_STATE_LISTEN)
    }
}