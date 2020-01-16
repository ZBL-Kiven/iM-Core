package com.zj.imcore.ui.main.contact

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
import com.alibaba.fastjson.JSON
import com.cf.im.db.repositorys.MemberRepository
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.imcore.R
import com.zj.imcore.apis.members.MemberApi
import com.zj.imcore.base.FCApplication
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.imcore.model.member.contact.ContactMemberInfo
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
    private var rvContent: RecyclerView? = null
    private var loadingView: BaseLoadingView? = null
    private var adapter: ContactListAdapter? = null
    private var searchHandler: Handler? = null
    private var cachedData: ArrayList<ContactMemberInfo.MemberModel>? = null

    private fun initView() {
        etSearch = find(R.id.app_fragment_contact_et_search)
        vSearchClear = find(R.id.app_fragment_contact_v_search_clear)
        rvContent = find(R.id.app_fragment_contact_rv)
        loadingView = find(R.id.app_fragment_contact_loading)
    }

    private fun initListener() {
        loadingView?.setRefreshListener {
            loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
            getData()
        }
        adapter?.setOnChildClickListener { adapter, _, groupPosition, childPosition ->
            val member = adapter.getItem(groupPosition).children[childPosition]
            UserInfoActivity.start(activity, member.uid.toString())
        }
        vSearchClear?.setOnClickListener {
            etSearch?.setText("")
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
        searchHandler = Handler(Looper.getMainLooper()) {
            if (it.what == searchKey) doOnSearch(it.obj.toString())
            return@Handler false
        }
        this.context?.let {
            adapter = ContactListAdapter(it)
            rvContent?.adapter = adapter
            getData()
        } ?: loadingView?.setMode(
            BaseLoadingView.DisplayMode.NO_DATA,
            getString(R.string.app_system_error_no_context)
        )
    }

    private fun getData() {
        val since = SPUtils_Proxy.getMemberSyncSince(0)
        loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
        MemberApi.fetchMembers(since) { isSuccess, data, throwable ->
            if (isSuccess) {
                if (data == null || data.members.isNullOrEmpty()) {
                    loadingView?.setMode(BaseLoadingView.DisplayMode.NO_DATA)
                } else {
                    SPUtils_Proxy.setMemberSyncSince(data.nextTs)
                    data.members?.let { cachedData = ArrayList(it) }
                    setData(cachedData)
                    MemberRepository.insertOrUpdateAll(JSON.toJSONString(cachedData)) {
                        //数据集操作完成
                    }
                    loadingView?.setMode(BaseLoadingView.DisplayMode.NORMAL.delay(500))
                }
            } else {
                FCApplication.showToast(
                    throwable?.response()?.errorBody()?.string()
                        ?: getString(R.string.app_get_contact_failed)
                )
                loadingView?.setMode(BaseLoadingView.DisplayMode.NO_NETWORK)
            }
        }
    }

    private fun doOnSearch(ets: String?) {
        if (ets.isNullOrEmpty()) {
            setData(cachedData)
            return
        }
        cachedData?.let {
            val filterList = it.filterTo(arrayListOf()) { m ->
                m.name?.contains(ets, true) ?: false || m.profile?.title?.contains(
                    ets,
                    true
                ) ?: false || m.profile?.email?.contains(ets, true) ?: false
            }
            setData(filterList)
        }
    }

    private fun setData(data: List<ContactMemberInfo.MemberModel>?) {
        val map = mutableMapOf<String, MutableList<ContactMemberInfo.MemberModel>>()
        data?.groupByTo(map) {
            it.indexSymbol
        }
        val groupData = mutableListOf<ContactGroupInfo>()
        map.mapTo(groupData) {
            ContactGroupInfo(it.key, it.value)
        }
        adapter?.change(groupData)
    }

    override fun onDestroyed() {
        searchHandler?.removeCallbacksAndMessages(null)
        adapter?.clear()
        cachedData?.clear()
        super.onDestroyed()
    }
}
