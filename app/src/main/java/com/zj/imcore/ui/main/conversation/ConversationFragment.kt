package com.zj.imcore.ui.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.imcore.R
import com.zj.imcore.ui.chat.ChatActivity
import com.zj.imcore.ui.main.conversation.ConversationAdapter.Companion.CONVERSATION_EVENT_ITEM
import com.zj.model.mod.DialogInfo

class ConversationFragment : BaseLinkageFragment(), ((Int, DialogInfo, Int) -> Unit) {

    private var rvContent: RecyclerView? = null
    private var adapter: ConversationAdapter? = null

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
    }

    override fun invoke(type: Int, data: DialogInfo, pos: Int) {
        when (type) {
            CONVERSATION_EVENT_ITEM -> {
                val impl = data.impl
                ChatActivity.start(this, impl.getId(), impl.getUserId(), impl.getDraft(), impl.getTitle())
            }
        }
    }
}