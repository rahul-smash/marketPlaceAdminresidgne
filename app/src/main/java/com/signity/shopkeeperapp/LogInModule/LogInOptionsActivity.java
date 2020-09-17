package com.signity.shopkeeperapp.LogInModule;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

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
