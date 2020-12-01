package com.signity.shopkeeperapp.onesignal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import com.onesignal.OSInAppMessageAction;
import com.onesignal.OneSignal;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OneSignalInAppMessaging implements OneSignal.InAppMessageClickHandler {

    @Override
    public void inAppMessageClicked(OSInAppMessageAction result) {

        if (result == null || result.clickName == null) {
            return;
        }

        Class myClass = getActivityClass(getApplicationContext(), result.clickName);

        if (myClass == null) {
            return;
        }

        if (AppPreference.getInstance().isLoggedIn() == Constant.Mode.NOT_LOGGED_IN) {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), myClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    private Class getActivityClass(Context context, String namee) {
        PackageManager pManager = context.getPackageManager();
        String packageName = context.getApplicationContext().getPackageName();

        Class myClass = null;

        try {
            ActivityInfo[] list = pManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities;
            for (ActivityInfo activityInfo : list) {
                if (activityInfo.name.contains(namee)) {
                    try {
                        myClass = Class.forName(activityInfo.name);

                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return myClass;
    }

}
