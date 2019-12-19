package com.zj.preview.downloader.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import com.zj.preview.downloader.Constants;
import com.zj.preview.downloader.Progress;
import com.zj.preview.downloader.OnProgressListener;



public class ProgressHandler extends Handler {

    private final OnProgressListener listener;

    public ProgressHandler(OnProgressListener listener) {
        super(Looper.getMainLooper());
        this.listener = listener;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (msg.what == Constants.UPDATE) {
            if (listener != null) {
                final Progress progress = (Progress) msg.obj;
                listener.onProgress(progress);
            }
        } else {
            super.handleMessage(msg);
        }
    }
}
