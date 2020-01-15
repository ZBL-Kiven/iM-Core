package com.zj.im.emotionboard.data

import android.content.Context
import android.view.View
import com.zj.im.emotionboard.interfaces.OnEmoticonClickListener
import com.zj.im.emotionboard.interfaces.GridPageFactory
import com.zj.im.emotionboard.interfaces.PageFactory

class EmoticonPack<T : Emoticon> {
    var iconUri: String? = null
    var name: String? = null
    private lateinit var emoticons: MutableList<T>
    private var pageFactory: PageFactory<T> = GridPageFactory()
    var isDataChanged = false
    var tag: Any? = null

    val pageCount: Int
        get() {
            var count = emoticons.size / pageFactory.emoticonsCapacity()
            if (emoticons.size % pageFactory.emoticonsCapacity() > 0) {
                count++
            }
            return count
        }


    fun getView(context: Context, pageIndex: Int, listener: OnEmoticonClickListener<Emoticon>?): View {
        return pageFactory.create(context, getEmoticons(pageIndex), listener)
    }

    private fun getEmoticons(pageIndex: Int): List<T> {

        val fromIndex = pageIndex * pageFactory.emoticonsCapacity()
        val toIndex = Math.min((pageIndex + 1) * pageFactory.emoticonsCapacity(), emoticons.size)

        return emoticons.subList(fromIndex, toIndex)
    }
}