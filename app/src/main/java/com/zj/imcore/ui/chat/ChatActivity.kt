package com.zj.imcore.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zj.base.view.BaseTitleView
import com.zj.ui.dispatcher.addReceiveObserver
import com.zj.ui.emotionboard.CusEmoticonsLayout
import com.zj.ui.emotionboard.utils.EmoticonsKeyboardUtils
import com.zj.ui.emotionboard.widget.FuncLayout
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.model.chat.MsgInfo
import com.zj.imcore.base.FCApplication
import com.zj.imcore.core.notification.NotificationManager
import com.zj.imcore.im.obtain.MessageObtainUtils
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.im.transfer.DataTransferHub
import com.zj.imcore.ui.chat.emoticon.AdapterUtils
import com.zj.imcore.ui.chat.emoticon.OnEmojiClickListener
import com.zj.imcore.ui.chat.func.FuncEventListener
import com.zj.imcore.ui.chat.func.FuncGridView
import com.zj.imcore.ui.chat.func.FuncsAdapter
import com.zj.imcore.ui.main.contact.group.GroupInfoActivity
import com.zj.imcore.ui.users.UserInfoActivity
import com.zj.imcore.ui.views.IMRecyclerView
import java.lang.Exception

class ChatActivity : FCActivity(), FuncLayout.FuncKeyBoardListener {

    companion object {
        private const val SESSION_ID = "session_id"
        private const val DIALOG_TYPE = "dialog_type"
        private const val DRAFT = "draft"
        private const val TITLE = "title"
        private const val TEAM_ID = "title"
        private const val NORMAL = "normal"

        fun start(ctx: Context?, dialogId: String, dialogType: String, tmId: String?, draft: String?, title: String?) {
            val i = Intent(ctx, ChatActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (dialogId.isEmpty()) {
                FCApplication.showToast("failed to jump to dialog with null id")
                return
            }
            i.putExtra(SESSION_ID, dialogId)
            if (dialogType != Constance.DIALOG_TYPE_P2P && dialogType != Constance.DIALOG_TYPE_GROUP) {
                FCApplication.showToast("failed to jump to dialog with unkown type")
                return
            }
            i.putExtra(DIALOG_TYPE, dialogType)
            if (dialogId.isEmpty()) {
                FCApplication.showToast("failed to jump to dialog with null id")
                return
            }
            if (!tmId.isNullOrEmpty()) i.putExtra(TEAM_ID, tmId)
            if (!draft.isNullOrEmpty()) i.putExtra(DRAFT, draft)
            if (!title.isNullOrEmpty()) i.putExtra(TITLE, title)
            ctx?.startActivity(i)
        }
    }

    private var dialogType = ""
    private var sessionId = ""
    private var tmId = ""
    private var draft = ""
    private var conversasionTitle = ""

    private var rvContent: IMRecyclerView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var titleView: BaseTitleView? = null
    private var celBar: CusEmoticonsLayout? = null
    private var onFuncListener: FuncsAdapter.OnItemClickListener? = null

    override fun getContentId(): Int {
        return R.layout.app_act_chat_content
    }

    override fun initBase() {
        initBaseIntent(intent)
    }

    private fun initBaseIntent(intent: Intent?) {
        try {
            intent?.let {
                if (it.hasExtra(SESSION_ID)) sessionId = it.getStringExtra(SESSION_ID) ?: ""
                if (it.hasExtra(DIALOG_TYPE)) dialogType = it.getStringExtra(DIALOG_TYPE) ?: Constance.DIALOG_TYPE_P2P
                if (it.hasExtra(TEAM_ID)) tmId = it.getStringExtra(TEAM_ID) ?: ""
                if (it.hasExtra(DRAFT)) draft = it.getStringExtra(DRAFT) ?: ""
                if (it.hasExtra(TITLE)) conversasionTitle = it.getStringExtra(TITLE) ?: ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (sessionId.isEmpty()) {
            FCApplication.showToast(getString(R.string.app_chat_error_empty_target))
            finish()
            return
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initBaseIntent(intent)
        rvContent?.adapter?.clear()
        rvContent?.adapter?.notifyDataSetChanged()
        initData()
    }

    override fun initView() {
        rvContent = findViewById(R.id.app_act_chat_content_rv)
        titleView = findViewById(R.id.app_act_chat_title)
        celBar = findViewById(R.id.ap_act_chat_cel)
        refreshLayout = findViewById(R.id.app_act_chat_refresh)
        onFuncListener = FuncEventListener(this)
        val rIcon = if (dialogType == Constance.DIALOG_TYPE_GROUP) {
            R.mipmap.app_act_chat_icon_group_ditail
        } else {
            R.mipmap.app_act_chat_icon_user_ditail
        }
        titleView?.setRightIcon(rIcon)
        titleView?.setRightClickListener {
            if (dialogType == Constance.DIALOG_TYPE_GROUP) {
                GroupInfoActivity.startActivity(this, sessionId)
            } else {
                UserInfoActivity.start(this, tmId, true)
            }
        }
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
        refreshLayout?.setOnRefreshListener {
            getMessage(false, 20, it)
        }
        refreshLayout?.setOnLoadMoreListener {
            getMessage(true, 20, it)
        }
    }

    override fun initData() {
        register()
        NotificationManager.singleton.get().removeNotification(sessionId)
        DataTransferHub.queryMsgInDb(sessionId)
    }

    override fun initListener() {
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

        this@ChatActivity.addReceiveObserver<MsgInfo>(Constance.REG_CODE_CHAT_ACTIVITY_MESSAGE).filterIn { it, _ -> it.dialogId == sessionId }.listen { data, lst, payload ->
            if (!isFinishing) rvContent?.let {
                it.adapter.data().let { adapter ->
                    if (data != null) {
                        adapter.add(NORMAL, data)
                    }
                    if (lst != null) {

                        adapter.addAll(NORMAL, lst)
                    }

                }

                if (it.canScrollVertically(-1)) return@listen
                it.stopScroll()
                //                handler.removeMessages(1999)
                //                val p = it.adapter.data().maxCurDataPosition()
                //                val msg = Message.obtain()
                //                msg.what = 1999
                //                msg.arg1 = p
                //                handler.sendMessageDelayed(msg, 30)
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
        val count = rvContent?.adapter?.itemCount ?: 1
        rvContent?.post { rvContent?.scrollToPosition(count - 1) }
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

    private fun getMessage(isNewer: Boolean, limit: Int, rl: RefreshLayout) {
        val msg = if (isNewer) rvContent?.adapter?.data?.lastOrNull() else rvContent?.adapter?.data?.firstOrNull()
        if (msg == null) {
            rl.finishRefresh()
        } else {
            MessageObtainUtils.fetchNewerMessage(msg, limit, isNewer) { _, _ ->
                if (isNewer) rl.finishLoadMore() else rl.finishRefresh()
            }
        }
    }

    override fun finish() {
        rvContent?.stopScroll()
        rvContent?.clear()
        rvContent = null
        IMHelper.removeSocketStateChangeListener(javaClass.simpleName)
        super.finish()
    }

    override fun onPause() {
        super.onPause()
        celBar?.reset()
    }
}