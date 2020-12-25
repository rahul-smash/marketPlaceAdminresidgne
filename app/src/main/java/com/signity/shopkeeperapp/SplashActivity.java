package com.signity.shopkeeperapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.login.MobileLoginActivity;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.onboarding.OnBoardingActivity;
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

    public static Intent getIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpStatusBar();
        setContentView(R.layout.activity_splash);
    }

    private void setUpStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = 1024;
        decorView.setSystemUiVisibility(uiOptions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O && Build.VERSION.SDK_INT != Build.VERSION_CODES.O_MR1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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

        if (!AppPreference.getInstance().isOnBoardingShown()) {
            openOnBoarding();
            return;
        }

        if (!Util.checkIntenetConnection(this)) {
            internetDialog();
            return;
        }

        if (AppPreference.getInstance().isLoggedIn() == Constant.Mode.LOGGED_IN) {
            openHomeScreen();
        } else {
            openMobileLogin();
        }

    }

    private void openOnBoarding() {
        AppPreference.getInstance().setOnBoardingShown(true);
        startActivity(OnBoardingActivity.getStartIntent(this));
        runAnimation();
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