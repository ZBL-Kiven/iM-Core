package com.zj.imcore.base

import androidx.core.content.ContextCompat
import com.zj.base.BaseActivity
import com.zj.base.view.BaseTitleView
import com.zj.imcore.R

abstract class FCActivity : BaseActivity() {

    override fun initTitleBar(baseTitleView: BaseTitleView?) {
        baseTitleView?.setTitleColor(R.color.color_primary_light_grey)
        baseTitleView?.setTitleTextSizeRef(R.dimen.app_title_text_size)
        baseTitleView?.setBackgroundColor(ContextCompat.getColor(this, R.color.color_primary))
    }


}