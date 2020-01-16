package com.zj.imcore.im.fetcher

import com.zj.im.chat.enums.SocketState
import com.zj.imcore.im.options.IMHelper

object SyncManager {

    private const val SOCKET_STATE_LISTEN = "SyncManagerSocketStateKey"
    private const val FETCH_MSG_PAUSE_CODE = 0x101a

    fun init() {
        IMHelper.registerSocketStateChangeListener(SOCKET_STATE_LISTEN) {
            if (it == SocketState.CONNECTED) {
                IMHelper.pause(FETCH_MSG_PAUSE_CODE)
                onFetchStart {
                    IMHelper.resume(FETCH_MSG_PAUSE_CODE)
                }
            }
        }
    }

    fun shutdown() {
        IMHelper.removeSocketStateChangeListener(SOCKET_STATE_LISTEN)
    }

    private fun onFetchStart(isContinue: () -> Unit) {




        isContinue()
    }
}