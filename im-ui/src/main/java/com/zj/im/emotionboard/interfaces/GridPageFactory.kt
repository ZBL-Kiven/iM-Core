package com.zj.im.emotionboard.interfaces

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.zj.im.R
import com.zj.im.emotionboard.data.Emoticon
import com.zj.im.emotionboard.utils.imageloader.ImageLoader
import com.zj.im.emotionboard.widget.EmoticonPageView

open class GridPageFactory<T : Emoticon> : PageFactory<T> {

    private var line = 3
    private var row = 7

    override fun emoticonsCapacity(): Int {
        return Int.MAX_VALUE
    }

    override fun create(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): View {
        val pageView = EmoticonPageView(context)

        pageView.setNumColumns(row)

        val adapter = createAdapter(context, emoticons, clickListener)
        pageView.emoticonsGridView.adapter = adapter
        return pageView
    }

    open fun <T : Emoticon> createAdapter(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): BaseAdapter {
        val adapter = ImageAdapter(context, emoticons, clickListener)
        adapter.line = line

        return adapter
    }
}

abstract class GridAdapter<T : Emoticon, V>(val context: Context, private val emoticons: List<T>, val clickListener: OnEmoticonClickListener<Emoticon>?) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    @Suppress("UNCHECKED_CAST")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertTempView = convertView

        val viewHolder: V
        if (convertTempView == null) {
            convertTempView = createConvertView(inflater)
            viewHolder = createViewHolder(convertTempView)
            convertTempView.tag = viewHolder
        } else {
            viewHolder = convertTempView.tag as V
        }

        bindView(getItem(position) as T, parent, viewHolder)
        setViewHolderLayoutParams(viewHolder, parent)
        return convertTempView
    }

    abstract fun createViewHolder(convertView: View): V

    abstract fun createConvertView(inflater: LayoutInflater): View

    override fun getItem(position: Int): Any = emoticons[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = emoticons.size

    abstract fun bindView(item: T, parent: ViewGroup, viewHolder: V)

    abstract fun setViewHolderLayoutParams(viewHolder: V, parent: ViewGroup)
}

open class ImageAdapter<T : Emoticon>(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?) : GridAdapter<T, ImageAdapter.ViewHolder>(context, emoticons, clickListener) {

    private val defHeightMaxRatio = 2.toDouble()
    private val defaultItemHeight = context.resources.getDimension(R.dimen.item_emoticon_size_default).toInt()
    private var itemHeight = defaultItemHeight
    private var itemHeightMax = 0
    private var itemHeightMin = 0
    private var itemHeightMaxRatio = defHeightMaxRatio
    var line = 0

    override fun createViewHolder(convertView: View): ViewHolder {
        val viewHolder = ViewHolder()
        viewHolder.rootView = convertView
        viewHolder.rootLayout = convertView.findViewById(R.id.ly_root) as LinearLayout
        viewHolder.imageView = convertView.findViewById(R.id.iv_emoticon) as ImageView

        return viewHolder
    }

    @SuppressLint("InflateParams")
    override fun createConvertView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.item_emoticon, null)
    }

    override fun bindView(item: T, parent: ViewGroup, viewHolder: ViewHolder) {

        viewHolder.rootLayout?.setBackgroundResource(R.drawable.ui_bg_emoticon)

        val uri = item.uri

        val image = viewHolder.imageView

        if (image != null && uri != null) {
            ImageLoader.displayImage(uri, image)
        }

        viewHolder.rootView?.setOnClickListener {
            clickListener?.onEmoticonClick(item)
        }
    }

    override fun setViewHolderLayoutParams(viewHolder: ViewHolder, parent: ViewGroup) {
        if (defaultItemHeight != itemHeight) {
            viewHolder.imageView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight)
        }
        itemHeightMax = if (itemHeightMax != 0) itemHeightMax else (itemHeight * itemHeightMaxRatio).toInt()
        itemHeightMin = if (this.itemHeightMin != 0) this.itemHeightMin else itemHeight

        var realItemHeight = (parent.parent as View).measuredHeight / line
        realItemHeight = Math.min(realItemHeight, itemHeightMax)
        realItemHeight = Math.max(realItemHeight, itemHeightMin)

        viewHolder.rootLayout?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, realItemHeight)
    }

    class ViewHolder {
        var rootView: View? = null
        var rootLayout: LinearLayout? = null
        var imageView: ImageView? = null
    }
}