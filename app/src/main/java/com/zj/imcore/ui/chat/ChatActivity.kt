package com.zj.imcore.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.gson.Gson
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.base.view.BaseTitleView
import com.zj.im.dispatcher.addReceiveObserver
import com.zj.im.log
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCActivity
import com.zj.imcore.base.FCApplication
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.im.options.mod.BaseMod
import com.zj.imcore.im.transfer.DataTransferHub
import com.zj.imcore.ui.views.IMRecyclerView
import com.zj.model.mod.MessageBean
import java.lang.Exception
import java.util.*

class ChatActivity : FCActivity() {

    companion object {

        private const val SESSION_ID = "session_id"
        private const val USER_ID = "user_id"
        private const val DRAFT = "draft"
        private const val TITLE = "title"

        private const val NORMAL = "normal"
        private const val FAILED = "failed"

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
        try {
            intent?.let {
                if (it.hasExtra(SESSION_ID)) sessionId = it.getStringExtra(SESSION_ID) ?: ""
                if (it.hasExtra(USER_ID)) uid = it.getStringExtra(USER_ID) ?: ""
                if (it.hasExtra(DRAFT)) draft = it.getStringExtra(DRAFT) ?: ""
                if (it.hasExtra(TITLE)) conversasionTitle = it.getStringExtra(TITLE) ?: ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (sessionId.isEmpty() && uid.isEmpty()) {
            FCApplication.showToast(getString(R.string.app_chat_error_empty_target))
            return
        }
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
        this@ChatActivity.addReceiveObserver<MsgInfo>(Constance.REG_CODE_CHAT_ACTIVITY_MESSAGE).listen { data ->
            log("11111 ${data.text}  ${data.sendingState}")
            if (!isFinishing) rvContent?.let {
                it.stopScroll()
                it.adapter.data().add(NORMAL, data)
                val p = it.adapter.data().maxCurDataPosition()
                handler.removeMessages(1999)
                val msg = Message.obtain()
                msg.what = 1999
                msg.arg1 = p
                handler.sendMessageDelayed(msg, 30)
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper()) {
        if (it.what == 1999) {
            rvContent?.scrollToPosition(it.arg1)
        }
        return@Handler false
    }

    private fun sendText(text: String) {
        val callId = UUID.randomUUID().toString()
        val baseSendInfo = BaseMod()
        val m = MessageBean().apply {
            this.subtype = "normal"
            this.uid = SPUtils_Proxy.getUserId("0").toInt()
            this.team_id = 1
            this.dialog_id = sessionId.toLong()
            this.text = text
            this.callId = callId
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