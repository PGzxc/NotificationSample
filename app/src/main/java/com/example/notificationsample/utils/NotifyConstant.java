package com.example.notificationsample.utils;

import android.app.NotificationManager;

/**
 * Notify常量
 */

public class NotifyConstant {

    //Group
    public static String groupId1 = "groupId1";
    public static CharSequence groupName1 = "Group1";

    public static String groupId2 = "groupId2";
    public static CharSequence groupName2 = "Group2";
    //===================================================================
    //显示通知栏
    public static String channelId1 = "1";
    public static String channelName1 = "channelName1";
    public static String channelDesc1 = "显示通知栏";
    public static int channelImportance1 = NotificationManager.IMPORTANCE_MAX;

    //显示大视图风格通知栏
    public static String channelId2 = "2";
    public static String channelName2 = "channelName2";
    public static String channelDesc2 = "显示大视图风格通知栏";
    public static int channelImportance2 = NotificationManager.IMPORTANCE_HIGH;

    //显示常驻通知栏
    public static String channelId3 = "3";
    public static String channelName3 = "channelName3";
    public static String channelDesc3 = "显示常驻通知栏";
    public static int channelImportance3 = NotificationManager.IMPORTANCE_DEFAULT;

    //显示通知栏点击跳转到指定Activity
    public static String channelId4 = "4";
    public static String channelName4 = "channelName4";
    public static String channelDesc4 = "显示通知栏点击跳转到指定Activity";
    public static int channelImportance4 = NotificationManager.IMPORTANCE_LOW;

    //显示通知栏点击打开Apk
    public static String channelId5 = "5";
    public static String channelName5 = "channelName5";
    public static String channelDesc5 = "显示通知栏点击打开Apk";
    public static int channelImportance5 = NotificationManager.IMPORTANCE_LOW;

    //显示自定义通知栏
    public static String channelId6 = "6";
    public static String channelName6 = "channelName6";
    public static String channelDesc6 = "显示自定义通知栏";
    public static int channelImportance6 = NotificationManager.IMPORTANCE_LOW;

    //带按钮的通知栏
    public static String channelId7 = "7";
    public static String channelName7 = "channelName7";
    public static String channelDesc7 = "带按钮的通知栏";
    public static int channelImportance7 = NotificationManager.IMPORTANCE_LOW;

    //带下载进度的通知栏
    public static String channelId8 = "8";
    public static String channelName8 = "channelName8";
    public static String channelDesc8 = "带下载进度的通知栏";
    public static int channelImportance8 = NotificationManager.IMPORTANCE_LOW;
}
