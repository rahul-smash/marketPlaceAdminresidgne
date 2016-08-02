package com.signity.shopkeeperapp.LogInModule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.view.LoginFragmentMobile;

public class LogInWithEmailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_with_email);
        Fragment fragment = LoginFragmentMobile.newInstance(this);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();
    }

}
