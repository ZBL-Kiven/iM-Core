package com.zj.imcore.gui.login.pager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class LoginPagerAdapter(val onViewSet: (p: Int, v: View) -> Unit) : PagerAdapter() {

    var ids: List<Int>? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view.tag == obj
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        ids?.let { data ->
            val id = data[position]
            val view = LayoutInflater.from(parent.context).inflate(id, parent, false)
            view.tag = position
            parent.addView(view, ViewPager.LayoutParams())
            onViewSet(position, view)
            return view.tag
        }
        return 0
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeAllViews()
    }

    override fun getCount(): Int {
        return ids?.size ?: 0
    }
}