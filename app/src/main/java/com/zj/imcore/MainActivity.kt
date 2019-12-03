package com.zj.imcore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zj.im.main.UIHelper
import com.zj.im.registerMsgReceivedListener
import com.zj.im.sender.SendObject
import com.zj.im.store.interfaces.DataListener
import com.zj.imcore.options.IMClient
import com.zj.imcore.options.IMClient.Companion.TCP_TIME_OUT
import com.zj.imcore.options.IMHelper
import com.zj.imcore.renderer.TestHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initView()
        register()
    }

    private fun init() {
        IMHelper.init(this.application)
    }

    private fun register() {
        IMHelper.registerSocketStateChangeListener(javaClass.simpleName) {
            main_status?.text = it.name
        }

        val l = this.registerMsgReceivedListener<Int, String>("aaa").addHandler(TestHandler()).subscribe(object : DataListener<String>() {
                override fun onReceived(data: String) {
                    main_msgBar?.append("$data\n")
                }
            })
        l.lock(false)
    }

    private fun initView() {
        sendMsg?.setOnClickListener {
            val callId = IMClient.getRandomCallId()
            val vid = "=bvwBLyAvD"
            val timeOut = TCP_TIME_OUT
            val text = et?.text.toString()
            SendObject.create(callId).put("type", "message").put("vchannel_id", vid).put("text", text).put("subtype", "normal").putAll(makeSentParams(callId)).timeOut(timeOut).build().send()
        }
        receiveMock?.setOnClickListener {
            mutableListOf<Int>().apply {
                val r = Random()
                for (i in 0..10000) {
                    add(r.nextInt(100000))
                }
                UIHelper.postReceiveData(this)
            }
        }
    }

    override fun finish() {
        IMHelper.removeSocketStateChangeListener(javaClass.simpleName)
        super.finish()
    }
}