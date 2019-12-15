package com.zj.imcore.utils.img.loader

import android.content.Context
import com.zj.im.img.CacheAble

class ImageMsgLoadUtil(context: Context, w: Int, h: Int, quality: Float, cache: CacheAble, fillType: Int, payloads: String? = null) : BaseGlideLoader(context, w, h, quality, cache, fillType, payloads) {
    private val cacheDir = "${context.getExternalFilesDir("conversation")?.path}"

    override fun getCacheDir(context: Context): String {
        return cacheDir
    }
}