@file:Suppress("unused")

package com.zj.im.chat.core

import com.zj.im.chat.exceptions.ChatException
import com.zj.im.chat.hub.ClientHub
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

abstract class OnBuildOption {

    abstract fun getClient(): ClientHub

    abstract fun getServer(): ServerHub

    abstract fun onError(e: ChatException)

    open fun checkNetWorkIsWorking(): Boolean {
        return true
    }

    open fun prepare() {}

    open fun shutdown() {}

    open fun onLayerChanged(inBackground: Boolean) {}
}
