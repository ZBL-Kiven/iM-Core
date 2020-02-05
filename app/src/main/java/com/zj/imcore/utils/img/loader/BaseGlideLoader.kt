package com.zj.imcore.utils.img.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zj.ui.img.CacheAble
import com.zj.ui.img.cache.ImageCacheUtil

abstract class BaseGlideLoader(context: Context, w: Int, h: Int, quality: Float, cache: CacheAble, fillType: Int, payloads: String? = null) : ImageCacheUtil(context, w, h, quality, cache, fillType, payloads) {

    override fun loadImgForOriginal(cacheOriginalPath: String, w: Int, h: Int, fillType: Int, onResult: (Bitmap?) -> Unit) {

        fun <T : BaseRequestOptions<T>> getFillType(t: T): BaseRequestOptions<T> {
            return when (fillType) {
                FIT_CENTER -> t.fitCenter()
                CENTER_CROP -> t.centerCrop()
                CIRCLE -> t.circleCrop()
                CENTER_INSIDE -> t.centerInside()
                else -> t
            }
        }

        getFillType(Glide.with(getContext()).asBitmap().load(cacheOriginalPath).override(w, h)).skipMemoryCache(true).into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {
                onResult(null)
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                onResult(resource)
            }
        })
    }
}