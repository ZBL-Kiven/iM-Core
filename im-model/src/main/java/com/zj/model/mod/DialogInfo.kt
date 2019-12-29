package com.zj.model.mod

import com.zj.list.multiable.MultiAbleData
import com.zj.model.interfaces.DialogIn

abstract class DialogInfo(val impl: DialogIn) : MultiAbleData<DialogInfo> {


    override fun compareTo(other: DialogInfo): Int {
        val isPin = impl.isPin()
        val otherIsPin = other.impl.isPin()
        if (isPin && !otherIsPin) {
            return 1
        } else if (!isPin && otherIsPin) {
            return -1
        }
        other.impl.sortTs().let { ot ->
            impl.sortTs().let {
                if (it > ot) return 1
                return if (it < ot) -1 else 0
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is MsgInfo) false else impl.getId() == other.impl.key()
    }

    override fun hashCode(): Int {
        return impl.getId().hashCode()
    }
}