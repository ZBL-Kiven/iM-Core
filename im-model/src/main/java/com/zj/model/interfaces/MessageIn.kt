package com.zj.model.interfaces

@Suppress("unused")
interface MessageIn {

    fun vChannelId(): String?

    fun subType(): String?

    fun subTypeDetail(): String?

    fun text(): String?

    fun createdTs(): Long

    fun uid(): String?

    fun referKey(): String

    fun starId(): String?

    fun deleted(): Boolean

    fun textColor(): String?

    fun tsColor(): String?

    fun bubbleColor(): String?

    fun localCreatedTs(): Long

    fun callId(): String?

    fun key(): String

    fun getAvatarUrl(): String?

    fun getName(): String?

    /**------- */

    fun getStickerUrl(): String?

    fun getStickerWidth(): Int
    fun getStickerHeight(): Int

    /**------- */

    fun getImageUrl(): String?

    fun getImageWidth(): Int
    fun getImageHeight(): Int

    /**------- */

    fun getVoiceUrl(): String?

    fun getVoiceDuration(): Long

    /**------- */

    fun getFileUrl(): String?

    fun getFileSize(): Long

    /**------- */

    fun getVideoUrl(): String?

    fun getVideoThumb(): String?

    fun getVideoThumbWidth(): Int

    fun getVideoThumbHeight(): Int

    fun getVideoDuration(): Long

    /**----- packing ignore -----*/

    //send status
    fun sendingState()

    //If the file is sent, the local path of the file needs to be saved for retransmission
    fun localFilePath(): String?

    /** -------- db ignore properties ------- */

}