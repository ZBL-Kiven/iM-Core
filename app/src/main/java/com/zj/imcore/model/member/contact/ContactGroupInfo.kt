package com.zj.imcore.model.member.contact

import com.zj.list.groupedadapter.proctol.GroupedData

data class ContactGroupInfo(val indexSymbol: String, val children: List<ContactMemberInfo.MemberModel>) : GroupedData<ContactMemberInfo.MemberModel>{

    override fun getChild(): List<ContactMemberInfo.MemberModel> {
        return children
    }

}