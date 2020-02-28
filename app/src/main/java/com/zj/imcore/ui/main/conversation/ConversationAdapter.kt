package com.zj.imcore.ui.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cf.im.db.repositorys.MessageRepository
import com.zj.base.utils.DPUtils
import com.zj.im.chat.enums.SendMsgState
import com.zj.ui.list.utils.TimeLineInflateModel
import com.zj.model.chat.DialogInfo
import com.zj.imcore.R
import com.zj.imcore.ui.list.ChatOption
import com.zj.imcore.utils.img.transactions.RoundCorner
import com.zj.list.multiable.MultiRecyclerAdapter
import com.zj.model.Payloads
import com.zj.ui.mainHandler
import java.lang.IllegalArgumentException

class ConversationAdapter(val listener: (type: Int, data: DialogInfo, pos: Int, v: View) -> Unit) : MultiRecyclerAdapter<DialogInfo>() {

    companion object {
        const val CONVERSATION_EVENT_ITEM = 0
        const val CONVERSATION_EVENT_UNREAD = 1
        const val CONVERSATION_EVENT_PIN = 2
        const val CONVERSATION_EVENT_MUTE = 3
        const val CONVERSATION_EVENT_ARCHIVE = 4
    }

    override fun initData(itemView: View, data: DialogInfo?, position: Int, payloads: MutableList<Any>?) {
        if (data == null) return
        val content = itemView.findViewById<View>(R.id.app_fragment_conversation_item_content)
        val avatar = itemView.findViewById<ImageView>(R.id.app_fragment_conversation_item_iv_avatar)
        val count = itemView.findViewById<TextView>(R.id.app_fragment_conversation_item_tv_count)
        val name = itemView.findViewById<TextView>(R.id.app_fragment_conversation_item_tv_name)
        val subDetail = itemView.findViewById<TextView>(R.id.app_fragment_conversation_item_tv_sub_detail)
        val time = itemView.findViewById<TextView>(R.id.app_fragment_conversation_item_tv_time)
        val muteDisplay = itemView.findViewById<View>(R.id.app_fragment_conversation_item_v_mute_display)
        val unread = itemView.findViewById<View>(R.id.app_fragment_conversation_item_v_unread)
        val pin = itemView.findViewById<View>(R.id.app_fragment_conversation_item_v_pin)
        val mute = itemView.findViewById<View>(R.id.app_fragment_conversation_item_v_mute)
        val archive = itemView.findViewById<View>(R.id.app_fragment_conversation_item_v_archive)

        fun initAvatar() {
            avatar?.let {
                val url = data.avatar
                val radius = DPUtils.dp2px(ChatOption.avatarRadius) * 1.0f
                val transformer = RoundCorner(it.context, radius, radius, radius, radius)
                Glide.with(it).load(url).override(it.width, it.height).transform(transformer).error(R.mipmap.app_contact_avatar_default).into(it)
            }
        }

        fun initUnReadCount() {
            val unReadCount = data.unReadCount.toString()
            count?.let {
                if (unReadCount.isEmpty()) it.visibility = View.GONE
                else {
                    it.visibility = View.VISIBLE
                    it.text = unReadCount
                }
            }
        }

        fun initName() {
            name?.text = data.name
        }

        fun initSubDetail() {
            MessageRepository.queryLast(data.dialogId) {
                mainHandler.post {
                    subDetail?.text = if (data.draft != null) "[草稿] ${data.draft}" else {
                        if (it.text.isNullOrEmpty()) "" else {
                            "${if (it?.sendMsgState == SendMsgState.SENDING.type) "← " else ""}${it.text}"
                        }
                    }
                    time?.context?.let { ctx ->
                        var ts = it?.localCreateTs ?: 0
                        if (ts <= 0) ts = data.updated
                        if (ts <= 0) ts = data.created
                        time.text = TimeLineInflateModel.getTimeString(ctx, ts)
                    }
                }
            }
        }

        fun initPin() {
            val isPin = data.pin
            itemView.isSelected = isPin
            pin?.isSelected = isPin
        }

        fun initMute() {
            val isMute = data.mute
            mute?.isSelected = isMute
            muteDisplay?.visibility = if (isMute) View.VISIBLE else View.GONE
        }

        fun initDeleted() {
            if (data.hidden) throw IllegalArgumentException("the module for dialog was deleted? why inflate with there? ")
        }

        when (payloads?.firstOrNull()) {
            null -> {
                initAvatar();initName();initSubDetail();initUnReadCount();initMute();initPin();initDeleted()
            }
            Payloads.CONVERSATION_AVATAR -> {
                initAvatar()
            }
            Payloads.CONVERSATION_NEW_MSG -> {
                initSubDetail();initUnReadCount()
            }
            Payloads.CONVERSATION_TITLE -> {
                initName()
            }
            Payloads.CONVERSATION_STATUS_PIN -> {
                initPin()
            }
            Payloads.CONVERSATION_STATUS_MUTE -> {
                initMute()
            }
        }

        content.setOnClickListener {
            listener(CONVERSATION_EVENT_ITEM, data, position, it)
        }

        unread.setOnClickListener {
            listener(CONVERSATION_EVENT_UNREAD, data, position, it)
        }
        pin.setOnClickListener {
            listener(CONVERSATION_EVENT_PIN, data, position, it)
        }
        mute.setOnClickListener {
            listener(CONVERSATION_EVENT_MUTE, data, position, it)
        }
        archive.setOnClickListener {
            listener(CONVERSATION_EVENT_ARCHIVE, data, position, it)
        }
    }

    override fun onBuildData(data: MutableList<DialogInfo>?): MutableList<DialogInfo> {
        return data ?: arrayListOf()
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.app_fragment_conversation_item, parent, false)
    }
}