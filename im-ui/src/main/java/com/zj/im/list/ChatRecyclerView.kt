package com.zj.im.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zj.im.list.interfaces.BaseChatModel
import com.zj.im.list.views.ChatItemView
import com.zj.list.multiable.MultiAbleData
import com.zj.list.multiable.MultiRecyclerAdapter

abstract class ChatRecyclerView<T : MultiAbleData<T>> @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attr, defStyle) {

    abstract fun createOptions(data: T): ChatItemOptions
    abstract fun getViewModel(data: T): BaseChatModel<T>

    open fun getItemId(data: T): Long? {
        return null
    }

    open fun getItemViewType(data: T): Int? {
        return null
    }

    open fun onBuildData(data: MutableList<T>?): MutableList<T>? {
        return data
    }

    private val multiAdapter: MultiRecyclerAdapter<T>

    override fun getAdapter(): MultiRecyclerAdapter<T> {
        return multiAdapter
    }

    final override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
    }

    init {
        multiAdapter = object : MultiRecyclerAdapter<T>() {

            override fun onBuildData(data: MutableList<T>?): MutableList<T>? {
                return this@ChatRecyclerView.onBuildData(data)
            }

            override fun getItemId(position: Int): Long {
                return this@ChatRecyclerView.getItemId(data().getDataWithPosition(position)) ?: super.getItemId(position)
            }

            override fun getItemViewType(position: Int): Int {
                return this@ChatRecyclerView.getItemViewType(data().getDataWithPosition(position)) ?: super.getItemViewType(position)
            }

            override fun onCreateView(parent: ViewGroup, viewType: Int): View {
                return ChatItemView(context)
            }

            override fun initData(itemView: View, data: T, position: Int, payloads: MutableList<Any>?) {
                (itemView as? ChatItemView)?.let {
                    val option = this@ChatRecyclerView.createOptions(data)
                    val mode = getViewModel(data)
                    it.initBase(option)
                    if (mode.isInitTimeStampView(data)) {
                        it.initTimeLine(option)
                    } else {
                        it.removeTimeLine()
                    }
                    if (mode.isInitInfoView(data)) {
                        it.initInfoLine(option)
                    } else {
                        it.removeInfoLine()
                    }
                    if (mode.isInitBaseBubbleView(data)) {
                        val orientation = mode.getOrientation(data)
                        it.initBaseBubbleView(orientation, option)
                    } else {
                        it.removeBaseBubbleView()
                    }
                    mode.initData(context, it, data, payloads)
                }
            }
        }
        adapter = multiAdapter
    }

    fun clear() {
        multiAdapter.clear()
    }
}
