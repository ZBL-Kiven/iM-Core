package com.zj.im.store.interfaces

internal abstract class DataHandler<R> {
    abstract fun onDataGot(r: R?)
}
