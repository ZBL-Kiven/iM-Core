package com.zj.ui.emotionboard.interfaces

import android.content.Context
import android.view.View
import com.zj.ui.emotionboard.data.Emoticon

interface PageFactory<T: Emoticon> {
    /**
     * Create an e'moji View
     */
    fun create(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): View
}