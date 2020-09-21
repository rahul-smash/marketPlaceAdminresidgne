package com.signity.shopkeeperapp.dashboard.orders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.manage_stores.StaffListFragment;

public class OrderDetailActivity extends AppCompatActivity  implements View.OnClickListener {
    TextView txtTotal, txtTotalPrice, txtCartSavings, txtAddress, txtDate, txtStausVal, txtnoteValue, txtItems;
    RecyclerView recyclerView;
    ImageView imgGuideMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);

        initview();
    }

    private void initview() {
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        txtCartSavings = (TextView) findViewById(R.id.txtCartSavings);
        txtAddress = (TextView) findViewById(R.id.txtTotalPrice);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtStausVal = (TextView) findViewById(R.id.txtStausVal);
        txtnoteValue = (TextView) findViewById(R.id.txtnoteValue);
        txtItems = (TextView) findViewById(R.id.txtItems);
        imgGuideMe=(ImageView)findViewById(R.id.imgGuideMe);

        imgGuideMe.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}
