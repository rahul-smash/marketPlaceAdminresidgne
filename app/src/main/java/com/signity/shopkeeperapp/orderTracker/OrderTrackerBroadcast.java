package com.signity.shopkeeperapp.orderTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

public class OrderTrackerBroadcast extends BroadcastReceiver {
    static public String INTENT_ACTION_START_SOUND = "Intent.start_current_sound";
    static public String INTENT_ACTION_DISMISS_SOUND = "Intent.dismiss_current_sound";
    static public String INTENT_ACTION_STOP_SERVICE = "Intent.stop_current_service";
    static public String INTENT_ACTION_START_ALARM = "Intent.start_alarm_service";
    static public String INTENT_ACTION_CANCEL_ALARM = "Intent.cancel_alarm_service";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Reminder", "On onReceive ReminderStatus is " + intent.getAction());

        if (intent.getAction().equalsIgnoreCase(INTENT_ACTION_DISMISS_SOUND)) {
            if (Util.isMyServiceRunning(OrderTrackingService.class, context)) {
                context.sendBroadcast(new Intent(context, OrderTrackingService.OrderServiceBroadcast.class)
                        .setAction(OrderTrackerBroadcast.INTENT_ACTION_DISMISS_SOUND));
            }
        } else if (intent.getAction().equalsIgnoreCase(INTENT_ACTION_START_ALARM)) {
            startService(context);
        } else if (intent.getAction().equalsIgnoreCase(INTENT_ACTION_CANCEL_ALARM)) {
            if (Util.isMyServiceRunning(OrderTrackingService.class, context)) {
                context.sendBroadcast(new Intent(context, OrderTrackingService.OrderServiceBroadcast.class)
                        .setAction(OrderTrackerBroadcast.INTENT_ACTION_CANCEL_ALARM));
            }
        } else if (intent.getAction().equalsIgnoreCase(INTENT_ACTION_STOP_SERVICE)) {
            if (Util.isMyServiceRunning(OrderTrackingService.class, context)) {
                context.sendBroadcast(new Intent(context, OrderTrackingService.OrderServiceBroadcast.class)
                        .setAction(OrderTrackerBroadcast.INTENT_ACTION_CANCEL_ALARM));
                context.stopService(new Intent(context, OrderTrackingService.class));
            }
        } else if (intent.getAction().equalsIgnoreCase(
                Intent.ACTION_BOOT_COMPLETED)) {
            boolean isReminderActive = AppPreference.getInstance(context).getReminderStatus();
            if (isReminderActive)
                startService(context);
        }

    }

    void startService(Context context) {
        AppPreference.getInstance(context).saveReminderStatusFirstTime(true);
        AppPreference.getInstance(context).saveReminderStatus(true);
        if (!Util.isMyServiceRunning(OrderTrackingService.class, context)) {
            Intent serviceIntent = new Intent(context, OrderTrackingService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }
    }

}
