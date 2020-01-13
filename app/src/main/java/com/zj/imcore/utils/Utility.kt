package com.zj.imcore.utils

import android.app.Application
import com.zj.imcore.utils.voice.VoiceRecordManager

object Utility {

    private const val MAX_RECORD_TIME = 30000L
    private const val VOICE_UPDATE_INTERVAL = 64L

    var voiceManager: VoiceRecordManager? = null


    fun init(app: Application, tempFileName: String) {
        voiceManager = VoiceRecordManager(app, tempFileName, MAX_RECORD_TIME, VOICE_UPDATE_INTERVAL)
    }



}