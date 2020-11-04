package com.signity.shopkeeperapp.customers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.book.BookOrderActivity;
import com.signity.shopkeeperapp.model.customers.CustomerDataResponse;
import com.signity.shopkeeperapp.model.customers.CustomersResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomersActivity extends BaseActivity implements CustomersAdapter.CustomerAdapterListener {

    private static final String TAG = "CustomersActivity";
    private Toolbar toolbar;
    private CustomersAdapter customersAdapter;
    private RecyclerView recyclerView;
    private int pageSize = 10, currentPageNumber = 1, start, totalCustomers;
    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    if (start < totalCustomers) {
                        getCustomers();
                    }
                }
            }
        }
    };

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CustomersActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        initViews();
        setUpToolbar();
        setUpAdapter();
        getCustomers();
    }

    public void getCustomers() {
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);

        isLoading = true;
        NetworkAdaper.getNetworkServices().getCustomersNew(param, new Callback<CustomerDataResponse>() {
            @Override
            public void success(CustomerDataResponse customerDataResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                isLoading = false;

                if (customerDataResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalCustomers = customerDataResponse.getData().getCustomersCount();
                    if (customerDataResponse.getData() != null) {
                        customersAdapter.addCustomersList(customerDataResponse.getData().getCustomers(), totalCustomers);
                    } else {
                        Toast.makeText(CustomersActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CustomersActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                isLoading = false;
                if (!isDestroyed()) {
                    Toast.makeText(CustomersActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                    customersAdapter.setShowLoading(false);
                }
            }
        });
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(this);
        customersAdapter = new CustomersAdapter(this);
        customersAdapter.setListener(this);
        recyclerView.setAdapter(customersAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 12)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_customers);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void runAnimation() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_customer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            runAnimation();
        }

        if (item.getItemId() == R.id.action_add_customer) {
            startActivity(AddCustomerActivity.getStartIntent(this));
            AnimUtil.slideFromRightAnim(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickCustomer(CustomersResponse customersResponse) {
        Bundle bundle = new Bundle();
        bundle.putString(CustomerDetailActivity.CUSTOMER_ID, customersResponse.getId());
        startActivity(CustomerDetailActivity.getStartIntent(this, bundle));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onClickBookOrder(CustomersResponse customersResponse) {
        Bundle bundle = new Bundle();
        bundle.putString(BookOrderActivity.CUSTOMER_NUMBER, customersResponse.getPhone());
        startActivity(BookOrderActivity.getIntent(this, bundle));
        AnimUtil.slideFromRightAnim(this);
    }
}
