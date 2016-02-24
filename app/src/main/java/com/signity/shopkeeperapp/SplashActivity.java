package com.signity.shopkeeperapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.signity.shopkeeperapp.gcm.GCMClientManager;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.view.LoginScreenActivity;

/**
 * Created by Rajinder on 29/9/15.
 */
public class SplashActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private int SPLASH_TIME_OUT = 1000;
    private GCMClientManager pushClientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


//        pushClientManager = new GCMClientManager(this, Constant.PROJECT_NUMBER);
//
//        String deviceToken = pushClientManager.getRegistrationId(SplashActivity.this);

//        if (deviceToken != null && !deviceToken.isEmpty()) {
//            moveNext();
//        } else {
//            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
//                @Override
//                public void onSuccess(String registrationId, boolean isNewRegistration) {
//                    moveNext();
//                }
//
//                @Override
//                public void onFailure(String ex) {
//                    super.onFailure(ex);
//                }
//            });
//        }

        removeNotificationsFromStatusBar();

        if (Util.checkIntenetConnection(SplashActivity.this)) {
            moveNext();
        } else {
            final DialogHandler dialogHandler = new DialogHandler(SplashActivity.this);
            dialogHandler.setdialogForFinish("Internet", "Please check your Internet Connection.", true);
        }


    }

    private void removeNotificationsFromStatusBar() {

        NotificationManager notifManager = (NotificationManager) SplashActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }


    void moveNext() {
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                String loginCheck = Util.loadPreferenceValue(SplashActivity.this, Constant.LOGIN_CHECK);
                if (loginCheck.equalsIgnoreCase("0")) {
                    Intent intent_home = new Intent(SplashActivity.this,
                            LoginScreenActivity.class);
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
                            LoginScreenActivity.class);
                    startActivity(intent_home);
                    AnimUtil.slideFromRightAnim(SplashActivity.this);
                    finish();
                }

            }

        }, SPLASH_TIME_OUT);
    }

}

