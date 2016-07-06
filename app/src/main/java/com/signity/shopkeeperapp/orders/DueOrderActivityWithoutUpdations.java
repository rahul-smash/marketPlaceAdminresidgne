package com.signity.shopkeeperapp.orders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by rajesh on 29/10/15.
 */
public class DueOrderActivityWithoutUpdations extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_order);

        Bundle bundle = getIntent().getExtras();
        Fragment fragment = DueOrderDetailFragmentWithoutUpdations.newInstance(this);
        fragment.setArguments(bundle);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(DueOrderActivityWithoutUpdations.this);
    }
}
