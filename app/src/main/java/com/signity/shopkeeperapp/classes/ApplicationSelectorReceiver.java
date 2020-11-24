package com.signity.shopkeeperapp.classes;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class ApplicationSelectorReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null)
            for (String key : intent.getExtras().keySet()) {
                try {
                    ComponentName componentInfo = (ComponentName) intent.getExtras().get(key);
                    PackageManager packageManager = context.getPackageManager();

                    if (componentInfo == null) {
                        return;
                    }

                    String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentInfo.getPackageName(), PackageManager.GET_META_DATA));
                    Log.i("Selected Application", appName.toLowerCase());

//                AppPreferenceHelper.getInstance().setShareIntentMedium(appName.toLowerCase());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}