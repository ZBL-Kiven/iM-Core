package com.zj.im.emotionboard.data

import android.content.Context
import android.view.View
import com.zj.im.emotionboard.interfaces.GridPageFactory
import com.zj.im.emotionboard.interfaces.OnEmoticonClickListener
import com.zj.im.emotionboard.interfaces.PageFactory

class EmoticonPack<T : Emoticon> {
    var iconUri: String? = null
    var name: String? = null
    var pageFactory: PageFactory<T> = GridPageFactory()
    var isDataChanged = false
    var tag: Any? = null
    lateinit var emoticons: MutableList<T>

    fun getView(context: Context, listener: OnEmoticonClickListener<Emoticon>?): View {
        return pageFactory.create(context, emoticons, listener)
    }
}