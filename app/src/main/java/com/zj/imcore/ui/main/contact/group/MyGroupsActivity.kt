package com.zj.imcore.ui.main.contact.group

import androidx.recyclerview.widget.RecyclerView
import com.zj.base.view.BaseTitleView
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.imcore.ui.main.contact.ContactListAdapter
import com.zj.imcore.ui.main.contact.DialogsProvider
import com.zj.imcore.ui.main.contact.DialogsVisitor
import com.zj.loading.BaseLoadingView
import com.zj.model.chat.DialogInfo
import java.util.ArrayList

class MyGroupsActivity : FCActivity() {

    private var titleView: BaseTitleView? = null
    private var loadingView: BaseLoadingView? = null
    private var rvDialog: RecyclerView? = null

    private var adapter: ContactListAdapter? = null

    private var cachedData: ArrayList<DialogInfo>? = null

    override fun getContentId(): Int {
        return R.layout.app_act_contact_group
    }

    override fun initView() {
        loadingView = find(R.id.app_act_contact_groups_blv)
        titleView = find(R.id.app_act_contact_groups_title)
        rvDialog = find(R.id.app_act_contact_groups_rv)
    }

    override fun initData() {
        adapter = ContactListAdapter(this)
        rvDialog?.adapter = adapter;

        DialogsProvider.getDialogByGroupFromLocalOrServer(object : DialogsVisitor {
            override fun onGot(m: List<DialogInfo>?) {
                if (m.isNullOrEmpty()) {
                    loadingView?.setMode(
                        BaseLoadingView.DisplayMode.NO_DATA,
                        getString(R.string.app_common_no_data),
                        false
                    )
                } else {
                    cachedData = ArrayList(m)
                    setData(cachedData)
                    loadingView?.setMode(BaseLoadingView.DisplayMode.NORMAL.delay(500))
                }
            }
        })
    }

    private fun setData(data: List<DialogInfo>?) {
        val map = mutableMapOf<String, MutableList<DialogInfo>>()
        data?.groupByTo(map) {
            it.indexSymbol ?: "#"
        }
        val groupData = mutableListOf<ContactGroupInfo>()
        map.mapTo(groupData) {
            ContactGroupInfo(it.key, it.value)
        }
        adapter?.change(groupData)
    }

    override fun initListener() {

    }
}