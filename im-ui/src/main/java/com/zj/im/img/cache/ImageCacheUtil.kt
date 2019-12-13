package com.zj.im.img.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zj.im.img.CacheAble
import java.io.File
import java.lang.IllegalArgumentException
import java.util.concurrent.Executors
import java.util.regex.Pattern


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
abstract class ImageCacheUtil(private val context: Context, private val w: Int, private val h: Int, private val cache: CacheAble, private val payloads: String? = null) {

    companion object {
        private val imageSaverService = Executors.newFixedThreadPool(5)

    }

    abstract fun getCacheDir(context: Context): String

    protected fun getCache(): CacheAble {
        return cache
    }

    protected fun getContext(): Context {
        return context
    }

    protected fun getPayloads(): String? {
        return payloads
    }

    private var onGot: ((String) -> Unit)? = null

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
        if (cacheOriginalPath.isEmpty()) {
            onGot("the original path was null or empty!!")
            return
        }
        this.onGot = onGot
        val fName = getFileName(cacheOriginalPath)
        if (!fName.isNullOrEmpty()) {
            val fileName = "_$w*$h-$fName"
            val cachedFolder = File(cacheDir)
            if (cachedFolder.exists()) {
                val cachedFile = File(cachedFolder, fileName)
                if (cachedFile.exists()) {
                    onGot(cachedFile.path)
                    return
                } else {
                    if (cachedFolder.exists()) cachedFolder.delete()
                }
            }
            loadImgForOriginal(cacheDir, cacheOriginalPath, fileName)
        } else {
            /**
             * cancel the multi cache , because source is gif or another unsupported types, or may redirect in server
             */
            onGot(cacheOriginalPath)
        }
    }

    private fun loadImgForOriginal(cacheDir: String, cacheOriginalPath: String, fileName: String) {
        Glide.with(context).asBitmap().load(cacheOriginalPath).into(object : CustomTarget<Bitmap>(w, h) {

            override fun onLoadCleared(placeholder: Drawable?) {
                onGot?.invoke("")
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageSaverService.submit(ImageSaveTask(resource, cacheDir, fileName) {
                    onGot?.invoke(it)
                })
            }
        })
    }

    private fun getFileName(url: String): String? {
        val suffixes = "jpeg|gif|jpg|png"
        val file = url.substring(url.lastIndexOf('/') + 1)
        println(file)
        val pat = Pattern.compile("[*\\w|=&]+[.]($suffixes)")
        val mc = pat.matcher(file)
        while (mc.find()) {
            return mc.group()
        }
        return null
    }
}