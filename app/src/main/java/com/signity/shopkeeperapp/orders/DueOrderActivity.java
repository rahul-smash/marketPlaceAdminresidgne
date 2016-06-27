package com.signity.shopkeeperapp.orders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by rajesh on 28/9/15.
 */
public class DueOrderActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_due_order);

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String orderID = getIntent().getStringExtra("orderID");
        String userID = getIntent().getStringExtra("userID");
        String type = getIntent().getStringExtra("type");

        String note = getIntent().getStringExtra("note");
        Double discount = getIntent().getDoubleExtra("discount", 0.00);
        Double total = getIntent().getDoubleExtra("total", 0.00);
        Double tax = getIntent().getDoubleExtra("tax", 0.00);
        Double shipping_charges = getIntent().getDoubleExtra("shipping_charges", 0.00);
        String address = getIntent().getStringExtra("address");


        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("phone", phone);
        bundle.putString("orderID", orderID);
        bundle.putString("userID", userID);
        bundle.putString("type", type);

        bundle.putString("note", note);
        bundle.putDouble("discount", discount);
        bundle.putDouble("total", total);
        bundle.putDouble("tax", tax);
        bundle.putDouble("shipping_charges", shipping_charges);
        bundle.putString("address", address);

        Fragment fragment = DueOrderDetailFragment.newInstance(this);
        fragment.setArguments(bundle);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(DueOrderActivity.this);
    }

}
