package com.zj.imcore.core.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;

import com.zj.imcore.PushReceiver;
import com.zj.imcore.R;

public class NotificationService extends ContextWrapper {

    public static final String DEFAULT_CHANNEL_ID = "DEFAULT_CHANNEL_ID";
    public static final String DEFAULT_CHANNEL_NAME = "DEFAULT_CHANNEL_NAME";


    private String channelId;
    private String channelName;
    private String channelDescribe;
    private NotificationManager mManager;

    public NotificationService(Context context) {
        this(context, DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, "");
    }

    public NotificationService(Context context, @StringRes int channelId, @StringRes int channelName) {
        this(context, context.getString(channelId), context.getString(channelName), "");
    }


    public NotificationService(Context context, @StringRes int channelId, @StringRes int channelName, @StringRes int channelDescribe) {
        this(context, context.getString(channelId), context.getString(channelName), context.getString(channelDescribe));
    }

    public NotificationService(Context context, String channelId, String channelName, String channelDescribe) {
        super(context);
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelDescribe = channelDescribe;
    }


    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public void sendNotification(String title, String content) {
        sendNotification(title, content, true, true);
    }

    public void sendNotification(String title, String content, boolean voice, boolean vibration) {
        Notification notification;
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(channelId, channelName, channelDescribe);
            notification = getNotification_26(channelId, title, content).build();
        } else {
            notification = getNotification_25(title, content).build();
        }

        getManager().notify((int) (System.currentTimeMillis() / 1000), notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(String channelId, String channelName, String channelDescribe) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(channelDescribe);

        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);//设置是否应在锁定屏幕上显示此频道的通知
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 600});//设置震动频率
        channel.setBypassDnd(true);//设置是否绕过免打扰模式

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes);
        getManager().createNotificationChannel(channel);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        NotificationCompat.BigTextStyle style1 = new NotificationCompat.BigTextStyle();
        style1.setBigContentTitle(title);
        style1.bigText(content);
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(getIntent())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 200, 100, 200});
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification_26(String id, String title, String content) {

        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(Notification.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(getIntent())
                .setNumber(1)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{0, 200, 100, 200});
    }

    private PendingIntent getIntent() {
        Intent clickIntent = new Intent(getBaseContext(), PushReceiver.class); //点击通知之后要发送的广播
        return PendingIntent.getBroadcast(this.getApplicationContext(), 100, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

