package com.zj.im.emotionboard.interfaces

import android.content.Context
import android.view.View
import com.zj.im.emotionboard.data.Emoticon

interface PageFactory<T: Emoticon> {
    /**
     * E'mojis Per Page
     */
    fun emoticonsCapacity(): Int

    /**
     * Create an e'moji View
     */
    fun create(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): View
}