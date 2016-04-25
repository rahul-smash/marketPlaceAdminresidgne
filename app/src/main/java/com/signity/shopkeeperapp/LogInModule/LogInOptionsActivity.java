package com.signity.shopkeeperapp.LogInModule;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.view.LoginFragmentMobile;
import com.signity.shopkeeperapp.view.LoginScreenActivity;

public class LogInOptionsActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_options);

        Fragment fragment = LogInOptionsFragment.newInstance(this);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slidDownAnim(LogInOptionsActivity.this);
    }


}
