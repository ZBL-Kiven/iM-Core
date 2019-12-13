package com.zj.im.img.cache.loader

import android.content.Context
import com.zj.im.img.CacheAble
import com.zj.im.img.cache.ImageCacheUtil

class AvatarLoadUtil(context: Context, w: Int, h: Int, cache: CacheAble, payloads: String? = null) : ImageCacheUtil(context, w, h, cache, payloads) {

    private val cacheDir = "${context.getExternalFilesDir(getCache().getCacheName(getPayloads()))?.path}/avatar/"

    override fun getCacheDir(context: Context): String {
        return cacheDir
    }
}