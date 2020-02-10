package com.zj.imcore.ui.main.contact.group

import android.content.Context
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cf.im.db.domain.MemberBean
import com.zj.imcore.R
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.list.groupedadapter.adapter.GroupedListAdapter
import com.zj.list.holders.BaseViewHolder
import java.util.ArrayList


class MyGroupListAdapter(val context: Context) : GroupedListAdapter<MemberBean, ContactGroupInfo>(context) {

    init {
        setOnChildClickListener { adapter, _, groupPosition, childPosition ->
            val memberId = adapter.getItem(groupPosition).children[childPosition].uid

            adapter.notifyChildChanged(groupPosition, childPosition)
        }
    }

    override fun hasFooter(groupPosition: Int): Boolean {
        return false
    }

    override fun getHeaderResId(viewType: Int): Int {
        return R.layout.app_fragment_contact_item_header
    }

    override fun getFooterResId(viewType: Int): Int {
        return 0
    }

    override fun getChildResId(viewType: Int): Int {
        return R.layout.app_act_contact_group_item
    }

    override fun bindHeader(holder: BaseViewHolder?, t: ContactGroupInfo?, pos: Int) {
        (holder?.itemView as? TextView)?.text = t?.indexSymbol ?: "#"
    }

    override fun bindChild(holder: BaseViewHolder?, r: MemberBean, pos: Int) {
        val w = context.resources.getDimension(R.dimen.app_contact_avatar_width).toInt()
        val h = context.resources.getDimension(R.dimen.app_contact_avatar_height).toInt()
        holder?.getView<ImageView>(R.id.app_act_contact_select_item_iv_avatar)?.let { iv ->
            Glide.with(context).load(r.avatar).override(w, h).placeholder(R.mipmap.app_contact_avatar_default).into(iv)
        }
        holder?.setText(R.id.app_act_contact_select_item_tv_name, r.name ?: "null")
        holder?.setText(R.id.app_act_contact_select_item_tv_title, r.title ?: "null")
    }

    override fun bindFooter(holder: BaseViewHolder?, t: ContactGroupInfo?, pos: Int) {

    }
}
