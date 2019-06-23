package com.dsource.idc.jellowintl.utility;


import androidx.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JellowFirebaseMessageServiceTest {
    /*private Context mContext;
    private RemoteMessage remoteMessage;
    private JellowFirebaseMessageService jFms;

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        setRemoteMessage(createRemoteMessage());
        jFms = new JellowFirebaseMessageService();
    }

    private RemoteMessage createRemoteMessage() {
        return new RemoteMessage.Builder("TestRemoteMessage")
            .addData("title", "Jellow update available")
            .addData("packageName", mContext.getPackageName())
            .addData("versionCode", String.valueOf(BuildConfig.VERSION_CODE))
            .addData("app_update", "true")
            .addData("body", "")
            .addData("link", "")
            .build();
    }

    @Test
    public void isRemoteMessageIntendedTest(){
        //assert jFms.isRemoteMessageIntended(getRemoteMessage());
    }

    @Test
    public void onUpdateMessageReceived(){
        RemoteMessage rmsg = new RemoteMessage.Builder("TestRemoteMessage")
                .addData("title", "Jellow update available")
                .addData("packageName", mContext.getPackageName())
                .addData("versionCode", String.valueOf(BuildConfig.VERSION_CODE))
                .addData("app_update", "true")
                .addData("body", "")
                .addData("link", "")
                .build();

        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.dsource.idc.jellowintl"));
        } catch (android.content.ActivityNotFoundException anf) {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.dsource.idc.jellowintl"));
        }
        jFms.onMessageReceived(rmsg);
                //assert jFms.builtNotification().equals(mockNotification(rmsg, intent));
    }

    private Notification mockNotification(RemoteMessage rmsg, Intent intent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(mContext, "jellow_aac");
        builder.setContentTitle(rmsg.getData().get("title"));
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher));
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setContentText(rmsg.getData().get("body"));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(rmsg.getData().get("body")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setSmallIcon(R.drawable.ic_notif_small);
            builder.setColor(mContext.getColor(R.color.colorAccent));
        } else {
            builder.setSmallIcon(R.drawable.ic_launcher);
        }
        return builder.build();
    }

    public RemoteMessage getRemoteMessage() {
        return remoteMessage;
    }

    public void setRemoteMessage(RemoteMessage remoteMessage) {
        this.remoteMessage = remoteMessage;
    }*/
}
