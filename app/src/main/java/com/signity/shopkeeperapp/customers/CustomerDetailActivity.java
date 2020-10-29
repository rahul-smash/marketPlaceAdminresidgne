package com.signity.shopkeeperapp.customers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.dashboard.orders.HomeOrdersAdapter;
import com.signity.shopkeeperapp.util.AnimUtil;

public class CustomerDetailActivity extends BaseActivity {

    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    private static final String TAG = "CustomerDetailActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private HomeOrdersAdapter ordersAdapter;
    private String customerId;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CustomerDetailActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CustomerDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        getExtra();
        initViews();
        setUpToolbar();
        setUpAdapter();
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerId = bundle.getString(CUSTOMER_ID);
        }
    }

    private void setUpAdapter() {
        ordersAdapter = new HomeOrdersAdapter(this);
        recyclerView.setAdapter(ordersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_orders);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void runAnimation() {
        AnimUtil.slideFromRightAnim(this);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
