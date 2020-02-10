package com.zj.imcore.core.notification;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cf.im.db.databases.BaseSingleton;
import com.cf.im.db.repositorys.DialogRepository;
import com.zj.base.BaseApplication;
import com.zj.base.utils.storage.sp.SPUtils_Proxy;
import com.zj.imcore.R;
import com.zj.imcore.base.FCApplication;
import com.zj.imcore.ui.chat.ChatActivity;
import com.zj.model.chat.MsgInfo;

public class NotificationManager {

    public static BaseSingleton<NotificationManager> singleton = new BaseSingleton<NotificationManager>() {
        @Override
        protected NotificationManager create(String... arg) {
            return new NotificationManager();
        }
    };

    private Handler mHandler;

    private Context mContext;

    private NotificationManager() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private NotificationService messageNotification;

    public void sendMessageNotification(MsgInfo info) {
        if (messageNotification == null) {
            messageNotification = new NotificationService(mContext, R.string.app_notification_new_message_id, R.string.app_notification_new_message, R.string.app_notification_new_message_describe);
        }

        //业务处理
        if (TextUtils.isEmpty(info.getCallId())) {
            return;
        }

        //判断是否是当前自己
        if (info.isSelf(SPUtils_Proxy.getUserId(-1L))) {
            return;
        }
        long currentDialogId = -1;

        Activity activity = BaseApplication.Companion.getAct();
        if (activity instanceof ChatActivity) {
            ChatActivity act = (ChatActivity) activity;
            currentDialogId = act.getSessionId();
        }

        //判断当前窗口 dialogId 是否是 messageId
        if (info.getDialogId() == currentDialogId) {
            return;
        }

        DialogRepository.queryByDialogId(info.getDialogId(), dialogBean -> {
            //判断当前消息是否是不提示状态 且消息不属于@ 本人
            String title = dialogBean != null ? dialogBean.getTitle() + "" : info.getName();



            mHandler.post(new Task(title, info.getName() + ": " + info.getText()));
        });
    }

    private class Task implements Runnable {

        private String taskTitle;
        private String taskContent;

        public Task(String taskTitle, String taskContent) {
            this.taskTitle = taskTitle;
            this.taskContent = taskContent;
        }

        @Override
        public void run() {
            messageNotification.sendNotification(taskTitle, taskContent);
        }
    }

}
