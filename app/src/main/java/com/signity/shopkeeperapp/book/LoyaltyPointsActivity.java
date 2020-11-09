package com.signity.shopkeeperapp.book;

import android.app.Activity;
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
import com.signity.shopkeeperapp.model.orders.loyalty.LoyaltyPointsResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoyaltyPointsActivity extends BaseActivity implements LoyaltyPointsAdapter.LoyaltyPointsAdapterListener {

    public static final String LOYALTY = "LOYALTY";
    public static final String POINT = "POINT";
    private static final String TAG = "LoyaltyPointsActivity";
    public static final String DISCOUNT = "DISCOUNT";
    private Toolbar toolbar;
    private RecyclerView recyclerViewLoyalty;
    private LoyaltyPointsAdapter loyaltyPointsAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoyaltyPointsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_points);
        initViews();
        setUpToolbar();
        setUpAdapter();
        getLoyaltyPoints();
    }

    private void getLoyaltyPoints() {

        Map<String, String> param = new HashMap<>();
        param.put("user_id", "3246");

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.orderNetworkServices(AppPreference.getInstance().getStoreId()).getLoyalityPoints(param, new Callback<LoyaltyPointsResponse>() {
            @Override
            public void success(LoyaltyPointsResponse loyaltyPointsResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (loyaltyPointsResponse.isSuccess()) {
                    loyaltyPointsAdapter.setData(loyaltyPointsResponse.getData());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });

    }

    private void setUpAdapter() {
        loyaltyPointsAdapter = new LoyaltyPointsAdapter(this);
        loyaltyPointsAdapter.setListener(this);
        recyclerViewLoyalty.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLoyalty.setAdapter(loyaltyPointsAdapter);
        recyclerViewLoyalty.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 16)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewLoyalty = findViewById(R.id.rv_loyalty_points);
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
    public void onClickApply(String coupon, String discount,String point) {
        Intent intent = new Intent();
        intent.putExtra(LOYALTY, coupon);
        intent.putExtra(DISCOUNT, discount);
        intent.putExtra(POINT, point);
        setResult(Activity.RESULT_OK, intent);
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }
}
