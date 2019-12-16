package com.zj.imcore.enums

@Suppress("unused")
enum class MsgType(private val subName: String) {

    FILE("file"), NORMAL("normal"), INFO("info"), VOICE("voice"), STICKER("sticker");

    fun eq(name: String?): Boolean {
        return name.equals(this.subName, true)
    }

    companion object {
        fun isMsg(name: String?): Boolean {
            return NORMAL.eq(name) || FILE.eq(name) || VOICE.eq(name) || STICKER.eq(name)
        }
    }


}