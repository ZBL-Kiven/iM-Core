package com.zj.imcore.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.KeyEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zj.base.view.BaseTitleView
import com.zj.im.dispatcher.addReceiveObserver
import com.zj.im.emotionboard.CusEmoticonsLayout
import com.zj.im.emotionboard.utils.EmoticonsKeyboardUtils
import com.zj.im.emotionboard.widget.FuncLayout
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCApplication
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.im.transfer.DataTransferHub
import com.zj.imcore.ui.chat.emoticon.AdapterUtils
import com.zj.imcore.ui.chat.emoticon.OnEmojiClickListener
import com.zj.imcore.ui.chat.func.FuncEventListener
import com.zj.imcore.ui.chat.func.FuncGridView
import com.zj.imcore.ui.chat.func.FuncsAdapter
import com.zj.imcore.ui.views.IMRecyclerView
import java.lang.Exception

class ChatActivity : AppCompatActivity(), FuncLayout.FuncKeyBoardListener {

    companion object {

        private const val SESSION_ID = "session_id"
        private const val USER_ID = "user_id"
        private const val DRAFT = "draft"
        private const val TITLE = "title"

        private const val NORMAL = "normal"

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
    private var titleView: BaseTitleView? = null
    private var celBar: CusEmoticonsLayout? = null
    private var onFuncListener: FuncsAdapter.OnItemClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_act_chat_content)
        initView()
        initData()
        initListener()
    }

    private fun initView() {
        rvContent = findViewById(R.id.app_act_chat_content_rv)
        titleView = findViewById(R.id.app_act_chat_title)
        celBar = findViewById(R.id.ap_act_chat_cel)
        onFuncListener = FuncEventListener(this)
        celBar?.addOnFuncKeyBoardListener(this)
        celBar?.addFuncView(FuncGridView(this, onFuncListener))
        celBar?.etChat?.setOnSizeChangedListener { _, _, _, _ ->
            scrollToBottom()
        }
        celBar?.setAdapter(AdapterUtils.getAdapter(this, object : OnEmojiClickListener() {
            override fun getEt(): EditText? {
                return celBar?.etChat
            }

            override fun getSessionId(): String {
                return this@ChatActivity.sessionId
            }
        }))
    }

    private fun initData() {
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
            finish()
            return
        }
        register()
        DataTransferHub.queryMsgInDb("13", 8589934605)
    }

    private fun initListener() {
        celBar?.btnSend?.setOnClickListener {
            val text = celBar?.etChat?.text?.toString()
            if (!text.isNullOrEmpty()) {
                celBar?.etChat?.text = null
                IMHelper.sendTxt(sessionId, text)
            }
        }
        rvContent?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    celBar?.reset()
                }
            }
        })
        titleView?.setLeftClickListener { finish() }
    }

    private fun register() {
        IMHelper.registerSocketStateChangeListener(javaClass.simpleName) {
            titleView?.setTitle(it.name)
        }

        this@ChatActivity.addReceiveObserver<MsgInfo>(Constance.REG_CODE_CHAT_ACTIVITY_MESSAGE).listen { data ->
            if (!isFinishing) rvContent?.let {
                handler.removeMessages(1999)
                it.stopScroll()
                it.adapter.data().add(NORMAL, data)
                val p = it.adapter.data().maxCurDataPosition()
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

    private fun scrollToBottom() {
        rvContent?.requestLayout()
        rvContent?.post { rvContent?.scrollToPosition(rvContent?.adapter?.itemCount ?: 1 - 1) }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (EmoticonsKeyboardUtils.isFullScreen(this)) {
            if (celBar?.dispatchKeyEventInFullScreen(event) == true) {
                true
            } else {
                super.dispatchKeyEvent(event)
            }
        } else super.dispatchKeyEvent(event)

    }

    override fun onFuncPop(height: Int) {
        scrollToBottom()
    }

    override fun onFuncClose() {

    }

    fun resetEmotationBar() {
        celBar?.reset()
    }

    fun getSessionId(): String {
        return sessionId
    }

    override fun finish() {
        IMHelper.removeSocketStateChangeListener(javaClass.simpleName)
        super.finish()
    }

    override fun onPause() {
        super.onPause()
        celBar?.reset()
    }
}