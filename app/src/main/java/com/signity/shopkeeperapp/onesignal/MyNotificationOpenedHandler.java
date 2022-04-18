package com.signity.shopkeeperapp.onesignal;

import android.content.Intent;

import com.onesignal.OSNotificationAction;
//import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * MyNotificationOpenedHandler
 *
 * @blame Android Team
 */
public class MyNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    private static final String TAG = MyNotificationOpenedHandler.class.getSimpleName();
//
//    @Override
//    public void notificationOpened(OSNotificationOpenResult result) {
//        OSNotificationAction.ActionType actionType = result.action.type;
//        JSONObject data = result.notification.payload.additionalData;
//
//        if (AppPreference.getInstance().isLoggedIn() == Constant.Mode.NOT_LOGGED_IN) {
//            return;
//        }
//
//        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//        if (data != null) {
//            String action = data.optString("action", null);
//            intent.putExtra("action", action);
//        }
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
//    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        OSNotificationAction.ActionType actionType = result.getAction().getType();
        JSONObject data = result.getNotification().getAdditionalData();

        if (AppPreference.getInstance().isLoggedIn() == Constant.Mode.NOT_LOGGED_IN) {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        if (data != null) {
            String action = data.optString("action", null);
            intent.putExtra("action", action);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}

