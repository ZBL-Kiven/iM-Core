package com.zj.ui.emotionboard.interfaces

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zj.ui.R
import com.zj.ui.emotionboard.data.Emoticon
import com.zj.ui.emotionboard.utils.imageloader.ImageLoader

open class GridPageFactory<T : Emoticon> : PageFactory<T> {

    override fun create(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): View {
        val pageView = RecyclerView(context)
        val lm = GridLayoutManager(context, 7)
        pageView.layoutManager = lm
        pageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val adapter = createAdapter(context, emoticons, clickListener)
        pageView.adapter = adapter
        return pageView
    }

    open fun <T : Emoticon> createAdapter(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): RecyclerView.Adapter<*> {
        return ImageAdapter(context, emoticons, clickListener)
    }
}

class ImageAdapter<T : Emoticon>(context: Context, private val emoticons: List<T>, private val clickListener: OnEmoticonClickListener<Emoticon>?) : RecyclerView.Adapter<ImageAdapter.ImgViewHolder>() {

    private val defaultItemSize = context.resources.getDimension(R.dimen.item_emoticon_size_default).toInt()
    private val padding = context.resources.getDimension(R.dimen.item_emoticon_padding).toInt()

    override fun getItemCount(): Int {
        return emoticons.size
    }

    private fun getItem(position: Int): Emoticon = emoticons[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
        val iv = ImageView(parent.context)
        iv.scaleType = ImageView.ScaleType.CENTER
        val lp = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, defaultItemSize)
        iv.setPadding(0, padding, 0, padding)
        iv.layoutParams = lp
        return ImgViewHolder(iv)
    }

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        holder.itemView.setBackgroundResource(R.drawable.ui_bg_emoticon)
        val image = holder.itemView as ImageView
        val data = getItem(position)
        val uri = data.uri
        if (uri != null) ImageLoader.displayImage(uri, image)
        holder.itemView.setOnClickListener {
            clickListener?.onEmoticonClick(data)
        }
    }

    class ImgViewHolder(v: View) : RecyclerView.ViewHolder(v)
}