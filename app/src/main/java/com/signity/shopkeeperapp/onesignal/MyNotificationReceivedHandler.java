package com.signity.shopkeeperapp.onesignal;

import android.text.TextUtils;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import com.signity.shopkeeperapp.model.notification.Notifications;
import com.signity.shopkeeperapp.util.Util;

import org.json.JSONObject;

/**
 * MyNotificationReceivedHandler
 *
 * @blame Android Team
 */
public class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    private static final String TAG = MyNotificationReceivedHandler.class.getSimpleName();

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        Log.d(TAG, "notificationOpened: " + data);

        Notifications notification1 = new Notifications();
        notification1.setTitle(notification.payload.title);
        notification1.setMessage(notification.payload.body);
        notification1.setCreatedAt(Util.getCurrentDateTime());

        if (data != null) {
            String action = data.optString("action", "");

            if (TextUtils.isEmpty(action)) {
                notification1.setAction("Announcement");
            } else {
                notification1.setAction(action);
            }
        }else {
            notification1.setAction("Announcement");
        }

        if (data != null) {
            //While sending a Push notification from OneSignal dashboard
            // you can send an addtional data named "customkey" and retrieve the value of it and do necessary operation
            String action = data.optString("action", null);
            if (action != null) {
                Log.d(TAG, "notificationOpened:--customKey-- " + action);
                Log.i("OneSignalExample", "customkey set with value: " + action);
            }
        }
    }
}
