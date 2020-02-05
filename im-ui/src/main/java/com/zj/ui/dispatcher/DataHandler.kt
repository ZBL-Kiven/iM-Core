package com.zj.ui.dispatcher

interface DataHandler<DATA, R> {
    fun handle(data: DATA): R
}