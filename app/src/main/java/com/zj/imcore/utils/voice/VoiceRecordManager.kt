package com.zj.imcore.utils.voice

import android.app.Application
import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Build
import com.zj.imcore.base.FCApplication
import java.io.File
import java.util.*

class VoiceRecordManager(context: Application, private val recordTempFileName: String, private val maxRecordTime: Long, private val updateInterval: Long) {

    private var recorder: MediaRecorder? = null
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var isRecording = false
    private var startTime: Long = 0
    private val recordFile: File
        get() {
            return File("${FCApplication.getCacheDir()}/$recordTempFileName/voice-record/${System.currentTimeMillis()}.amr")
        }

    private var volumeTimer: Timer? = null
    private var listener: RecordEventListener? = null

    fun start(listener: RecordEventListener) {
        if (isRecording) return
        startTime = System.currentTimeMillis()
        isRecording = true
        this.listener = listener
        this.recorder = MediaRecorder()
        try {
            recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
            recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder?.setOutputFile(recordFile.path)
            recorder?.prepare()
            recorder?.start()
            startSendVoiceVolume()
            requestFoucs()
        } catch (e: Throwable) {
            isRecording = false
            FCApplication.showToast("open voice failed")
        }
    }

    fun stopRecord(isInvalid: Boolean) {
        if (!isRecording) return
        try {
            stopSendVoiceVolume()
            recorder?.stop()
            recorder?.release()
            listener?.onEnd(recordFile.path, isInvalid)
            listener = null
            abandonFoucs()
        } catch (e: Throwable) {
        } finally {
            isRecording = false
        }
    }

    private fun startSendVoiceVolume() {
        if (volumeTimer == null) {
            volumeTimer = Timer()
        }
        volumeTimer?.schedule(object : TimerTask() {
            override fun run() {
                val maxAmplitude: Int = try {
                    recorder?.maxAmplitude ?: 0
                } catch (e: IllegalStateException) {
                    0
                }
                val interval = (System.currentTimeMillis() * 1.0f - startTime) / maxRecordTime
                listener?.onVoiceLevel(interval, maxRecordTime, (100f * (maxAmplitude / 32768f)).toInt())
            }
        }, updateInterval, updateInterval)
    }

    @Synchronized
    private fun stopSendVoiceVolume() {
        if (volumeTimer != null) {
            volumeTimer?.cancel()
            volumeTimer = null
        }
    }

    @Suppress("DEPRECATION")
    private fun requestFoucs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).build())
        } else {
            audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
        }
    }

    @Suppress("DEPRECATION")
    private fun abandonFoucs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).build())
        } else {
            audioManager.abandonAudioFocus { }
        }
    }

    private val onAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener {}

    interface RecordEventListener {

        fun onVoiceLevel(interval: Float, max: Long, level: Int)

        fun onEnd(outputFilePath: String, isInvalid: Boolean)
    }
}