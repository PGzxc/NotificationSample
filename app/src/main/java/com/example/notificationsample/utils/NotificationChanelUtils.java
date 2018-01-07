package com.example.notificationsample.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

/**
 * Created by admin on 2018/1/7.
 */

public class NotificationChanelUtils {

    public static void createNotificationChannel(NotificationManager mNotificationManager, String id, String name, int importance, String desc, String groupId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mNotificationManager.getNotificationChannel(id) != null) return;
            NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400});

            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationChannel.setShowBadge(true);
            notificationChannel.setBypassDnd(true);

            notificationChannel.setDescription(desc);
            notificationChannel.setGroup(groupId);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
