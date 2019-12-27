package com.zj.imcore.ui.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.imcore.R

class ConversationFragment : BaseLinkageFragment() {

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.app_fragment_conversation_content, container, false)
    }



}