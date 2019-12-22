package com.zbl.api.interfaces

interface ErrorHandler {
    fun onError(throwable: Throwable)
}
