package com.zj.imcore.ui.chat

import com.zj.im.store.interfaces.DataListener
import com.zj.model.mod.MsgInfo
import com.zj.imcore.base.FCActivity
import com.zj.imcore.options.IMHelper
import com.zj.imcore.registerTcpReceivedListener
import com.zj.imcore.renderer.TestHandler
import com.zj.imcore.ui.main.conversation.ConversationFragment
import com.zj.model.interfaces.MessageIn

class ChatActivity :FCActivity(){

    companion object{
        fun start(conversationFragment: ConversationFragment, id: String, userId: String?, draft: String?, title: String) {

        }
    }

    private fun register() {
        IMHelper.registerSocketStateChangeListener(javaClass.simpleName) {
            //            main_status?.text = it.name
        }

        val l = registerTcpReceivedListener<MessageIn, MsgInfo>("aaa").addHandler(TestHandler()).subscribe(object : DataListener<MsgInfo>() {
            override fun onReceived(data: MsgInfo) {
//                                main_rv_msgBar.adapter.data().add("aa", data)
//                                main_rv_msgBar.stopScroll()
//                                val p = main_rv_msgBar.adapter.data().maxCurDataPosition()
//                                main_rv_msgBar.scrollToPosition(p)
            }
        })
        l.lock(false)
    }

    override fun getContentId(): Int {
        return 0
    }

    override fun initListener() {
    }

    override fun initData() {

    }

    override fun initView() {

    }

    //
    //    override fun initView() {
    //        main_rv_msgBar?.itemAnimator = null
    //        sendMsg?.setOnClickListener {
    //            it.postInvalidate()
    //            val callId = IMClient.getRandomCallId()
    //            val vid = "=bvwBLyAvD"
    //            val timeOut = IMClient.TCP_TIME_OUT
    //            val text = et?.text.toString()
    //            @Suppress("SpellCheckingInspection") SendObject.create(callId).put("type", "message").put("vchannel_id", vid).put("text", text).put("subtype", "normal").putAll(makeSentParams(callId)).timeOut(timeOut).build().send()
    //        }
    //        receiveMock?.setOnClickListener {
    //            receiveMock?.isEnabled = false
    //            mutableListOf<MsgReceivedInfo>().let {
    //                val r = java.util.Random()
    //                for (i in 0 until 10) {
    //                    val msg = MsgInfo()
    //                    msg.text = "this is data $i "
    //                    it.add(MsgReceivedInfo(msg, r.nextBoolean(), (System.currentTimeMillis()) + i))
    //                }
    //                UIHelper.postReceiveData(it)
    //            }
    //            receiveMock?.isEnabled = true
    //        }
    //    }
    //
    //    override fun finish() {
    //        IMHelper.removeSocketStateChangeListener(javaClass.simpleName)
    //        super.finish()
    //    }
}