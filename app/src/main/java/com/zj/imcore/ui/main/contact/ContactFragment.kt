package com.zj.imcore.ui.main.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.cf.fragments.BaseLinkageFragment
import com.zj.imcore.R
import com.zj.imcore.apis.members.MemberApi
import com.zj.imcore.base.FCApplication
import com.zj.imcore.model.member.contact.ContactGroupInfo
import com.zj.imcore.model.member.contact.ContactMemberInfo
import com.zj.list.divider.RecyclerViewDivider
import com.zj.loading.BaseLoadingView

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

    private var etSearch: EditText? = null
    private var vSearch: View? = null
    private var rvContent: RecyclerView? = null
    private var loadingView: BaseLoadingView? = null
    private var adapter: ContactListAdapter? = null

    private fun initView() {
        etSearch = find(R.id.app_fragment_contact_et_search)
        vSearch = find(R.id.app_fragment_contact_v_search)
        rvContent = find(R.id.app_fragment_contact_rv)
        loadingView = find(R.id.app_fragment_contact_loading)
    }


    private fun initListener() {
        loadingView?.setRefreshListener {
            loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
            getData()
        }
    }

    private fun initData() {
        this.context?.let {
            adapter = ContactListAdapter(it)
            rvContent?.adapter = adapter
            getData()
        } ?: loadingView?.setMode(BaseLoadingView.DisplayMode.NO_DATA, getString(R.string.app_system_error_no_context))
    }

    private fun getData() {
        val since = SPUtils_Proxy.getMemberSyncSince(0)
        loadingView?.setMode(BaseLoadingView.DisplayMode.LOADING)
        MemberApi.fetchMembers(since) { isSuccess, data, throwable ->
            if (isSuccess) {
                if (data == null || data.members.isNullOrEmpty()) {
                    loadingView?.setMode(BaseLoadingView.DisplayMode.NO_DATA)
                } else {
                    setData(data)
                    loadingView?.setMode(BaseLoadingView.DisplayMode.NORMAL.delay(500))
                }
            } else {
                FCApplication.showToast(throwable?.response()?.errorBody()?.string() ?: getString(R.string.app_get_contact_failed))
                loadingView?.setMode(BaseLoadingView.DisplayMode.NO_NETWORK)
            }
        }
    }

    private fun setData(data: ContactMemberInfo.IncrementalMemberModel) {
        val lst = data.members
        val map = mutableMapOf<String, MutableList<ContactMemberInfo.MemberModel>>()
        lst?.groupByTo(map) {
            it.indexSymbol
        }
        val groupData = mutableListOf<ContactGroupInfo>()
        map.mapTo(groupData) {
            ContactGroupInfo(it.key, it.value)
        }
        adapter?.change(groupData)
    }
}
