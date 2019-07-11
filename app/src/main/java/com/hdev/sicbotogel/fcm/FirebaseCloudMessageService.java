package com.hdev.sicbotogel.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hdev.sicbotogel.database.helper.NotificationHelper;
import com.hdev.sicbotogel.database.model.Notifications;
import com.hdev.sicbotogel.utils.NotificationUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FirebaseCloudMessageService extends FirebaseMessagingService {
    private static final String TAG = FirebaseCloudMessageService.class.getSimpleName();
    private NotificationHelper notificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper = new NotificationHelper(this);
        notificationHelper.open();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken : " + s);
    }

    public static void subscribe(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        Log.d(TAG, "subscribed : " + topic);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            handleDebug("NOTIFICATION", remoteMessage);
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendBroadcast(title);
            NotificationUtils.show(this, title, body);
            saveNotification(title, body);

        } else {
            handleDebug("DATA", remoteMessage);
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            sendBroadcast(remoteMessage.getData().get("title"));
            NotificationUtils.show(this, title, body);
            saveNotification(title, body);
        }
    }

    /*
    save data to sqlite database
     */
    private void saveNotification(String title, String body) {
        Log.d("DEBUG", "insert");
        String date = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS", Locale.getDefault()).format(new Date().getTime());
        Notifications notifications = new Notifications();
        notifications.setTitle(title);
        notifications.setBody(body);
        notifications.setDate(date);
        notifications.setStatus_read("unread");
        notificationHelper.insert(notifications);
    }

    /*
    send broadcast
     */
    private void sendBroadcast(String title) {
        Intent intent = new Intent("NOTIFICATION_VALUE");
        intent.putExtra("TITLE", title);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /*
    Handle debug
     */
    private void handleDebug(String type, RemoteMessage remoteMessage) {
        Log.d("DEBUG", type);
        if (type.equals("DATA")) {
            Log.d("DEBUG", remoteMessage.getData().get("title"));
            Log.d("DEBUG", remoteMessage.getData().get("body"));
        } else {
            Log.d("DEBUG", remoteMessage.getNotification().getTitle());
            Log.d("DEBUG", remoteMessage.getNotification().getBody());
        }
    }
}
