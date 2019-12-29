package com.zj.im.img.cache

import android.content.Context
import android.graphics.Bitmap
import com.zj.im.img.CacheAble
import java.io.File
import java.util.concurrent.Executors
import java.util.regex.Pattern
import kotlin.IllegalArgumentException


/**
 * Created by ZJJ on 19/12/13
 *
 * Picture of any size multi-level buffer,
 *
 * By calling load () normally, the size after this resize is automatically reserved for the image,
 *
 * When loading original images of the same size, thumbnails will be automatically obtained,
 *
 * Avoid taking out the original image and recalculating the compression every time you load it
 *
 * Especially suitable for display the IM thumb image list and UserAvatar
 *
 * Support local pictures, resource files, network pictures
 * */
@Suppress("unused")
abstract class ImageCacheUtil(private val context: Context, private val w: Int, private val h: Int, private val quality: Float, private val cache: CacheAble, private val fillType: Int = DEFAULT, private val payloads: String? = null) {

    companion object {
        const val CENTER_CROP = 1
        const val FIT_CENTER = 0
        const val CIRCLE = 2
        const val CENTER_INSIDE = 3
        const val DEFAULT = -1
        private val imageSaverService = Executors.newFixedThreadPool(5)
    }

    abstract fun getCacheDir(context: Context): String
    abstract fun loadImgForOriginal(cacheOriginalPath: String, w: Int, h: Int, fillType: Int, onResult: (Bitmap?) -> Unit)

    protected fun getCache(): CacheAble {
        return cache
    }

    protected fun getContext(): Context {
        return context
    }

    protected fun getPayloads(): String? {
        return payloads
    }

    fun load(onGot: ((String) -> Unit)) {
        val cached = cache
        if (w <= 0) throw IllegalArgumentException("the load width must not be a zero or negative number")
        if (h <= 0) throw IllegalArgumentException("the load height must not be a zero or negative number")
        val cacheDir = getCacheDir(context)
        val cacheOriginalPath = cached.getOriginalPath(payloads)
        if (cacheDir.isEmpty()) {
            onGot("the cache directory name was null or empty!!")
            return
        }
        if (cacheOriginalPath.isNullOrEmpty()) {
            onGot("the original path was null or empty!!")
            return
        }
        val fName = getFileName(cacheOriginalPath)
        if (!fName.isNullOrEmpty()) {
            val fileName = "${quality}_$w*$h-$fName"
            val cachedFolder = File(cacheDir)
            if (cachedFolder.exists()) {
                val cachedFile = File(cachedFolder, fileName)
                if (cachedFile.exists()) {
                    onGot(cachedFile.path)
                    return
                } else {
                    if (cachedFolder.exists()) {
                        cachedFolder.delete()
                    }
                }
            }
            if (quality !in 0f..1f) {
                throw IllegalArgumentException("error operations quality for image ,the quality must in 0.0f to 1.0f")
            }
            val cacheWidth = (w * quality).toInt()
            val cacheHeight = (h * quality).toInt()
            loadImgForOriginal(cacheOriginalPath, cacheWidth, cacheHeight, fillType) { bmp ->
                if (bmp == null) {
                    onGot("")
                } else imageSaverService.submit(ImageSaveTask(bmp, cacheDir, fileName) {
                    onGot.invoke(it)
                    if (!bmp.isRecycled) {
                        bmp.recycle()
                    }
                })
            }
        } else {
            /**
             * cancel the multi cache , because source is gif or another unsupported types, or may redirect in server
             */
            onGot(cacheOriginalPath)
        }
    }

    private fun getFileName(url: String): String? {
        val suffixes = "jpeg|gif|jpg|png"
        val file = url.substring(url.lastIndexOf('/') + 1)
        val pat = Pattern.compile("[*\\w|=&]+[.]($suffixes)")
        val mc = pat.matcher(file)
        while (mc.find()) {
            return mc.group()
        }
        return null
    }
}