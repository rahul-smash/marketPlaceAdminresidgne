package com.signity.shopkeeperapp.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onesignal.OneSignal;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.onesignal.MyNotificationOpenedHandler;
import com.signity.shopkeeperapp.onesignal.MyNotificationReceivedHandler;
import com.signity.shopkeeperapp.onesignal.OneSignalInAppMessaging;
//import com.signity.shopkeeperapp.twilio.BasicChatClient;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.signity.shopkeeperapp.util.Util;

/**
 * Created by Rajinder on 5/10/15.
 */
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static MyApplication instance;
    PrefManager prefManager;
//    private BasicChatClient basicChatClient;

    public static MyApplication getInstance() {
        return instance;
    }

//    public BasicChatClient getBasicChatClient() {
//        return basicChatClient;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        basicChatClient = new BasicChatClient(this);
        FirebaseApp.initializeApp(this);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        prefManager = new PrefManager(this);
        initSingletons();
        saveDeviceToken();
        registerActivityLifecycleCallbacks(this);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .setInAppMessageClickHandler(new OneSignalInAppMessaging())
                .init();

    }

    protected void initSingletons() {
        AppPreference.createInstance(getApplicationContext());
        NetworkAdaper.initInstance(getApplicationContext());
        DataAdapter.initInstance();
        DbAdapter.initInstance(this);
    }

    @Override
    public void onActivityCreated(Activity arg0, Bundle arg1) {
//        Log.e("", "onActivityCreated");

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        Log.e("", "onActivityDestroyed ");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        prefManager.storeBoolean("applicationOnPause", true);
        Log.e("", "onActivityPaused " + activity.getClass());

        prefManager.setApplicationVisibleState(false);
//        Log.e("", "onActivityPaused " + activity.getClass());

    }

    @Override
    public void onActivityResumed(Activity activity) {
        prefManager.storeBoolean("applicationOnPause", false);
        Log.e("", "onActivityResumed " + activity.getClass());
        prefManager.setApplicationVisibleState(true);
//        Log.e("", "onActivityResumed " + activity.getClass());

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        Log.e("", "onActivitySaveInstanceState");

    }

    @Override
    public void onActivityStarted(Activity activity) {
//        Log.e("", "onActivityStarted");

    }

    @Override
    public void onActivityStopped(Activity activity) {
//        Log.e("", "onActivityStopped");

    }

    private void saveDeviceToken() {
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    AppPreference.getInstance().setDeviceToken(instanceIdResult.getToken());
                    Log.d("TAG", "onSuccess: " + instanceIdResult.getToken());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
