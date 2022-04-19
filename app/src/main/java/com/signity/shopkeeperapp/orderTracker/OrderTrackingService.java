package com.signity.shopkeeperapp.orderTracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.model.orderCount.OrderCountResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderTrackingService extends Service {
    public static CountDownTimer timer;
    static MediaPlayer mediaPlayer;
    static Vibrator vibrator;
    static int notificationId = 13;
    static String channelId = "Channel_002";
    static String channelName = "Background Service Channel";

    static void initPlayer(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.order_recieved);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(10, 10);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    static void startPlayer(Context context) {
        stopPlayer();
        initPlayer(context);
        mediaPlayer.start();
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{300, 300, 300, 1000}, VibrationEffect.EFFECT_DOUBLE_CLICK));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    static void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
        mediaPlayer = null;
        vibrator = null;
    }

    private static Notification getNotification(Context context) {
        Intent notificationIntent = new Intent(context, DashboardActivity.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(context,
                    2,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(context,
                    2,
                    notificationIntent, 0);
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(context.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.notificationicon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notificationicon))
                .setContentIntent(pendingIntent)
                .setContentText(context.getString(R.string.notification_msg))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(context.getString(R.string.notification_msg)))
                .build();

        return notification;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void updateNotification(OrderCountResponse orderCountResponse, Context context) {
        NotificationManager manager = context.getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(serviceChannel);
        }

        manager.notify(notificationId, prepareForegroundNotification(orderCountResponse, context));
        startPlayer(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static public void updateBasicNotification(Context context) {
        NotificationManager manager = context.getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            );
            manager.createNotificationChannel(serviceChannel);
        }
        manager.notify(notificationId, getNotification(context));
    }

    private static Notification prepareForegroundNotification(OrderCountResponse orderCountResponse, Context context) {

        Intent notificationIntent = new Intent(context, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                2,
                notificationIntent, 0);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent dismissIntent = new Intent(context, OrderTrackerBroadcast.class);
        dismissIntent.setAction(OrderTrackerBroadcast.INTENT_ACTION_DISMISS_SOUND);
        PendingIntent dismissPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dismissPendingIntent =
                    PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            dismissPendingIntent =
                    PendingIntent.getBroadcast(context, 0, dismissIntent, 0);
        }

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("Order")
                .setSmallIcon(R.drawable.notificationicon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notificationicon))
                .setContentIntent(pendingIntent).
                        setVibrate(new long[]{1000, 1000})
                .setAutoCancel(false)
                .setContentText(orderCountResponse.getMessage())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(orderCountResponse.getMessage()))
                .addAction(R.drawable.notificationicon, "Dismiss", dismissPendingIntent)
                .build();

        return notification;
    }

    static void hitRunnerOrderCountService(final Context context) {

        NetworkAdaper.getInstance(context).getNetworkServices().runnerOrderCount(new Callback<OrderCountResponse>() {
            @Override
            public void success(OrderCountResponse orderCountResponse, Response response) {
                Log.e("Reminder", "success: " + orderCountResponse.getMessage());
                if (orderCountResponse != null && orderCountResponse.getSuccess() && orderCountResponse.getOrderCount() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        updateNotification(orderCountResponse, context);
                    }
//                    Intent serviceIntent = new Intent(context, OrderTrackingService.class);
//                    serviceIntent.putExtra(OrderTrackerBroadcast.INTENT_SERVICE_DATA, orderCountResponse);
//                    //start service
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        context.startForegroundService(serviceIntent);
//                    } else {
//                        context.startService(serviceIntent);
//                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void setAlarm(Context context) {
        startCountDownTimer(context);
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, OrderServiceBroadcast.class);
//        i.setAction(OrderTrackerBroadcast.INTENT_ACTION_START_SOUND);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 * 1, pi); // Millisec * Second * Minute
//        Log.e("Reminder", "On setAlarm ReminderStatus is" + AppPreference.getInstance(context).getReminderStatus());
    }

    public static void startCountDownTimer(final Context context) {

        timer = new CountDownTimer(40000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("Reminder", "onTick: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                hitRunnerOrderCountService(context);
                startCountDownTimer(context);
            }


        }.start();
    }

    public static void cancelCountDownTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static void cancelAlarm(Context context) {
//        Intent intent = new Intent(context, OrderServiceBroadcast.class);
//        intent.setAction(OrderTrackerBroadcast.INTENT_ACTION_START_SOUND);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//        Log.e("Reminder", "On cancelAlarm ReminderStatus is" + AppPreference.getInstance(context).getReminderStatus());
        cancelCountDownTimer();
        stopPlayer();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager manager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            );
            manager.createNotificationChannel(serviceChannel);
        }
        startForeground(notificationId, getNotification(getBaseContext()));
        boolean isReminderActive = AppPreference.getInstance().getReminderStatus();
        if (isReminderActive) {
            Log.e("Reminder", "onStartCommand: ");
            sendBroadcast(new Intent(getBaseContext(), OrderServiceBroadcast.class)
                    .setAction(OrderTrackerBroadcast.INTENT_ACTION_START_ALARM));
        }
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayer();
//        unregisterReceiver();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class OrderServiceBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Reminder", "On OrderServiceBroadcast onReceive ReminderStatus is " + intent.getAction());
            if (intent.getAction().equalsIgnoreCase(OrderTrackerBroadcast.INTENT_ACTION_DISMISS_SOUND)) {
                stopPlayer();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    updateBasicNotification(context);
                }
            } else if (intent.getAction().equalsIgnoreCase(OrderTrackerBroadcast.INTENT_ACTION_START_SOUND)) {
                hitRunnerOrderCountService(context);
            } else if (intent.getAction().equalsIgnoreCase(OrderTrackerBroadcast.INTENT_ACTION_START_ALARM)) {
                setAlarm(context);
            } else if (intent.getAction().equalsIgnoreCase(OrderTrackerBroadcast.INTENT_ACTION_CANCEL_ALARM)) {
                cancelAlarm(context);
            }
        }
    }
}