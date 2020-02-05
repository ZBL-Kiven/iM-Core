package com.zj.imcore.ui.main.contact.group

import com.zj.base.view.BaseTitleView
import com.zj.imcore.R
import com.zj.imcore.base.FCActivity
import com.zj.loading.BaseLoadingView

class MyGroupsActivity : FCActivity() {

    private var titleView: BaseTitleView? = null
    private var loadingView: BaseLoadingView? = null

    override fun getContentId(): Int {
        return R.layout.app_act_contact_group
    }

    override fun initView() {
        loadingView = find(R.id.app_act_contact_groups_blv)
        titleView = find(R.id.app_act_contact_groups_title)
    }

    override fun initData() {

    }

    override fun initListener() {

    }
}