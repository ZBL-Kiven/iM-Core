package com.zj.im.emotionboard.widget

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.zj.im.R
import com.zj.im.emotionboard.data.Emoticon
import com.zj.im.emotionboard.data.EmoticonPack
import com.zj.im.emotionboard.interfaces.EmoticonsToolBar
import com.zj.im.emotionboard.interfaces.OnToolBarItemClickListener
import com.zj.im.emotionboard.utils.imageloader.ImageLoader

@Suppress("unused")
open class EmotionsTabBar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), EmoticonsToolBar {

    private var recyclerView = RecyclerView(context)
    private var layoutManager: SmoothScrollLayoutManager? = null
    private var emotionPacks: List<EmoticonPack<out Emoticon>>? = null
    private var leftView = FrameLayout(context)
    private var rightView = FrameLayout(context)
    private var adapterFactory: EmotionsTabAdapterFactory<out RecyclerView.ViewHolder>? = null

    constructor(context: Context) : this(context, null)

    init {
        orientation = HORIZONTAL

        addFixView(leftView)
        addRecyclerView(context)
        addFixView(rightView)
    }

    private fun addFixView(view: View) {
        val params = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 0f)
        params.gravity = Gravity.CENTER
        view.layoutParams = params
        addView(view)
    }

    private fun addRecyclerView(context: Context) {
        recyclerView.layoutParams = LayoutParams(0, WRAP_CONTENT, 1f)
        layoutManager = SmoothScrollLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        addView(recyclerView)

        adapterFactory = DefaultAdapterFactory()
    }

    override fun setToolBarItemClickListener(listener: OnToolBarItemClickListener?) {
        adapterFactory?.itemClickListeners = listener
    }

    override fun selectEmotionPack(pack: EmoticonPack<out Emoticon>) {
        val position = emotionPacks?.indexOf(pack)

        val manager = layoutManager

        if (position != null && position >= 0 && manager != null) {
            val firstPosition = manager.findFirstVisibleItemPosition()
            val lastPosition = manager.findLastVisibleItemPosition()

            if (position < firstPosition) {
                manager.isMoveToTop = true
                recyclerView.smoothScrollToPosition(position)
            } else if (position > lastPosition) {
                manager.isMoveToTop = false
                recyclerView.smoothScrollToPosition(position)
            }
        }

        if (position != null) {
            adapterFactory?.onEmotionPackSelect(position)

            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun setPackList(packs: List<EmoticonPack<out Emoticon>>) {
        emotionPacks = packs

        recyclerView.adapter = adapterFactory?.createAdapter(packs)
    }

    override fun addFixedToolItemView(view: View?, isRight: Boolean) {
        if (view != null) {
            val container = if (isRight) rightView else leftView

            container.addView(view)
        }
    }

    override fun notifyDataChanged() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    interface EmotionsTabAdapterFactory<T : RecyclerView.ViewHolder> {
        var itemClickListeners: OnToolBarItemClickListener?
        fun createAdapter(packs: List<EmoticonPack<out Emoticon>>): RecyclerView.Adapter<out T>

        fun onEmotionPackSelect(position: Int)
    }

    open class DefaultAdapterFactory : EmotionsTabAdapterFactory<EmotionPackTabAdapter.ViewHolder> {

        override var itemClickListeners: OnToolBarItemClickListener? = null
        private lateinit var packList: List<EmoticonPack<out Emoticon>>

        override fun createAdapter(packs: List<EmoticonPack<out Emoticon>>): RecyclerView.Adapter<out EmotionPackTabAdapter.ViewHolder> {
            packList = packs

            val adapter = getAdapter(packList)
            adapter.itemClickListeners = itemClickListeners

            return adapter
        }

        protected open fun getAdapter(packs: List<EmoticonPack<out Emoticon>>): EmotionPackTabAdapter {
            return EmotionPackTabAdapter(packs)
        }

        override fun onEmotionPackSelect(position: Int) {
            packList.forEachIndexed { index, pair ->
                pair.tag = index == position
            }
        }

    }
}

/**
 * packs: MutablePair first is selected state
 */
open class EmotionPackTabAdapter(private val packs: List<EmoticonPack<out Emoticon>>)
    : RecyclerView.Adapter<EmotionPackTabAdapter.ViewHolder>() {

    var itemClickListeners: OnToolBarItemClickListener? = null

    init {
        packs.forEach {
            it.tag = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.left_toolbtn, parent, false)

        return ViewHolder(view)
    }


    override fun getItemCount() = packs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.icon.context

        ImageLoader.displayImage(packs[position].iconUri!!, holder.icon)

        if (packs[position].tag == null) {
            // this data is not form set constructor
            packs[position].tag = false
        }

        if (packs[position].tag as Boolean) {
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_primary_light_grey))
        } else {
            holder.rootView.setBackgroundResource(R.drawable.ui_bg_toolbtn_bg)
        }

        holder.rootView.isClickable = true

        holder.rootView.setOnClickListener {
            itemClickListeners?.onToolBarItemClick(packs[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        var icon = itemView.findViewById(R.id.iv_icon) as ImageView
    }
}