package com.signity.shopkeeperapp.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.PrefManager;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            prefManager = new PrefManager(context);

            dueOrderNotificaionCount = prefManager.getDueOrderLocalNotiCount();

            if (dueOrderNotificaionCount < 2) {
                checkDueOrderStatus(context);
            } else {
               cancelAlaramManagerTask(context);
            }


        } catch (Exception e) {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void checkDueOrderStatus(final Context context) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "pending");
        param.put("api_key", "");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getOrdersModel, Response response) {

                OrdersModel ordersModel = getOrdersModel.getData();
                if (ordersModel.getOrders() != null && ordersModel.getOrders().size() != 0) {
                    dueOrderNotificaionCount = dueOrderNotificaionCount + 1;
                    prefManager.setDueOrderLocalNotiCount(dueOrderNotificaionCount);
                    sendNotification(context,
                            "Order Confirmation", "You have some due order to process. Please proceed to deliver");
                } else {
                    cancelAlaramManagerTask(context);
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void cancelAlaramManagerTask(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        Intent intents = new Intent(context, LocalNotifyReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(context, Constant.LOCAL_NOTIFY_FOR_DUE_ORDER, intents, 0);
        am.cancel(pendingIntent);
    }


    private void sendNotification(Context context, String title, String message) {


        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        int icon = R.mipmap.ic_launcher;
        Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.signity.valueappz/raw/notificationrecieved");
//        Uri defaultSoundUri = Uri.parse("android.resource://com.signity.valueappz/" + R.raw.notificationrecieved);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setTicker(title)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());


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