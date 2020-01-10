package com.zj.im.dispatcher

interface DataHandler<DATA, R> {
    fun handle(data: DATA): R
}