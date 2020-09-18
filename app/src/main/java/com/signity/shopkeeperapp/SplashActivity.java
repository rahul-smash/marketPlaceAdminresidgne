package com.signity.shopkeeperapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.signity.shopkeeperapp.LogInModule.LogInOptionsActivity;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajinder on 29/9/15.
 */
public class SplashActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    PendingIntent sender;
    AppDatabase appDatabase;
    PrefManager prefManager;
    String storeId;
    String userId;
    boolean isInternetConnected;
    NetworkAdaper netWorkAdapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private int SPLASH_TIME_OUT = 1000;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        netWorkAdapter = NetworkAdaper.getInstance();
        appDatabase = DbAdapter.getInstance().getDb();
        prefManager = new PrefManager(this);
        storeId = prefManager.getSharedValue(Constant.STORE_ID);
        userId = prefManager.getSharedValue(Constant.STAFF_ADMIN_ID);
        isInternetConnected = Util.checkIntenetConnection(this);
        removeNotificationsFromStatusBar();

        startSplashModule();


    }


    private void startSplashModule() {

        Intent intent_home = new Intent(SplashActivity.this,
                DashboardActivity.class);
        startActivity(intent_home);
        AnimUtil.slideFromRightAnim(SplashActivity.this);
        finish();

      /*  if (!storeId.isEmpty() && !userId.isEmpty()) {
            Log.i("startSplashModule_1", "startSplashModule");
            if (isInternetConnected) {
                Log.i("startSplashModule_2", "startSplashModule");

                checkForStaffValidationProcess();
            } else {
                Log.i("startSplashModule_3", "startSplashModule");

                openHomeScreen();
            }
        } else {
            if (isInternetConnected) {
                Log.i("startSplashModule_4", "startSplashModule");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("startSplashModule_5", "startSplashModule");


                        openLoginScreen();
                    }
                }, 2000);
            } else {
                Log.i("startSplashModule_6", "startSplashModule");

                openAlertForNoInternet();
            }
        }
*/
    }


    private void removeNotificationsFromStatusBar() {
        NotificationManager notifManager = (NotificationManager) SplashActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }


    public void checkForStaffValidationProcess() {

        ProgressDialogUtil.showProgressDialog(context);

        HashMap<String, String> map = new HashMap<>();
        map.put("staff_id", userId);

        NetworkAdaper.getInstance().getNetworkServices().validStaff(map, new Callback<MobResponseLogin>() {
            @Override
            public void success(MobResponseLogin mobResponseLogin, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (mobResponseLogin.getSuccess()) {
                    openHomeScreen();
                } else {
                    clearLoginCredIf();
                    openLoginScreen();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                Log.e("Splash", "" + error.getMessage());
                openErrorAlert("Error", "Server not working, please try later");
            }
        });


    }


    public void openLoginScreen() {
        Intent intent_home = new Intent(SplashActivity.this,
                LogInOptionsActivity.class);
        intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_home);
        AnimUtil.slideFromRightAnim(SplashActivity.this);
        finish();
    }

    public void openHomeScreen() {
        Intent intent_home = new Intent(SplashActivity.this,
                MainActivity.class);
        startActivity(intent_home);
        AnimUtil.slideFromRightAnim(SplashActivity.this);
        finish();
    }

    private void clearLoginCredIf() {
        prefManager.clearSharedValue(Constant.STORE_ID);
        prefManager.clearSharedValue(Constant.STAFF_ADMIN_ID);
    }

    /*Intent check for very first time*/
    private void openAlertForNoInternet() {

        final DialogHandler dialogHandler = new DialogHandler(context);
        dialogHandler.setDialog("No Internet", "Please check your internet connection");
        dialogHandler.setOnPositiveButtonClickListener("Ok", new DialogHandler.OnPostiveButtonClick() {
            @Override
            public void Onclick() {
                startSplashModule();
                dialogHandler.dismiss();
            }
        });
        dialogHandler.setOnNegativeButtonClickListener("Cancel", new DialogHandler.OnNegativeButtonClick() {
            @Override
            public void Onclick() {
                dialogHandler.dismiss();
                finish();
            }
        });

    }

    private void openErrorAlert(String title, String message) {

        final DialogHandler dialogHandler = new DialogHandler(context);
        dialogHandler.setDialog(title, message);
        dialogHandler.setOnNegativeButtonClickListener("Ok", new DialogHandler.OnNegativeButtonClick() {
            @Override
            public void Onclick() {
                dialogHandler.dismiss();
                finish();
            }
        });

    }


}


//    private void sendTestNotification() {
//
//        int icon = R.mipmap.ic_launcher;
//
//
////        Uri defaultSoundUri = Uri.parse(prefManager.getAppNotificationUri());
//        String uriString = prefManager.getAppNotificationUri();
//        Uri defaultSoundUri = Uri.parse(uriString);
//        Intent intent = null;
//        if (prefManager.isApplicationVisible()) {
//            intent = new Intent();
//        } else {
//            intent = new Intent(this, SplashActivity.class);
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
////        Uri defaultSoundUri = Uri.parse("android.resource://com.signity.valueappz/" + R.raw.notificationrecieved);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
//                .setContentTitle("Test")
//                .setContentText("Test Message")
//                .setTicker("Test Ticker")
//                .setSmallIcon(icon)
//                .setSound(defaultSoundUri)
//                .setAutoCancel(true)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("Tesing custom Notification"))
//                .setContentIntent(pendingIntent);
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(1/* ID of notification */, notificationBuilder.build());
//    }

