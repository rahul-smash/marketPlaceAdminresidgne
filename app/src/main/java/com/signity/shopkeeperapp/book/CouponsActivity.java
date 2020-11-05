package com.signity.shopkeeperapp.book;

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
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.orders.offers.StoreOffersResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CouponsActivity extends BaseActivity implements CouponsAdapter.CouponsAdapterListener {

    private static final String TAG = "CouponsActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerViewCoupons;
    private CouponsAdapter couponsAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CouponsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        initViews();
        setUpToolbar();
        setUpAdapter();
        getCoupons();
    }

    private void getCoupons() {

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.orderNetworkServices(AppPreference.getInstance().getStoreId()).getCoupons(new Callback<StoreOffersResponse>() {
            @Override
            public void success(StoreOffersResponse storeOffersResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (storeOffersResponse.isSuccess()) {
                    couponsAdapter.setData(storeOffersResponse.getData());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });

    }

    private void setUpAdapter() {
        couponsAdapter = new CouponsAdapter(this);
        couponsAdapter.setListener(this);
        recyclerViewCoupons.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCoupons.setAdapter(couponsAdapter);
        recyclerViewCoupons.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 16)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewCoupons = findViewById(R.id.rv_coupons);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
            runAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickApply() {
        // TODO - Pass the coupon code back to the activity
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }
}
