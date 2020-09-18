package com.signity.shopkeeperapp.LogInModule;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.login.LoginMobileFragment;

public class LogInWithEmailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_with_email);
        Fragment fragment = LoginMobileFragment.getInstance(null);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();
    }

}
