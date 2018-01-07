package com.example.notificationsample.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.notificationsample.R;
import com.example.notificationsample.utils.NotificationChanelUtils;
import com.example.notificationsample.utils.NotifyConstant;
import java.io.IOException;

/**
 * 自定义通知栏
 */

public class CustomActivity extends AppCompatActivity {
    private static final String TAG = CustomActivity.class.getSimpleName();
    public NotificationManager mNotificationManager;
    /**
     * 是否在播放
     */
    public boolean isPlay = false;
    /**
     * 通知栏按钮点击事件对应的ACTION
     */
    public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    /**
     * 上一首 按钮点击 ID
     */
    public final static int BUTTON_PREV_ID = 1;
    /**
     * 播放/暂停 按钮点击 ID
     */
    public final static int BUTTON_PALY_ID = 2;
    /**
     * 下一首 按钮点击 ID
     */
    public final static int BUTTON_NEXT_ID = 3;
    /**
     * 通知栏按钮广播
     */
    public ButtonBroadcastReceiver bReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_custom);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        setListener();
        initButtonReceiver();
    }

    private void setListener() {
        findViewById(R.id.btn_show_custom).setOnClickListener(view -> shwoNotify());
        findViewById(R.id.btn_show_custom_button).setOnClickListener(view -> showButtonNotify());

    }



    public void shwoNotify() {
        NotificationChanelUtils.createNotificationChannel(mNotificationManager,NotifyConstant.channelId6, NotifyConstant.channelName6, NotifyConstant.channelImportance6, NotifyConstant.channelDesc6, NotifyConstant.groupId2);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId6);

        // 先设定RemoteViews
        RemoteViews view_custom = new RemoteViews(getPackageName(), R.layout.activity_notification_view_custom);
        view_custom.setImageViewResource(R.id.custom_icon, R.drawable.notification_icon);
        view_custom.setTextViewText(R.id.tv_custom_title, "今日头条");
        view_custom.setTextViewText(R.id.tv_custom_content, "金州勇士官方宣布球队已经解雇了主帅马克-杰克逊，随后宣布了最后的结果。");

        mBuilder.setContent(view_custom)
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("有新资讯").setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(false)// 不是正在进行的 true为正在进行 效果和.flag一样
                .setSmallIcon(R.drawable.notification_icon);
        addPendingIntent(mBuilder, NotifyToActivity.class);

        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId6), mBuilder.build());
    }

    /**
     * 带按钮的通知栏
     */
    public void showButtonNotify() {
        NotificationChanelUtils.createNotificationChannel(mNotificationManager,NotifyConstant.channelId7, NotifyConstant.channelName7, NotifyConstant.channelImportance7, NotifyConstant.channelDesc7, NotifyConstant.groupId2);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId7);

        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification_view_custom_button);
        mRemoteViews.setImageViewResource(R.id.custom_song_icon, R.drawable.notification_sing_icon);

        mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, "周杰伦");
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, "七里香");
        if (isPlay) {
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.notification_btn_pause);
        } else {
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.notification_btn_play);
        }

        clickMusic(mRemoteViews);

        mBuilder.setContent(mRemoteViews)
                .setWhen(System.currentTimeMillis())
                // 通知产生的时间，会在通知信息里显示
                .setTicker("正在播放").setPriority(Notification.PRIORITY_DEFAULT)
                // 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.drawable.notification_sing_icon);
        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId7), mBuilder.build());
    }

    private void clickMusic(RemoteViews mRemoteViews) {
        // 点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        /* 上一首按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
        // 这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);
        /* 播放/暂停 按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly);
		/* 下一首 按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);
    }

    /**
     * 带按钮的通知栏点击广播接收
     */
    public void initButtonReceiver() {
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver, intentFilter);
    }

    /**
     * 广播监听按钮点击时间
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_BUTTON.equals(action)) {
                // 通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_PREV_ID:
                        Toast.makeText(getApplicationContext(), "上一首", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_PALY_ID:
                        String play_status = "";
                        isPlay = !isPlay;
                        MediaPlayer play = null;
                        if (isPlay) {
                            play_status = "开始播放";
                            playBackgroundMusic();
                        } else {
                            play_status = "已暂停";
                            stopBackgroundMusic();
                        }
                        showButtonNotify();
                        Log.d(TAG, play_status);
                        Toast.makeText(getApplicationContext(), play_status, Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_NEXT_ID:
                        Log.d(TAG, "下一首");
                        Toast.makeText(getApplicationContext(), "下一首", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private MediaPlayer mplayer;

    /*
	 * 后台播放背景音
	 */
    private void playBackgroundMusic() {
        if (mplayer == null) {
            mplayer = new MediaPlayer();
            try {
                AssetFileDescriptor afd = this.getAssets().openFd("angel.mp3");
                // 获取音乐数据源
                mplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mplayer.setLooping(true); // 设为循环播放
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (mplayer.isPlaying()) {
                return;
            }
            mplayer.prepare();
            mplayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
	 * 停止播放背景音乐
	 */
    private void stopBackgroundMusic() {
        try {
            if (null != mplayer) {
                if (mplayer.isPlaying()) {
                    mplayer.pause();
					mplayer.release();
                    mplayer = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加PendingIntent
     * @param mBuilder
     */
    private void addPendingIntent(NotificationCompat.Builder mBuilder, Class clz) {
        Intent resultIntent = new Intent(this, clz);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(clz);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
    }
}
