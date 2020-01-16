package com.zj.imcore.model.member.contact

import com.cf.im.db.domain.MemberBean
import com.zj.list.groupedadapter.proctol.GroupedData

data class ContactGroupInfo(val indexSymbol: String, val children: List<MemberBean>) : GroupedData<MemberBean>{

    override fun getChild(): List<MemberBean> {
        return children
    }

}