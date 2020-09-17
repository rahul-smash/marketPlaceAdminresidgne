package com.signity.shopkeeperapp.enquiries;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by rajesh on 24/12/15.
 */
public class EnquiriesActivity  extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enquiries);

        String name = getIntent().getStringExtra("name");
        String message = getIntent().getStringExtra("message");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String city = getIntent().getStringExtra("city");
        String bookingDate = getIntent().getStringExtra("booking");
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("message", message);
        bundle.putString("email", email);
        bundle.putString("phone", phone);
        bundle.putString("city", city);
        bundle.putString("booking", bookingDate);

        Fragment fragment = EnquiriesDetailFragment.newInstance(this);
        fragment.setArguments(bundle);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(EnquiriesActivity.this);
    }
}
