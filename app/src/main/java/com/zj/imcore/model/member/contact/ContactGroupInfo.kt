package com.zj.imcore.model.member.contact

import com.zj.list.groupedadapter.proctol.GroupedData
import com.zj.model.chat.DialogInfo

data class ContactGroupInfo(val indexSymbol: String, val children: List<DialogInfo>) : GroupedData<DialogInfo>{

    override fun getChild(): List<DialogInfo> {
        return children
    }
}