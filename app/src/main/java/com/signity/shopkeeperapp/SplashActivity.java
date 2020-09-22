package com.signity.shopkeeperapp;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.login.MobileLoginActivity;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajinder on 29/9/15.
 * Updated by ketan on 16/09/20.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, SPLASH_TIME_OUT);
    }

    private void init() {

        if (!Util.checkIntenetConnection(this)) {
            internetDialog();
        }

        if (AppPreference.getInstance().isLoggedIn() == Constant.Mode.LOGGED_IN) {
            openHomeScreen();
        } else {
            openMobileLogin();
        }

    }

    private void checkForStaffValidationProcess() {
        ProgressDialogUtil.showProgressDialog(this);

        HashMap<String, String> map = new HashMap<>();
        map.put("staff_id", "userId");

        NetworkAdaper.getNetworkServices().validStaff(map, new Callback<MobResponseLogin>() {
            @Override
            public void success(MobResponseLogin mobResponseLogin, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (mobResponseLogin.getSuccess()) {
                    openHomeScreen();
                } else {
                    clearLoginCredIf();
                    openMobileLogin();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                openErrorAlert();
            }
        });
    }

    private void openMobileLogin() {
        startActivity(MobileLoginActivity.getStartIntent(this));
        runAnimation();
    }

    private void openHomeScreen() {
        startActivity(DashboardActivity.getStartIntent(this));
        runAnimation();
    }

    private void runAnimation() {
        AnimUtil.slideFromRightAnim(this);
        finish();
    }

    private void clearLoginCredIf() {
        AppPreference.getInstance().clearAll();
    }

    private void internetDialog() {

        final DialogHandler dialogHandler = new DialogHandler(this);
        dialogHandler.setDialog("No Internet", "Please check your internet connection");
        dialogHandler.setOnPositiveButtonClickListener("Ok", new DialogHandler.OnPostiveButtonClick() {
            @Override
            public void Onclick() {
                init();
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

    private void openErrorAlert() {
        final DialogHandler dialogHandler = new DialogHandler(this);
        dialogHandler.setDialog("Error", "Server not working, please try later");
        dialogHandler.setOnNegativeButtonClickListener("Ok", new DialogHandler.OnNegativeButtonClick() {
            @Override
            public void Onclick() {
                dialogHandler.dismiss();
                finish();
            }
        });
    }

}