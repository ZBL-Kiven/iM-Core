package com.zj.im.emotionboard.interfaces

import com.zj.im.emotionboard.data.EmoticonPack

interface EmoticonsIndicator {
    /**
     * Move to an emoticon
     * @param position Pages in emoticons
     * @param pack the pack to be move
     */
    fun playTo(position: Int, pack: EmoticonPack<*>)

    fun notifyDataChanged()
}
