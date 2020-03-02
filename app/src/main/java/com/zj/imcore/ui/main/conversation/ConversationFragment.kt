package com.zj.imcore.ui.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.ui.dispatcher.addReceiveObserver
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.gui.login.TeamManager
import com.zj.imcore.im.transfer.DataTransferHub
import com.zj.imcore.ui.chat.ChatActivity
import com.zj.imcore.ui.main.conversation.ConversationAdapter.Companion.CONVERSATION_EVENT_ITEM
import com.zj.loading.BaseLoadingView
import com.zj.model.chat.DialogInfo
import com.zj.ui.mainHandler

class ConversationFragment : BaseLinkageFragment(), ((Int, DialogInfo, Int, View) -> Unit) {

    private var rvContent: RecyclerView? = null
    private var adapter: ConversationAdapter? = null
    private var blv: BaseLoadingView? = null
    private val normal = "normal"

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.app_fragment_conversation_content, container, false)
    }

    override fun onCreate() {
        super.onCreate()
        initView()
        initData()
    }

    private fun initView() {
        rvContent = find(R.id.app_fragment_conversation_rv_content)
        blv = find(R.id.app_fragment_conversation_blv)
        blv?.setRefreshListener {
            getData()
        }
    }

    private fun initData() {
        adapter = ConversationAdapter(this::invoke)
        rvContent?.adapter = adapter
        this.addReceiveObserver<DialogInfo>(Constance.REG_CODE_COVERSATION_FRAGMENT_DIALOG).listen { d, lst, payload ->
            blv?.setMode(BaseLoadingView.DisplayMode.NORMAL)
            if (activity?.isFinishing == true) return@listen
            if (d != null) {
                adapter?.data()?.set(d, normal, payload)
            }
            if (!lst.isNullOrEmpty()) {
                adapter?.data()?.addAll(normal, lst)
            }
        }
    }

    private fun getData() {
        blv?.setMode(BaseLoadingView.DisplayMode.LOADING)
        DataTransferHub.queryDialogInDb(TeamManager.getCurrentTeamId()) {
            mainHandler.post {
                blv?.setMode(BaseLoadingView.DisplayMode.NO_DATA.delay(500))
            }
        }
    }

    override fun invoke(type: Int, data: DialogInfo, pos: Int, v: View) {
        when (type) {
            CONVERSATION_EVENT_ITEM -> {
                ChatActivity.start(activity, data.dialogId, data.type, data.tmid, data.draft, data.title)
            }
        }
    }

    override fun onResumed() {
        super.onResumed()
        getData()
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroyed() {
        adapter?.clear()
        super.onDestroyed()
    }
}