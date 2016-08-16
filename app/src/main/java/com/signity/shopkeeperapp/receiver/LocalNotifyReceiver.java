package com.signity.shopkeeperapp.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.SplashActivity;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rajesh on 7/4/16.
 */
public class LocalNotifyReceiver extends BroadcastReceiver {
    int dueOrderNotificaionCount;
    PrefManager prefManager;

    int dueOrderCount = 0;
    int activeOrderCount = 0;

    int notificationId;

    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            mContext = context;
            Bundle bundle = intent.getExtras();

            String type = intent.getStringExtra("type");

            if (type.equalsIgnoreCase(Constant.LOCAL_TYPE_ONE)) {
                notificationId = Constant.LOCAL_NOTIFY_FOR_DUE_ORDER;
                prefManager = new PrefManager(context);
                dueOrderNotificaionCount = prefManager.getDueOrderLocalNotiCount();
                if (dueOrderNotificaionCount < 2) {
                    checkDueOrderStatus(context);
                } else {
                    cancelAlaramManagerTask(context);
                }
            } else if (type.equalsIgnoreCase(Constant.LOCAL_TYPE_TWO)) {
                notificationId = Constant.LOCAL_NOTIFY_FOR_8_PM;
                checkDueOrderStatus(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkDueOrderStatus(final Context context) {


        if (!Util.checkIntenetConnection(context)) {
            return;
        }

        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "pending");
        param.put("api_key", "");


        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getOrdersModel, Response response) {

                OrdersModel ordersModel = getOrdersModel.getData();
                if (ordersModel.getOrders() != null && ordersModel.getOrders().size() != 0) {
                    dueOrderCount = ordersModel.getOrders().size();
                    checkActiveOrderStatus(context);
                } else {
                    checkActiveOrderStatus(context);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error", error.getMessage());
            }
        });

    }

    private void checkActiveOrderStatus(Context context) {
        if (!Util.checkIntenetConnection(context)) {
            return;
        }


        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "active");
        param.put("api_key", "");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getOrdersModel, Response response) {

                OrdersModel ordersModel = getOrdersModel.getData();
                if (ordersModel.getOrders() != null && ordersModel.getOrders().size() != 0) {
                    activeOrderCount = ordersModel.getOrders().size();
                    setUpNotification();
                } else {
                    setUpNotification();
                }

            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("Error", error.getMessage());

            }
        });

    }

    private void setUpNotification() {
        String message = "";
        if (dueOrderCount != 0 & activeOrderCount != 0) {
            message = "You have " + (dueOrderCount > 1 ? dueOrderCount + " orders" : dueOrderCount + " order") +
                    " due and " + (activeOrderCount > 1 ? activeOrderCount + " orders" : activeOrderCount + " order") + " active. Kindly process the same.";
        } else if (dueOrderCount != 0) {
            message = "You have " + (dueOrderCount > 1 ? dueOrderCount + " orders" : dueOrderCount + " order") + " due. " +
                    "Kindly process the same.";
        } else if (activeOrderCount != 0) {
            message = "You have " + (activeOrderCount > 1 ? activeOrderCount + " orders" : activeOrderCount + " order") + " active. " +
                    "Kindly process the same.";
        } else {
            return;
        }

        if (notificationId == Constant.LOCAL_NOTIFY_FOR_DUE_ORDER) {
            int currentCount = prefManager.getDueOrderLocalNotiCount();
            prefManager.setDueOrderLocalNotiCount(currentCount++);
        }

        sendNotification(mContext, "Order Notification", message, notificationId);
    }

    private void cancelAlaramManagerTask(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        Intent intents = new Intent(context, LocalNotifyReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(context, Constant.LOCAL_NOTIFY_FOR_DUE_ORDER, intents, 0);
        am.cancel(pendingIntent);
        prefManager.setDueOrderLocalNotiCount(0);
    }

    private void sendNotification(Context context, String title, String message, int notificationId) {

        if (notificationId == Constant.LOCAL_NOTIFY_FOR_DUE_ORDER) {
            int currentCount = prefManager.getDueOrderLocalNotiCount();
            prefManager.setDueOrderLocalNotiCount(currentCount++);
        }

        Intent intent = null;
        if (prefManager.isApplicationVisible()) {
            intent = new Intent();
        } else {
            intent = new Intent(context, SplashActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        int icon = R.mipmap.ic_launcher;
        String uriString = prefManager.getAppNotificationUri();
        Uri defaultSoundUri = Uri.parse(uriString);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setTicker(title)
                .setSmallIcon(icon)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId/* ID of notification */, notificationBuilder.build());
    }
}


//        final int currentMode;
//        boolean modeChange = false;
//        final AudioManager am;
//        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        currentMode = am.getRingerMode();
//        if (currentMode != AudioManager.RINGER_MODE_NORMAL) {
//            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//            modeChange = true;
//        }


//if (modeChange) {
//        new Handler().postDelayed(new Runnable() {
//@Override
//public void run() {
//        am.setRingerMode(currentMode);
//        }
//        }, 2000);
//        }