package com.example.notificationsample.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;
import com.example.notificationsample.R;
import com.example.notificationsample.utils.NotificationChanelUtils;
import com.example.notificationsample.utils.NotifyConstant;

/**
 * 带进步的通知栏
 */

public class ProgressActivity extends AppCompatActivity {
    public NotificationManager mNotificationManager;
    int progress = 0;
    public boolean isPause = false;
    public boolean isCustom = false;
    DownloadThread downloadThread;
    public Boolean indeterminate = false;
    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_progress);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId8);
        initNotify();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.btn_show_progress).setOnClickListener(view -> {
            downloadThread = null;
            isCustom = false;
            indeterminate = false;
            showProgressNotify();
        });
        findViewById(R.id.btn_show_un_progress).setOnClickListener(view -> {
            downloadThread = null;
            isCustom = false;
            indeterminate = true;
            showProgressNotify();
        });
        findViewById(R.id.btn_show_custom_progress).setOnClickListener(view -> {
            downloadThread = null;
            isCustom = false;
            indeterminate = true;
            showProgressNotify();
        });
        findViewById(R.id.btn_download_start).setOnClickListener(view -> startDownloadNotify());
        findViewById(R.id.btn_download_pause).setOnClickListener(view -> pauseDownloadNotify());
        findViewById(R.id.btn_download_cancel).setOnClickListener(view -> stopDownloadNotify());
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {
        @Override
        public void run() {
            int now_progress = 0;
            while (now_progress <= 100) {
                if (downloadThread == null) {break;}
                if (!isPause) {
                    progress = now_progress;
                    if (!isCustom) {
                        mBuilder.setContentTitle("下载中");
                        if (!indeterminate) {
                            setNotify(progress);
                        }
                    } else {
                        showCustomProgressNotify("下载中");
                    }
                    now_progress += 10;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (downloadThread != null) {
                if (!isCustom) {
                    mBuilder.setContentText("下载完成")
                            .setProgress(0, 0, false);
                    mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId8), mBuilder.build());
                } else {
                    showCustomProgressNotify("下载完成");
                }
            }
        }
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        NotificationChanelUtils.createNotificationChannel(mNotificationManager, NotifyConstant.channelId8, NotifyConstant.channelName8, NotifyConstant.channelImportance8, NotifyConstant.channelDesc8, NotifyConstant.groupId1);
        mBuilder.setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.notification_icon);
    }

    /**
     * 显示带进度条通知栏
     */
    public void showProgressNotify() {
        mBuilder.setContentTitle("等待下载")
                .setContentText("进度:")
                .setTicker("开始下载");// 通知首次出现在通知栏，带上升动画效果的
        if (indeterminate) {
            //不确定进度的
            mBuilder.setProgress(0, 0, true);
        } else {
            //确定进度的
            mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条  设置为true就是不确定的那种进度条效果
        }
        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId8), mBuilder.build());
    }

    /**
     * 显示自定义的带进度条通知栏
     */
    private void showCustomProgressNotify(String status) {
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification_view_custom_progress);
        mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.drawable.notification_icon);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, "今日头条");
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, status);
        if (progress >= 100 || downloadThread == null) {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 0, 0, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
        } else {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, progress, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
        }
        mBuilder.setContent(mRemoteViews)
                .setTicker("头条更新");

        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId8), mBuilder.build());
    }

    /**
     * 开始下载
     */
    public void startDownloadNotify() {
        isPause = false;
        if (downloadThread != null && downloadThread.isAlive()) {
            downloadThread.start();
        } else {
            downloadThread = new DownloadThread();
            downloadThread.start();
        }
    }

    /**
     * 暂停下载
     */
    public void pauseDownloadNotify() {
        isPause = true;
        if (!isCustom) {
            mBuilder.setContentTitle("已暂停");
            setNotify(progress);
        } else {
            showCustomProgressNotify("已暂停");
        }
    }

    /**
     * 取消下载
     */
    public void stopDownloadNotify() {
        if (downloadThread != null) {downloadThread.interrupt();}
        downloadThread = null;
        if (!isCustom) {
            mBuilder.setContentTitle("下载已取消").setProgress(0, 0, false);
            mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId8), mBuilder.build());
        } else {
            showCustomProgressNotify("下载已取消");
        }
    }

    /**
     * 设置下载进度
     */
    public void setNotify(int progress) {
        mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条
        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId8), mBuilder.build());
    }

}
