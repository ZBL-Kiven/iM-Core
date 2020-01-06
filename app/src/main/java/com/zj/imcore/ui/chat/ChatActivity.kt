package com.zj.imcore.ui.chat

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.gson.Gson
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.base.view.BaseTitleView
import com.zj.im.store.interfaces.DataListener
import com.zj.imcore.R
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCActivity
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.im.options.mod.BaseMod
import com.zj.imcore.im.renderer.MsgHandler
import com.zj.imcore.im.transfer.DataTransferHub
import com.zj.imcore.registerTcpReceivedListener
import com.zj.imcore.ui.views.IMRecyclerView
import com.zj.model.mod.MessageBean
import java.util.*

class ChatActivity : FCActivity() {

    companion object {

        private const val SESSION_ID = "session_id"
        private const val USER_ID = "user_id"
        private const val DRAFT = "draft"
        private const val TITLE = "title"

        private const val NORMAL = "normal"
        private const val ARCHIVE = "archive"
        private const val HISTORY = "history"

        fun start(activity: Activity?, id: String, userId: String?, draft: String?, title: String) {
            val i = Intent(activity, ChatActivity::class.java)
            i.putExtra(SESSION_ID, id)
            i.putExtra(USER_ID, userId)
            i.putExtra(DRAFT, draft)
            i.putExtra(TITLE, title)
            activity?.startActivity(i)
        }
    }

    private var uid = ""
    private var sessionId = ""
    private var draft = ""
    private var conversasionTitle = ""

    private var rvContent: IMRecyclerView? = null
    private var vVoice: View? = null
    private var vEmoji: View? = null
    private var vMore: View? = null
    private var etInput: EditText? = null

    override fun getContentId(): Int {
        return R.layout.app_act_chat_content
    }

    override fun initView() {
        rvContent = findViewById(R.id.app_act_chat_content_rv)
        vVoice = findViewById(R.id.app_act_chat_content_btn_voice)
        vEmoji = findViewById(R.id.app_act_chat_content_btn_emoji)
        vMore = findViewById(R.id.app_act_chat_content_btn_more)
        etInput = findViewById(R.id.app_act_chat_content_et_input)
        showTitleBar(true)
    }

    override fun initTitleBar(baseTitleView: BaseTitleView?) {
        super.initTitleBar(baseTitleView)
        baseTitleView?.setLeftIcon(R.mipmap.app_back)
        baseTitleView?.setLeftClickListener { finish() }
    }

    override fun initData() {
        setTitle("aa")
        register()
        DataTransferHub.queryMsgInDb("", "")
    }

    override fun initListener() {
        etInput?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val text = v.text.toString()
                if (text.isEmpty()) return@setOnEditorActionListener false
                v.text = ""
                sendText(text)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun register() {
        IMHelper.registerSocketStateChangeListener(javaClass.simpleName) {
            setTitle(it.name)
        }
        val l = registerTcpReceivedListener<MessageBean, MsgInfo>("aaa").addHandler(MsgHandler()).subscribe(object : DataListener<MsgInfo>() {
            override fun onReceived(data: MsgInfo) {
                println("----- 5555 ${data.sendingState}")
                rvContent?.let {
                    it.stopScroll()
                    it.adapter.data().add(NORMAL, data)
                    val p = it.adapter.data().maxCurDataPosition()
                    it.scrollToPosition(p)
                }
            }
        })
        l.lock(false)
    }

    private fun sendText(text: String) {
        val callId = UUID.randomUUID().toString()
        val baseSendInfo = BaseMod()
        val m = MessageBean().apply {
            this.subtype = "normal"
            this.uid = SPUtils_Proxy.getUserId("0").toInt()
            this.team_id = 1
            this.dialog_id = 8589934596
            this.text = text
        }
        baseSendInfo.data = m
        baseSendInfo.callId = callId
        baseSendInfo.type = "create_message"
        val data = Gson().toJson(baseSendInfo)
        IMHelper.send(data, callId, 10000, false, false, null)
    }

    override fun finish() {
        IMHelper.removeSocketStateChangeListener(javaClass.simpleName)
        super.finish()
    }
}