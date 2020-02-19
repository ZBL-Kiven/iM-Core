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
import com.cf.im.db.domain.MemberBean
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.ui.dispatcher.addReceiveObserver
import com.zj.ui.log
import com.zj.imcore.Constance
import com.zj.imcore.R
import com.zj.imcore.model.member.MembersEventMod
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.imcore.ui.main.contact.group.MyGroupsActivity
import com.zj.imcore.ui.users.UserInfoActivity
import com.zj.loading.BaseLoadingView
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
    private var cachedData: ArrayList<MemberBean>? = null

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
            getData(false)
        }
        adapter?.setOnChildClickListener { adapter, _, groupPosition, childPosition ->
            val member = adapter.getItem(groupPosition).children[childPosition]
            UserInfoActivity.start(activity, member.uid)
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
        } ?: loadingView?.setMode(BaseLoadingView.DisplayMode.NO_DATA, getString(R.string.app_system_error_no_context))
        addReceiveObserver<MembersEventMod>(Constance.REG_CODE_FRAGMENT_CONTACT).listen { it, s, c ->
            log(it?.case?:"")
            getData(false)
        }
    }

    private fun doOnSearch(ets: String?) {
        if (ets.isNullOrEmpty()) {
            setData(cachedData)
            return
        }
        cachedData?.let {
            val filterList = it.filterTo(arrayListOf()) { m ->
                m.name?.contains(ets, true) ?: false || m.title?.contains(ets, true) ?: false || m.email?.contains(ets, true) ?: false
            }
            setData(filterList)
        }
    }

    private fun setData(data: List<MemberBean>?) {
        val map = mutableMapOf<String, MutableList<MemberBean>>()
        data?.groupByTo(map) {
            it.indexSymbol ?: "#"
        }
        val groupData = mutableListOf<ContactGroupInfo>()
        map.mapTo(groupData) {
            ContactGroupInfo(it.key, it.value)
        }
        adapter?.change(groupData)
    }

    private fun getData(isFirst: Boolean) {
        loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
        MembersProvider.getMembersFromLocalOrServer(isFirst, object : MembersVisitor {
            override fun onGot(m: List<MemberBean>?) {
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

    override fun onResumed() {
        super.onResumed()
        if (cachedData.isNullOrEmpty()) getData(true)
    }

    override fun onDestroyed() {
        searchHandler?.removeCallbacksAndMessages(null)
        adapter?.clear()
        cachedData?.clear()
        super.onDestroyed()
    }
}
