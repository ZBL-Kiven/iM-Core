package com.zj.im.img

interface StorageAble {

    fun getCacheWithParam(w: Int, h: Int, cache: CacheAble, onGot: (String) -> Unit)

}