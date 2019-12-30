package com.zj.imcore.ui.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.im.scheduler.ReceiveListener
import com.zj.imcore.R
import com.zj.imcore.registerTcpReceivedListener
import com.zj.imcore.ui.chat.ChatActivity
import com.zj.imcore.ui.main.conversation.ConversationAdapter.Companion.CONVERSATION_EVENT_ITEM
import com.zj.model.interfaces.DialogIn
import com.zj.model.mod.DialogInfo

class ConversationFragment : BaseLinkageFragment(), ((Int, DialogInfo, Int) -> Unit) {

    private var rvContent: RecyclerView? = null
    private var adapter: ConversationAdapter? = null
    private var dialogsReceiver: ReceiveListener<DialogIn, DialogInfo>? = null

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.app_fragment_conversation_content, container, false)
    }

    override fun onCreate() {
        super.onCreate()
        rvContent = find(R.id.app_fragment_conversation_rv_content)
        initData()
    }

    private fun initData() {
        adapter = ConversationAdapter(this::invoke)
        rvContent?.adapter = adapter
        dialogsReceiver = this.registerTcpReceivedListener(this.javaClass.simpleName)
        dialogsReceiver?.query()?.equalTo("",false)
    }

    override fun invoke(type: Int, data: DialogInfo, pos: Int) {
        when (type) {
            CONVERSATION_EVENT_ITEM -> {
                ChatActivity.start(this, data.channelId, data.userId, data.draft, data.title)
            }
        }
    }

    override fun onDestroyed() {

        super.onDestroyed()
    }
}