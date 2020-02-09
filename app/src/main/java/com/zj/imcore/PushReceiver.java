package com.zj.imcore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("收到广播");
        Toast.makeText(context, "收到广播", Toast.LENGTH_SHORT).show();
    }
}
