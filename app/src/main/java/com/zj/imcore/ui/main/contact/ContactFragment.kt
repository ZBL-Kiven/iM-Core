package com.zj.imcore.ui.main.contact

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.ui.dispatcher.addReceiveObserver
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.model.GroupInfo
import com.zj.imcore.model.member.EventMod
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.imcore.ui.main.contact.group.GroupInfoActivity
import com.zj.imcore.ui.main.contact.group.MyGroupsActivity
import com.zj.imcore.ui.users.UserInfoActivity
import com.zj.loading.BaseLoadingView
import com.zj.model.chat.DialogInfo
import java.util.ArrayList

class ContactFragment : BaseLinkageFragment() {

    override fun getView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.app_fragment_contact_content, container, false)
    }

    override fun onCreate() {
        super.onCreate()
        initView()
        initData()
        initListener()
    }

    private val searchKey = 0xaac
    private var etSearch: EditText? = null
    private var vSearchClear: View? = null
    private var vMyGroups: View? = null
    private var rvContent: RecyclerView? = null
    private var loadingView: BaseLoadingView? = null
    private var adapter: ContactListAdapter? = null

    private var searchHandler: Handler? = null
    private var cachedData: ArrayList<DialogInfo>? = null

    private fun initView() {
        etSearch = find(R.id.app_fragment_contact_et_search)
        vSearchClear = find(R.id.app_fragment_contact_v_search_clear)
        rvContent = find(R.id.app_fragment_contact_rv)
        vMyGroups = find(R.id.app_fragment_contact_tv_my_groups)
        loadingView = find(R.id.app_fragment_contact_loading)
    }

    private fun initListener() {
        loadingView?.setRefreshListener {
            loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
            getData()
        }
        adapter?.setOnChildClickListener { adapter, _, groupPosition, childPosition ->
            val dialog = adapter.getItem(groupPosition).children[childPosition]
            if ("group" == dialog.type) {
                GroupInfoActivity.startActivity(context, dialog.dialogId)
            } else {
                UserInfoActivity.start(activity, dialog.dialogId)
            }
        }
        vSearchClear?.setOnClickListener {
            if (!etSearch?.text.isNullOrEmpty()) etSearch?.setText("")
        }
        vMyGroups?.setOnClickListener {
            startActivity(Intent(this.activity, MyGroupsActivity::class.java))
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

    private fun initData() {
        loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
        searchHandler = Handler(Looper.getMainLooper()) {
            if (it.what == searchKey) doOnSearch(it.obj.toString())
            return@Handler false
        }
        this.context?.let {
            adapter = ContactListAdapter(it)
            rvContent?.adapter = adapter
        } ?: loadingView?.setMode(
            BaseLoadingView.DisplayMode.NO_DATA,
            getString(R.string.app_system_error_no_context)
        )
        addReceiveObserver<EventMod>(Constance.REG_CODE_FRAGMENT_CONTACT).filterIn { t, _ ->
            t.code == Constance.REG_RESULT_CONTANCT
        }.listen { _, _, _ ->
            getData()
        }
    }

    private fun doOnSearch(ets: String?) {
        if (ets.isNullOrEmpty()) {
            setData(cachedData)
            return
        }
        cachedData?.let {
            val filterList = it.filterTo(arrayListOf()) { m ->
                m.name.contains(ets, true) || m.title.contains(ets, true) || m.email.contains(
                    ets,
                    true
                )
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
        DialogsProvider.getDialogsFromLocalOrServer(object : DialogsVisitor {
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

    override fun onResumed() {
        super.onResumed()
        if (cachedData.isNullOrEmpty()) getData()
    }

    override fun onDestroyed() {
        searchHandler?.removeCallbacksAndMessages(null)
        adapter?.clear()
        cachedData?.clear()
        super.onDestroyed()
    }
}
