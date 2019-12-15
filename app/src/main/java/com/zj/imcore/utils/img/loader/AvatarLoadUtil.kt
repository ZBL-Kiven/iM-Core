package com.zj.imcore.utils.img.loader

import android.content.Context
import com.zj.im.img.CacheAble

class AvatarLoadUtil(context: Context, w: Int, h: Int, quality: Float, cache: CacheAble, fillType: Int = DEFAULT, payloads: String? = null) : BaseGlideLoader(context, w, h, quality, cache, fillType, payloads) {

    private val cacheDir = "${context.getExternalFilesDir(getCache().getCacheName(getPayloads()))?.path}/avatar/"

    override fun getCacheDir(context: Context): String {
        return cacheDir
    }
}