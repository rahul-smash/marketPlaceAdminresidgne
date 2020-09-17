package com.signity.shopkeeperapp.customer;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by rajesh on 28/9/15.
 */
public class CustomerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer);

        String name = getIntent().getStringExtra("name");
        String id = getIntent().getStringExtra("id");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("id", id);
        bundle.putString("email", email);
        bundle.putString("phone", phone);
        bundle.putString("address", address);

        Fragment fragment = CustomerDetailFragment.newInstance(this);
        fragment.setArguments(bundle);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(CustomerActivity.this);
    }
}
