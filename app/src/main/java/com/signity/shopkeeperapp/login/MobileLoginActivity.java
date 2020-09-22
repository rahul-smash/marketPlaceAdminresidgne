package com.signity.shopkeeperapp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.stores.StoresActivity;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by rajesh on 2/11/15.
 * Updated by ketan on 16/09/20.
 */
public class MobileLoginActivity extends AppCompatActivity implements LoginMobileFragment.LoginMobileListener, VerifyOtpFragment.VerifyOtpListener {

    private static final String TAG = "MobileLoginActivity";

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MobileLoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginMobileFragment loginMobileFragment = LoginMobileFragment.getInstance(null);
        showFragment(loginMobileFragment, LoginMobileFragment.TAG);
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
        if (manager.findFragmentByTag(VerifyOtpFragment.TAG) != null) {
            manager.popBackStack(VerifyOtpFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
}