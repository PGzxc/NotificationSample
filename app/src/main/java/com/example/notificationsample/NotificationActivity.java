package com.example.notificationsample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.notificationsample.activity.CustomActivity;
import com.example.notificationsample.activity.NotifyToActivity;
import com.example.notificationsample.activity.ProgressActivity;
import com.example.notificationsample.utils.FileUtils;
import com.example.notificationsample.utils.NotificationChanelUtils;
import com.example.notificationsample.utils.NotifyConstant;

import java.io.File;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Notify
 */

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = NotificationActivity.class.getSimpleName();
    /**
     * Notification的ID
     */
    public NotificationManager mNotificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createGroup();
        setListener();
    }

    /**
     * 创建组
     */
    private void createGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(NotifyConstant.groupId1, NotifyConstant.groupName1));
            mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(NotifyConstant.groupId2, NotifyConstant.groupName2));
        }
    }

    protected void setListener() {
        findViewById(R.id.btn_show).setOnClickListener(view -> showNotify()); //显示通知栏
        findViewById(R.id.btn_bigstyle_show).setOnClickListener(view -> showBigStyleNotify()); //显示大视图风格通知栏
        findViewById(R.id.btn_show_cz).setOnClickListener(view -> showCzNotify()); //显示常驻通知栏
        findViewById(R.id.btn_show_intent_act).setOnClickListener(view -> showIntentActivityNotify()); //显示通知，点击跳转到指定Activity
        findViewById(R.id.btn_show_intent_apk).setOnClickListener(view -> showIntentApkNotify()); //显示通知，点击打开APK
        findViewById(R.id.btn_clear).setOnClickListener(view -> clearNotify(NotifyConstant.channelId1)); //清除指定通知
        findViewById(R.id.btn_clear_all).setOnClickListener(view -> clearAll()); //清除所有通知
        findViewById(R.id.btn_show_custom).setOnClickListener(view -> startActivity(new Intent(this, CustomActivity.class))); //显示自定义通知栏
        findViewById(R.id.btn_show_progress).setOnClickListener(view -> startActivity(new Intent(this, ProgressActivity.class))); //显示带进度条通知栏

    }

    /**
     * 显示通知栏
     */
    private void showNotify() {
        NotificationChanelUtils.createNotificationChannel(mNotificationManager, NotifyConstant.channelId1, NotifyConstant.channelName1, NotifyConstant.channelImportance1, NotifyConstant.channelDesc1, NotifyConstant.groupId1);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId1);
        mBuilder.setContentTitle("测试标题")
                .setContentText("测试内容")
                .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis()) //通知产生的时间，会在通知信息里显示
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false) //true，设置他为一个正在进行的通知。它们通常是用来表示一个后台任务，用户积极
                .setSmallIcon(R.drawable.ic_launcher);

        addPendingIntent(mBuilder, NotifyToActivity.class);

        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId1), mBuilder.build());
    }

    /**
     * 显示大视图风格通知栏
     */
    private void showBigStyleNotify() {

        NotificationChanelUtils.createNotificationChannel(mNotificationManager, NotifyConstant.channelId2, NotifyConstant.channelName2, NotifyConstant.channelImportance2, NotifyConstant.channelDesc2, NotifyConstant.groupId1);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId2);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        String[] events = new String[5];
//        // Sets a title for the Inbox style big view
//        inboxStyle.setBigContentTitle("大视图内容:");
//        // Moves events into the big view
//        for (int i=0; i < events.length; i++) {
//            inboxStyle.addLine(events[i]);
//        }
        mBuilder.setContentTitle("大视图内容测试标题")
                .setContentText("大视图内容测试内容")
                .setStyle(inboxStyle)//设置风格
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("大视图内容测试通知来啦");// 通知首次出现在通知栏，带上升动画效果的

        addPendingIntent(mBuilder, MainActivity.class);
        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId2), mBuilder.build());
    }

    /**
     * 显示常驻通知栏
     */
    private void showCzNotify() {
        NotificationChanelUtils.createNotificationChannel(mNotificationManager, NotifyConstant.channelId3, NotifyConstant.channelName3, NotifyConstant.channelImportance3, NotifyConstant.channelDesc3, NotifyConstant.groupId1);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId2);

        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setTicker("常驻通知来了")
                .setContentTitle("常驻测试")
                .setContentText("使用cancel()方法才可以把我去掉哦")
                .setOngoing(true); //true，设置他为一个正在进行的通知。它们通常是用来表示一个后台任务，用户积极
        addPendingIntent(mBuilder, NotifyToActivity.class);
        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId3), mBuilder.build());
    }

    /**
     * 显示通知栏点击跳转到指定Activity
     */
    private void showIntentActivityNotify() {
        NotificationChanelUtils.createNotificationChannel(mNotificationManager, NotifyConstant.channelId4, NotifyConstant.channelName4, NotifyConstant.channelImportance4, NotifyConstant.channelDesc4, NotifyConstant.groupId1);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId4);
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setTicker("跳转到指定Activity")
                .setContentTitle("点击跳转到指定Activity")
                .setContentText("通知栏点击跳转到指定Activity")
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false); //true，设置他为一个正在进行的通知。它们通常是用来表示一个后台任务，用户积极
        addPendingIntent(mBuilder, NotifyToActivity.class);
        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId4), mBuilder.build());

    }

    /**
     * 显示通知栏点击打开Apk
     */
    private void showIntentApkNotify() {
        NotificationChanelUtils.createNotificationChannel(mNotificationManager, NotifyConstant.channelId5, NotifyConstant.channelName5, NotifyConstant.channelImportance5, NotifyConstant.channelDesc5, NotifyConstant.groupId1);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotifyConstant.channelId5);
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("下载完成")
                .setContentText("点击安装")
                .setTicker("下载完成！");
        installIntent(mBuilder);

        mNotificationManager.notify(Integer.parseInt(NotifyConstant.channelId5), mBuilder.build());

    }

    /**
     * 安装intent
     */
    private void installIntent(NotificationCompat.Builder mBuilder) {

        //我们这里需要做的是打开一个安装包
        Intent apkIntent = new Intent();
        apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkIntent.setAction(Intent.ACTION_VIEW);
        //注意：这里的这个APK是放在assets文件夹下，获取路径不能直接读取的，要通过COYP出去在读或者直接读取自己本地的PATH，这边只是做一个跳转APK，实际打不开的
        String apk_path = "file:///android_asset/cs.apk";
        Uri uri = Uri.fromFile(new File(apk_path));
        apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        PendingIntent contextIntent = PendingIntent.getActivity(this, 0, apkIntent, 0);
        mBuilder.setContentIntent(contextIntent);
/**
 Observable.create((Observable.OnSubscribe<String>) subscriber -> {
 try {
 //InputStream inputStream = getClass().getResourceAsStream("/assets/cs.apk");
 InputStream inputStream = getAssets().open("cs.apk");
 String path = new String(FileUtils.InputStreamToByte(inputStream));
 subscriber.onNext(path);
 }catch (Exception e){
 e.printStackTrace();
 }
 }).subscribeOn(Schedulers.io())
 .observeOn(AndroidSchedulers.mainThread())
 .subscribe(new Subscriber<String>() {
@Override public void onCompleted() {
Log.d(TAG, "onCompleted: ");

}

@Override public void onError(Throwable e) {
e.printStackTrace();
}

@Override public void onNext(String path) {
//我们这里需要做的是打开一个安装包
Intent apkIntent = new Intent();
apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
apkIntent.setAction(Intent.ACTION_VIEW);
//注意：这里的这个APK是放在assets文件夹下，获取路径不能直接读取的，要通过COYP出去在读或者直接读取自己本地的PATH，这边只是做一个跳转APK，实际打不开的
//String apk_path = "file:///android_asset/cs.apk";

//String apk_path = "file:///android_asset/cs.apk";
Uri uri = Uri.fromFile(new File(path));
apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
PendingIntent contextIntent = PendingIntent.getActivity(NotificationActivity.this, 0, apkIntent, 0);
mBuilder.setContentIntent(contextIntent);
}
});
 */
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(String notifyId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.deleteNotificationChannel(notifyId);//删除一个特定的通知ID对应的通知
        } else {
            mNotificationManager.cancel(Integer.parseInt(notifyId));
        }
    }

    /**
     * 清楚所有通知
     */
    private void clearAll() {
        mNotificationManager.cancelAll();
    }

    /**
     * 添加PendingIntent
     *
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
