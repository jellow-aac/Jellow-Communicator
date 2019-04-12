package com.dsource.idc.jellowintl.utility;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.UserRegistrationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class JellowFirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage);
    }

    public void showNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().get("packageName").equals(getPackageName()) &&
                remoteMessage.getData().get("versionCode").equals(String.valueOf(BuildConfig.VERSION_CODE))) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "jellow_aac");
            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            Intent intent = null;
            //If remote message has "app_update"
            if (remoteMessage.getData().get("app_update").equals("true")) {
                try {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.dsource.idc.jellowintl"));
                } catch (android.content.ActivityNotFoundException anf) {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.dsource.idc.jellowintl"));
                }
            } else {
                if (!remoteMessage.getData().get("link").isEmpty()) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(remoteMessage.getData().get("link")));
                } else {
                    intent = new Intent(this, UserRegistrationActivity.class);
                }
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            builder.setContentText(remoteMessage.getData().get("body"));
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.setSmallIcon(R.drawable.ic_notif_small);
                builder.setColor(getColor(R.color.colorAccent));
            } else {
                builder.setSmallIcon(R.drawable.ic_launcher);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(new Random().nextInt(), builder.build());
        }
    }

}