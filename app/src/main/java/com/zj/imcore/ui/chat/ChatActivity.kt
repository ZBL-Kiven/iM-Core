package com.zj.imcore.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zj.base.view.BaseTitleView
import com.zj.im.dispatcher.addReceiveObserver
import com.zj.im.emotionboard.CusEmoticonsLayout
import com.zj.im.emotionboard.adpater.EmoticonPacksAdapter
import com.zj.im.emotionboard.utils.EmoticonsKeyboardUtils
import com.zj.im.emotionboard.widget.FuncLayout
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCApplication
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.im.transfer.DataTransferHub
import com.zj.imcore.ui.views.IMRecyclerView
import java.lang.Exception

class ChatActivity : AppCompatActivity(), FuncLayout.FuncKeyBoardListener {

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
    private var titleView: BaseTitleView? = null
    private var celBar: CusEmoticonsLayout? = null
    private var adapter: EmoticonPacksAdapter? = null

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
        titleView?.setLeftClickListener { finish() }
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
        DataTransferHub.queryMsgInDb("", "")
    }

    private fun initListener() {
        //        etInput?.setOnEditorActionListener { v, actionId, _ ->
        //            if (actionId == EditorInfo.IME_ACTION_SEND) {
        //                val text = v.text.toString()
        //                if (text.isEmpty()) return@setOnEditorActionListener false
        //                v.text = ""
        //                IMHelper.sendTxt(sessionId, text)
        //                return@setOnEditorActionListener true
        //            }
        //            return@setOnEditorActionListener false
        //        }

        rvContent?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    celBar?.reset()
                }
            }
        })
    }

    private fun register() {
        IMHelper.registerSocketStateChangeListener(javaClass.simpleName) {
            titleView?.setTitle(it.name)
        }
        this@ChatActivity.addReceiveObserver<MsgInfo>(Constance.REG_CODE_CHAT_ACTIVITY_MESSAGE).listen { data ->
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

    protected fun scrollToBottom() {
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

    override fun finish() {
        IMHelper.removeSocketStateChangeListener(javaClass.simpleName)
        super.finish()
    }

    override fun onPause() {
        super.onPause()
        celBar?.reset()
    }
}