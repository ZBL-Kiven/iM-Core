package com.zj.im.net.tasks

import java.io.IOException
import java.lang.ref.WeakReference
import java.net.Socket
import java.util.concurrent.Callable

/**
 * Created by ZJJ
 */

class SendMessageTask(private val socket: WeakReference<Socket?>, val params: ByteArray) : Callable<Throwable> {

    override fun call(): Throwable? {
        return try {
            val outputStream = socket.get()?.getOutputStream()
            outputStream?.write(params)
            outputStream?.flush()
            null
        } catch (e: IOException) {
            e
        }
    }
}
