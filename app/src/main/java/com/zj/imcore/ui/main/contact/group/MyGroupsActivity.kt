package com.zj.imcore.ui.main.contact.group

import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.zj.base.view.BaseTitleView
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.imcore.ui.main.contact.ContactListAdapter
import com.zj.imcore.ui.main.contact.DialogsProvider
import com.zj.imcore.ui.main.contact.DialogsVisitor
import com.zj.imcore.ui.users.UserInfoActivity
import com.zj.loading.BaseLoadingView
import com.zj.model.chat.DialogInfo
import java.util.ArrayList

class MyGroupsActivity : FCActivity() {

    private val what: Int = 0x1;

    private var titleView: BaseTitleView? = null
    private var loadingView: BaseLoadingView? = null
    private var rvDialog: RecyclerView? = null
    private var etSearch: EditText? = null

    private var adapter: ContactListAdapter? = null
    private var cachedData: ArrayList<DialogInfo>? = null

    private val mHandler = Handler {
        if (!isResume) {
            return@Handler true
        }

        val content = etSearch?.text.toString() ?: ""

        if (content == "") {
            setData(cachedData)
            return@Handler true
        }

        cachedData?.let {
            val filterList = it.filterTo(arrayListOf()) { m ->
                m.name.contains(content, true) || (m.title ?: "").contains(
                    content,
                    true
                ) || (m.email ?: "").contains(
                    content, true
                )
            }
            setData(filterList)
        }

        true
    };

    override fun getContentId(): Int {
        return R.layout.app_act_contact_group
    }

    override fun initView() {
        loadingView = find(R.id.app_act_contact_groups_blv)
        titleView = find(R.id.app_act_contact_groups_title)
        rvDialog = find(R.id.app_act_contact_groups_rv)
        etSearch = find(R.id.app_act_contact_groups_et_search)
    }

    override fun initData() {
        adapter = ContactListAdapter(this)
        rvDialog?.adapter = adapter;

        adapter?.setOnChildClickListener { adapter, _, groupPosition, childPosition ->
            val dialog = adapter.getItem(groupPosition).children[childPosition]
            if ("group" == dialog.type) {
                GroupInfoActivity.startActivity(this, dialog.dialogId)
            } else {
                UserInfoActivity.start(this, dialog.tmid)
            }
        }

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

        titleView?.setLeftClickListener {
            super.onBackPressed()
        }

        findViewById<View>(R.id.app_act_contact_groups_v_search_clear)?.let {
            mHandler.sendEmptyMessage(
                what
            )
        }

        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mHandler.removeMessages(what)
                mHandler.sendEmptyMessageDelayed(what, 2000)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }
}