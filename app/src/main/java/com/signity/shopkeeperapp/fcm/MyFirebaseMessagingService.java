package com.signity.shopkeeperapp.fcm;

/**
 * Created by root on 28/11/16.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.signity.shopkeeperapp.Notifications.NotificationsDetailActivity;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.SplashActivity;
import com.signity.shopkeeperapp.app.DataAdapter;
import com.signity.shopkeeperapp.util.PrefManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "MyFirebaseMsgService";
    String title, message;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        try {
            message = remoteMessage.getData().get("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            title = remoteMessage.getData().get("title");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sendNotification(title, message);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    // [END receive_message]


    /*private void sendNotification(String title, String message) {
        PrefManager prefManager=new PrefManager(this);
        Intent intent=null;
        PendingIntent pendingIntent=null;
        DataAdapter.getInstance().setNotificationMessage(message);
        if(prefManager.getBoolean("applicationOnPause")){
            intent = new Intent(this, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                    PendingIntent.FLAG_ONE_SHOT);


        }else {
            intent = new Intent(this, NotificationsDetailActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }


        int icon = R.drawable.notifications;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setTicker(title)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID *//* ID of notification *//*, notificationBuilder.build());
    }*/

    private void sendNotification(String title, String message) throws Exception {
        PrefManager prefManager = new PrefManager(this);
        Intent intent = null;
        PendingIntent pendingIntent = null;
        DataAdapter.getInstance().setNotificationMessage(message);
        String DEFAULCHANNEL = "default_channel_id";
        if (prefManager.getBoolean("applicationOnPause")) {
            intent = new Intent(this, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);


        } else {
            intent = new Intent(this, NotificationsDetailActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final int NOTIFY_ID = 1002;
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationCompat.Builder builder;
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "my_package_channel";
            String description = "my_package_first_channel"; // The user-visible description of the channel.
            NotificationChannel mChannel = notifManager.getNotificationChannel(DEFAULCHANNEL);
            if (mChannel == null) {
                mChannel = new NotificationChannel(DEFAULCHANNEL, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
        }
        builder = new NotificationCompat.Builder(this, DEFAULCHANNEL);
        builder.setContentTitle(title)  // required
                .setContentText(message)  // required
                .setSmallIcon(R.drawable.ic_launcher) // required
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.ic_launcher))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setTicker(getResources().getString(R.string.app_name));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notificationOreo = builder.build();
        notificationManager.notify(NOTIFY_ID, notificationOreo);
    }

}
