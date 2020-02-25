package com.zj.model.interfaces

@Suppress("unused")
interface MessageIn {

    fun dialogId(): String

    fun subType(): String?

    fun subTypeDetail(): String?

    fun text(): String?

    fun createdTs(): Long

    fun uid(): String

    fun referKey(): String

    fun starId(): String?

    fun deleted(): Boolean

    fun localCreatedTs(): Long

    fun callId(): String?

    fun key(): Long

    fun getAvatarUrl(): String?

    fun getName(): String?

    /**---- sticker ---- */

    fun getStickerUrl(): String?

    fun getStickerWidth(): Int
    fun getStickerHeight(): Int

    /**---- image ---- */

    fun getImageUrl(): String?
    fun getImageWidth(): Int
    fun getImageHeight(): Int

    /**---- voice ---- */

    fun getVoiceUrl(): String?

    fun getVoiceDuration(): Long

    /**--- file ---- */

    fun getFileUrl(): String?

    fun getFileSize(): Long

    /**---- video --- */

    fun getVideoUrl(): String?

    fun getVideoThumb(): String?

    fun getVideoThumbWidth(): Int

    fun getVideoThumbHeight(): Int

    fun getVideoDuration(): Long

    /**----- packing ignore -----*/

    //send status
    fun sendingState():Int

    //If the file is sent, the local path of the file needs to be saved for retransmission
    fun localFilePath(): String?

}