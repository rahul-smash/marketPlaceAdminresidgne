package com.signity.shopkeeperapp.manage_stores;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.signity.shopkeeperapp.R;

/**
 * Created by rajesh on 23/2/16.
 */
public class ManageStaffActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_staff);

        Fragment fragment = StaffListFragment.newInstance(this);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();

    }


}
