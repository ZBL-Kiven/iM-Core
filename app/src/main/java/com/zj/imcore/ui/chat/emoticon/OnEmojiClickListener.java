package com.zj.imcore.ui.chat.emoticon;

import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import com.zj.im.emotionboard.data.Emoticon;
import com.zj.im.emotionboard.interfaces.OnEmoticonClickListener;
import com.zj.imcore.im.options.IMHelper;

public abstract class OnEmojiClickListener implements OnEmoticonClickListener<Emoticon> {

    public abstract EditText getEt();

    public abstract String getSessionId();

    @Override
    public void onEmoticonClick(Emoticon emoticon) {
        if (emoticon == null) {
            return;
        }
        if (emoticon instanceof AdapterUtils.DeleteEmoticon) {
            int action = KeyEvent.ACTION_DOWN;
            int code = KeyEvent.KEYCODE_DEL;
            KeyEvent event = new KeyEvent(action, code);
            getEt().onKeyDown(KeyEvent.KEYCODE_DEL, event);
        } else if (emoticon instanceof AdapterUtils.BigEmoticon) {
            IMHelper.INSTANCE.sendSticker(getSessionId(), emoticon.getUri());
        } else {
            String content = emoticon.getCode();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            int index = getEt().getSelectionStart();
            Editable editable = getEt().getText();
            editable.insert(index, content);
        }
    }
}
