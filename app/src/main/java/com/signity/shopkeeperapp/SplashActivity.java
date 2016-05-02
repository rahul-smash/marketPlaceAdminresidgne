package com.signity.shopkeeperapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.signity.shopkeeperapp.LogInModule.LogInOptionsActivity;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.gcm.GCMClientManager;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.receiver.LocalNotifyReceiver;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.Util;

import java.util.Calendar;

/**
 * Created by Rajinder on 29/9/15.
 */
public class SplashActivity extends Activity {

    PendingIntent sender;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private int SPLASH_TIME_OUT = 1000;
    private GCMClientManager pushClientManager;

    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appDatabase = DbAdapter.getInstance().getDb();

//        sendNotification("Local", "First Notification");

       /* pushClientManager = new GCMClientManager(this, Constant.PROJECT_NUMBER);

        String deviceToken = pushClientManager.getRegistrationId(SplashActivity.this);

        if (deviceToken != null && !deviceToken.isEmpty()) {
            moveNext();
        } else {
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {
                    moveNext();
                }

                @Override
                public void onFailure(String ex) {
                    super.onFailure(ex);
                }
            });
        }*/

        removeNotificationsFromStatusBar();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String storeId = Util.loadPreferenceValue(SplashActivity.this, Constant.STORE_ID);
                if (storeId != null && !(storeId.isEmpty())) {
                    moveNext();
                } else {
                    if (Util.checkIntenetConnection(SplashActivity.this)) {
                        moveNext();
                    } else {
                        final DialogHandler dialogHandler = new DialogHandler(SplashActivity.this);
                        dialogHandler.setdialogForFinish("Internet", "Please check your Internet Connection.", true);
                    }
                }
            }
        }, 2000);

    }

    private void removeNotificationsFromStatusBar() {

        NotificationManager notifManager = (NotificationManager) SplashActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }


    void moveNext() {

        String loginCheck = Util.loadPreferenceValue(SplashActivity.this, Constant.LOGIN_CHECK);
        if (loginCheck.equalsIgnoreCase("0")) {
            Intent intent_home = new Intent(SplashActivity.this,
                    LogInOptionsActivity.class);
            intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_home);
            AnimUtil.slideFromRightAnim(SplashActivity.this);
            finish();
        } else if (loginCheck.equalsIgnoreCase("1")) {
            Intent intent_home = new Intent(SplashActivity.this,
                    MainActivity.class);
            startActivity(intent_home);
            AnimUtil.slideFromRightAnim(SplashActivity.this);
            finish();
        } else {
            Intent intent_home = new Intent(SplashActivity.this,
                    LogInOptionsActivity.class);
            intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_home);
            AnimUtil.slideFromRightAnim(SplashActivity.this);
            finish();
        }


    }


    private void sendNotification(String title, String message) {


        final int currentMode;
        boolean modeChange = false;
        final AudioManager am;
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentMode = am.getRingerMode();

        if (currentMode != AudioManager.RINGER_MODE_NORMAL) {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            modeChange = true;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        int icon = R.mipmap.ic_launcher;


        Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.signity.valueappz/raw/notificationrecieved");
//        Uri defaultSoundUri = Uri.parse("android.resource://com.signity.valueappz/" + R.raw.notificationrecieved);
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
        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

        if (modeChange) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    am.setRingerMode(currentMode);
                }
            }, 2000);
        }

        setupLocalNotification();

    }

    private void setupLocalNotification() {
        Calendar cal = Calendar.getInstance();
        // add 5 minutes to the calendar object
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(this, LocalNotifyReceiver.class);
        sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // In reality, you would want to have a static variable for the request code instead of 192837

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10000, sender);

    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.cancel(sender);
//    }
}

