package com.zj.imcore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cf.im.db.repositorys.DialogRepository;
import com.zj.imcore.ui.chat.ChatActivity;

import java.util.Objects;

public class PushReceiver extends BroadcastReceiver {

    public final static String ACTION_PUSH_CLICK = "com.cf.im.action.CLICK";
    public final static String ACTION_PUSH_DELETE = "com.cf.im.action.DELETE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_PUSH_CLICK.equals(action)) {
            System.out.println("收到点击广播");
            event(context, intent);
        }

        if (ACTION_PUSH_DELETE.equals(action)) {
            System.out.println("收到删除通知");
            Toast.makeText(context, "收到删除通知", Toast.LENGTH_SHORT).show();
        }
    }

    private void event(Context context, Intent intent) {
        String key = Objects.requireNonNull(intent.getExtras()).getString("key");
        if (key != null) {
            //如果APP 是打开的状态
            DialogRepository.queryDialogById(key, dialogBean -> {
                if (dialogBean != null) {
                    ChatActivity.Companion.start(
                            context,
                            dialogBean.dialogId,
                            Constance.DIALOG_TYPE_P2P,
                            dialogBean.tmId,
                            "",
                            dialogBean.name
                    );
                }

            });
        }
    }


}
