package com.zj.model.chat

import com.zj.list.multiable.MultiAbleData
import com.zj.model.interfaces.DialogIn

class DialogInfo(private val impl: DialogIn) : MultiAbleData<DialogInfo> {

    //unique id for conversation
    val channelId: Long; get() = impl.getId()

    val title: String; get() = impl.getTitle()

    val subDetail: String; get() = impl.getSubDetail()

    //the conversation last updated Ts
    val latestTs: Long; get() = impl.getLatestTs()

    val selfReadTs: Long; get() = impl.getSelfReadTs()

    val unReadCount: Int; get() = impl.getUnReadCount()

    //last time someone read the conversation
    val otherReadTs: Long; get() = impl.getOtherReadTs()

    //user id of the peer during p2p conversation
    val userId: Long; get() = impl.getUserId()

    //are there favorite messages in the conversation
    val hasStar: Boolean; get() = impl.hasStar()

    val draft: String?; get() = impl.getDraft()

    val isShown: Boolean; get() = impl.isShown()

    val sortTs: Long; get() = impl.sortTs()

    val notification: Boolean; get() = impl.notification()

    val hideTs: Long; get() = impl.hideTs()

    val thumbUrl: String; get() = impl.getThumbUrl()

    val isPin: Boolean; get() = impl.isPin()

    val isMute: Boolean; get() = impl.isMute()

    val isDelete: Boolean; get() = impl.isDelete()

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
        return if (other !is DialogInfo) false else channelId == other.channelId
    }

    override fun hashCode(): Int {
        return channelId.hashCode()
    }
}