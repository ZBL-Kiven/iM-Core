package com.zj.imcore.im.transfer

import com.zj.model.chat.DialogInfo
import com.zj.model.interfaces.DialogIn

object DialogTransfer {

    fun transform(beans: List<DialogIn>): MutableList<DialogInfo> {
        val list = mutableListOf<DialogInfo>()
        beans.forEach { list.add(transform(it)) }
        return list
    }

    fun transform(bean: DialogIn): DialogInfo {
        return DialogInfo(bean)
    }

}