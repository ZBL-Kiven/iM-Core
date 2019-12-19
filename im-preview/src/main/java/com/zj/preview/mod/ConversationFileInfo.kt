package com.zj.preview.mod

import androidx.lifecycle.LiveData
import java.io.Serializable

/**
 * @author ZJJ on 2019.10.24
 * */
@Suppress("unused")
data class ConversationFileInfo(val type: SourceType, val originalPath: String, val localPath: String, val duration: Long, val imagePath: String) : Serializable {

    fun getPath(): String {
        if (localPath.isNotEmpty()) {
            //            return localPath
            return "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4"
        }

        return VIDEO_IS_NOT_PREPARE
    }

    val downloadInfo: LiveData<DownloadInfo>? = null

    fun isVideo(): Boolean {
        return type == SourceType.VIDEO
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ConversationFileInfo) return false
        return type == other.type && originalPath == other.originalPath || localPath == other.localPath
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + originalPath.hashCode()
        return result
    }

    companion object {
        const val VIDEO_IS_NOT_PREPARE = "videoIsNotPrepare"
    }
}
