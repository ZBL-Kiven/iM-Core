package com.zj.imcore.ui.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.im.dispatcher.addReceiveObserver
import com.zj.im.log
import com.zj.imcore.R
import com.zj.imcore.im.transfer.DataTransferHub
import com.zj.imcore.ui.chat.ChatActivity
import com.zj.imcore.ui.main.conversation.ConversationAdapter.Companion.CONVERSATION_EVENT_ITEM
import com.zj.model.chat.DialogInfo

class ConversationFragment : BaseLinkageFragment(), ((Int, DialogInfo, Int, View) -> Unit) {

    private var rvContent: RecyclerView? = null
    private var adapter: ConversationAdapter? = null

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.app_fragment_conversation_content, container, false)
    }

    override fun onCreate() {
        super.onCreate()
        rvContent = find(R.id.app_fragment_conversation_rv_content)
        initData()
        getData()
    }

    private fun initData() {
        adapter = ConversationAdapter(this::invoke)
        rvContent?.adapter = adapter
        this.addReceiveObserver<DialogInfo>(11121).listen {
            adapter?.data()?.add("aa", it)
        }
    }

    private fun getData() {
        DataTransferHub.queryDialogInDb()
    }

    override fun invoke(type: Int, data: DialogInfo, pos: Int, v: View) {
        when (type) {
            CONVERSATION_EVENT_ITEM -> {
                ChatActivity.start(activity, data.channelId, data.userId, data.draft, data.title)
            }
        }
    }

    override fun onDestroyed() {
        adapter?.clear()
        super.onDestroyed()
    }
}