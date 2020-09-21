package com.signity.shopkeeperapp.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.prefs.AppPreference;


/**
 * Created by Rajinder on 5/10/15.
 */
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {


    PrefManager prefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        prefManager = new PrefManager(this);
        initSingletons();
        registerActivityLifecycleCallbacks(this);
    }

    protected void initSingletons() {
        NetworkAdaper.initInstance(getApplicationContext());
        AppPreference.createInstance(getApplicationContext());
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


}
