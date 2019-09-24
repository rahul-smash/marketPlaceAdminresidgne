package com.signity.shopkeeperapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by rajesh on 2/11/15.
 */
public class LoginScreenActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fragment fragment;
        String type = getIntent().getStringExtra("type");
        Log.i("@@Type_value",""+type);
        if (type != null && type.equalsIgnoreCase("email")) {
            fragment = LoginFragmentEmail.newInstance(this);
        }else{
            fragment = LoginFragmentMobile.newInstance(this);
        }

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slidDownAnim(LoginScreenActivity.this);
    }
}