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
import com.zj.imcore.mod.MsgInfo
import com.zj.imcore.mod.MsgReceivedInfo
import kotlinx.android.synthetic.main.activity_main.*

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

        val l = this.registerMsgReceivedListener<MsgReceivedInfo, MsgInfo>("aaa").addHandler(TestHandler()).subscribe(object : DataListener<MsgInfo>() {
            override fun onReceived(data: MsgInfo) {
                main_rv_msgBar.adapter.data().add("aa", data)
                main_rv_msgBar.stopScroll()
                val p = main_rv_msgBar.adapter.data().maxCurDataPosition()
                main_rv_msgBar.smoothScrollToPosition(p)
            }
        })
        l.lock(false)
    }

    private fun initView() {
        main_rv_msgBar?.itemAnimator = null
        sendMsg?.setOnClickListener {
            val callId = IMClient.getRandomCallId()
            val vid = "=bvwBLyAvD"
            val timeOut = TCP_TIME_OUT
            val text = et?.text.toString()
            @Suppress("SpellCheckingInspection") SendObject.create(callId).put("type", "message").put("vchannel_id", vid).put("text", text).put("subtype", "normal").putAll(makeSentParams(callId)).timeOut(timeOut).build().send()
        }
        receiveMock?.setOnClickListener {
            receiveMock?.isEnabled = false
            mutableListOf<MsgReceivedInfo>().let {
                val r = java.util.Random()
                for (i in 0 until 1) {
                    val msg = MsgInfo()
                    msg.text = "this is data $i "
                    it.add(MsgReceivedInfo(msg, r.nextBoolean(), (System.currentTimeMillis()) + i))
                }
                UIHelper.postReceiveData(it)
            }
            receiveMock?.isEnabled = true
        }
    }

    override fun finish() {
        IMHelper.removeSocketStateChangeListener(javaClass.simpleName)
        super.finish()
    }
}