package com.zj.imcore.ui.main.contact.group

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.zj.base.view.BaseTitleView
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.imcore.base.FCApplication
import com.zj.imcore.model.member.EventMod
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.imcore.ui.main.contact.DialogsProvider
import com.zj.imcore.ui.main.contact.DialogsVisitor
import com.zj.imcore.ui.main.contact.group.adapter.CreateGroupListAdapter
import com.zj.loading.BaseLoadingView
import com.zj.model.chat.DialogInfo
import com.zj.ui.dispatcher.addReceiveObserver
import java.util.ArrayList

class CreateGroupActivity : FCActivity() {

    companion object {

        private const val SELECTED = "selected_list"
        private const val MAX_NUM = "max_select_size"
        fun start(context: Activity, req: Int, maxNum: Int, selectedIds: ArrayList<String>? = null) {
            val i = Intent(context, CreateGroupActivity::class.java)
            if (!selectedIds.isNullOrEmpty()) i.putExtra(SELECTED, selectedIds)
            if (maxNum > 0) i.putExtra(MAX_NUM, maxNum)
            context.startActivityForResult(i, req)
        }
    }

    override fun getContentId(): Int {
        return R.layout.app_act_contact_create_group
    }

    private val searchKey = 0xaac1
    private var etSearch: EditText? = null
    private var vSearchClear: View? = null
    private var rvContent: RecyclerView? = null
    private var loadingView: BaseLoadingView? = null
    private var adapter: CreateGroupListAdapter? = null
    private var titleBar: BaseTitleView? = null

    private var searchHandler: Handler? = null
    private var cachedData: ArrayList<DialogInfo>? = null
    private var maxSelectCount: Int = -1

    override fun initBase() {
        val selectedIds = if (intent.hasExtra(SELECTED)) {
            ArrayList(intent.getStringArrayListExtra(SELECTED)?.toMutableList() ?: ArrayList())
        } else arrayListOf()
        adapter = CreateGroupListAdapter(this, selectedIds)
        if (intent.hasExtra(MAX_NUM)) {
            maxSelectCount = intent.getIntExtra(MAX_NUM, -1)
        }
    }

    override fun initView() {
        etSearch = find(R.id.app_act_contact_create_group_et_search)
        vSearchClear = find(R.id.app_act_contact_create_group_v_search_clear)
        rvContent = find(R.id.app_act_contact_create_group_rv)
        titleBar = find(R.id.app_act_contact_create_group_title)
        loadingView = find(R.id.app_act_contact_create_group_blv)
    }

    override fun initListener() {
        loadingView?.setRefreshListener {
            loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
            getData()
        }
        vSearchClear?.setOnClickListener {
            if (!etSearch?.text.isNullOrEmpty()) etSearch?.setText("")
        }
        titleBar?.setLeftClickListener {
            finish()
        }
        titleBar?.setRightClickListener {
            createAGroup()
        }
        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchHandler?.removeMessages(searchKey)
                searchHandler?.sendMessageDelayed(Message.obtain().apply {
                    what = searchKey
                    obj = s?.trim()?.toString()
                }, 300)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun initData() {
        loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
        searchHandler = Handler(Looper.getMainLooper()) {
            if (it.what == searchKey) doOnSearch(it.obj.toString())
            return@Handler false
        }
        rvContent?.adapter = adapter
        addReceiveObserver<EventMod>(Constance.REG_CODE_FRAGMENT_CONTACT).filterIn { it, _ -> it.code == Constance.REG_RESULT_CONTANCT }.listen { _, _, _ ->
            getData()
        }
        getData()
    }

    private fun doOnSearch(ets: String?) {
        if (ets.isNullOrEmpty()) {
            setData(cachedData)
            return
        }
        cachedData?.let {
            val filterList = it.filterTo(arrayListOf()) { m ->
                m.name.contains(ets, true) || m.title.contains(ets, true) || m.email.contains(ets, true)
            }
            setData(filterList)
        }
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

    private fun getData() {
        loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
        DialogsProvider.getDialogsFromLocalOrServer( object : DialogsVisitor {
            override fun onGot(m: List<DialogInfo>?) {
                if (m.isNullOrEmpty()) {
                    loadingView?.setMode(BaseLoadingView.DisplayMode.NO_DATA, getString(R.string.app_common_no_data), false)
                } else {
                    cachedData = ArrayList(m)
                    setData(cachedData)
                    loadingView?.setMode(BaseLoadingView.DisplayMode.NORMAL.delay(500))
                }
            }
        })
    }

    private fun createAGroup() {
        val ids = adapter?.selectedIds
        if (ids.isNullOrEmpty()) {
            FCApplication.showToast(R.string.app_act_create_groups_hint_not_create)
        } else {
            loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING, getString(R.string.app_act_create_groups_hint), true)
            //todo create group
        }
    }

    override fun onDestroy() {
        searchHandler?.removeCallbacksAndMessages(null)
        adapter?.clear()
        cachedData?.clear()
        super.onDestroy()
    }
}