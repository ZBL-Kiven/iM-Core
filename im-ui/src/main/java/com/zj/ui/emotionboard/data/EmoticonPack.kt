package com.zj.ui.emotionboard.data

import android.content.Context
import android.view.View
import com.zj.ui.emotionboard.interfaces.GridPageFactory
import com.zj.ui.emotionboard.interfaces.OnEmoticonClickListener
import com.zj.ui.emotionboard.interfaces.PageFactory

class EmoticonPack<T : Emoticon> {
    var iconUri: String? = null
    var name: String? = null
    private var pageFactory: PageFactory<T> = GridPageFactory()
    var isDataChanged = false
    var tag: Any? = null
    lateinit var emoticons: MutableList<T>

    fun getView(context: Context, listener: OnEmoticonClickListener<Emoticon>?): View {
        return pageFactory.create(context, emoticons, listener)
    }
}