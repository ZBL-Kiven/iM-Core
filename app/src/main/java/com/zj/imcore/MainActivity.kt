package com.zj.imcore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zj.im.sender.SendObject
import com.zj.imcore.options.IMClient
import com.zj.imcore.options.IMClient.Companion.TCP_TIME_OUT
import com.zj.imcore.options.IMHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IMHelper.init(this.application)

        sendMsg?.setOnClickListener {
            val callId = IMClient.getRandomCallId()
            val vid = "=bvwBLyAvD"
            val timeOut =TCP_TIME_OUT
            val text = et?.text.toString()
            SendObject.create(callId)
                .put("type", "message")
                .put("vchannel_id", vid)
                .put("text", text)
                .put("subtype", "normal")
                .putAll(makeSentParams(callId))
                .timeOut(timeOut)
                .build()
                .send()
        }
    }

    private fun makeSentParams(callId: String, localFilePath: String? = null): Map<String, Any> {
        val map = hashMapOf<String, Any>()
        val uid = "=bwNpr"
        map["call_id"] = callId
        map["local_created_ts"] = System.currentTimeMillis()
        if (localFilePath != null) map["localFilePath"] = localFilePath
        map["uid"] = uid
        return map
    }
}
