package com.signity.shopkeeperapp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.stores.StoresActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

/**
 * Created by rajesh on 2/11/15.
 * Updated by ketan on 16/09/20.
 */
public class MobileLoginActivity extends BaseActivity implements LoginMobileFragment.LoginMobileListener, VerifyOtpFragment.VerifyOtpListener, LoginChooserFragment.LoginChooserListener, LoginEmailFragment.LoginEmailListener {

    private static final String TAG = "MobileLoginActivity";

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MobileLoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpStatusBar();
        setContentView(R.layout.activity_login);
        saveDeviceToken();
        LoginChooserFragment loginChooserFragment = LoginChooserFragment.getInstance(null);
        showFragment(loginChooserFragment, LoginChooserFragment.TAG);
    }

    private void setUpStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.container, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(LoginMobileFragment.TAG) != null || manager.findFragmentByTag(LoginEmailFragment.TAG) != null) {
            manager.popBackStack();
        } else {
            finish();
            AnimUtil.slidDownAnim(MobileLoginActivity.this);
        }
    }

    @Override
    public void onOtpRequested() {
        VerifyOtpFragment verifyOtpFragment = VerifyOtpFragment.getInstance(null);
        showFragment(verifyOtpFragment, VerifyOtpFragment.TAG);
    }

    @Override
    public void onOtpVerified(boolean chooseStore) {
        if (chooseStore) {
            startActivity(StoresActivity.getStartIntent(this));
        } else {
            startActivity(DashboardActivity.getStartIntent(this));
        }
        runAnimation();
    }

    private void runAnimation() {
        AnimUtil.slideFromRightAnim(this);
        finish();
    }

    @Override
    public void onClickMobile() {
        LoginMobileFragment loginMobileFragment = LoginMobileFragment.getInstance(null);
        showFragment(loginMobileFragment, LoginMobileFragment.TAG);
    }

    @Override
    public void onClickEmail() {
        LoginEmailFragment loginEmailFragment = LoginEmailFragment.getInstance(null);
        showFragment(loginEmailFragment, LoginEmailFragment.TAG);
    }

    @Override
    public void onEmailVerified(boolean verified) {
        onOtpVerified(verified);
    }

    private void saveDeviceToken() {
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    AppPreference.getInstance().setDeviceToken(instanceIdResult.getToken());
                    Log.d("TAG", "onSuccess: " + instanceIdResult.getToken());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}