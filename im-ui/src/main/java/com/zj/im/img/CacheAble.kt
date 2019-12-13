package com.zj.im.img

interface CacheAble {

    /**
     * the cache folder name
     * */
    fun getCacheName(payloads: String?): String

    /**
     * the data original path or url
     * */
    fun getOriginalPath(payloads: String?): String

}