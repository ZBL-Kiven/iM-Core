package com.zj.im.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zj.im.list.items.ChatItemOptions
import com.zj.im.list.items.ChatItemView
import com.zj.list.multiable.MultiAbleData
import com.zj.list.multiable.MultiRecyclerAdapter

abstract class ChatRecyclerView<T : MultiAbleData<T>> @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attr, defStyle) {

    abstract fun initData(itemView: ChatItemView?, data: T, payloads: MutableList<Any>?)
    abstract fun initOptions(data: T): ChatItemOptions

    private val multiAdapter: MultiRecyclerAdapter<T>

    override fun getAdapter(): MultiRecyclerAdapter<T> {
        return multiAdapter
    }

    init {
        multiAdapter = object : MultiRecyclerAdapter<T>() {
            override fun onCreateView(parent: ViewGroup, viewType: Int): View {
                return ChatItemView(context)
            }

            override fun initData(itemView: View?, data: T, payloads: MutableList<Any>?) {
                (itemView as? ChatItemView)?.let {
                    it.init(initOptions(data))
                    this@ChatRecyclerView.initData(it, data, payloads)
                }
            }
        }
        adapter = multiAdapter
    }

}
