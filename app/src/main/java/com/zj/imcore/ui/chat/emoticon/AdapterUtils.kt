package com.zj.imcore.ui.chat.emoticon

import android.content.Context
import com.sj.emoji.DefEmoticons
import com.zj.ui.emotionboard.adpater.EmoticonPacksAdapter
import com.zj.ui.emotionboard.data.Emoticon
import com.zj.ui.emotionboard.data.EmoticonPack
import com.zj.ui.emotionboard.interfaces.OnEmoticonClickListener
import com.zj.ui.emotionboard.utils.getResourceUri
import com.zj.imcore.R

object AdapterUtils {

    fun getAdapter(context: Context, emoticonClickListener: OnEmoticonClickListener<Emoticon>?): EmoticonPacksAdapter {
        val packs = mutableListOf<EmoticonPack<out Emoticon>>()
        packs.add(getEmoji(context))
        val adapter = EmoticonPacksAdapter(packs)
        adapter.setClickListener(emoticonClickListener)
        return adapter
    }

    private fun getEmoji(context: Context): EmoticonPack<Emoticon> {
        val emojiArray = mutableListOf<Emoticon>()
        DefEmoticons.sEmojiArray.mapTo(emojiArray) {
            val emoticon = Emoticon()
            emoticon.code = it.emoji
            emoticon.uri = context.getResourceUri(it.icon)
            return@mapTo emoticon
        }
        val pack = EmoticonPack<Emoticon>()
        pack.emoticons = emojiArray
        pack.iconUri = context.getResourceUri(R.mipmap.icon_face_thumb)
        return pack
    }

    class DeleteEmoticon : Emoticon()

    class BigEmoticon: Emoticon()
}
