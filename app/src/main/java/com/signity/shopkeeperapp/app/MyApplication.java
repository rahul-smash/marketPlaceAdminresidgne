package com.signity.shopkeeperapp.app;

import android.app.Application;

import com.signity.shopkeeperapp.network.NetworkAdaper;


/**
 * Created by Rajinder on 5/10/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSingletons();
    }

    protected void initSingletons() {
        NetworkAdaper.initInstance(getApplicationContext());
        DataAdapter.initInstance();
    }

}
