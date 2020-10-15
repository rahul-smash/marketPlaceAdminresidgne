package com.signity.shopkeeperapp.stores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.model.stores.StoresResponse;
import com.signity.shopkeeperapp.model.verify.StoreResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StoresActivity extends BaseActivity implements StoresAdapter.StoresAdapterListener {

    public static final String STORES_LIST = "STORES_LIST";
    private static final String TAG = "StoresActivity";
    private Toolbar toolbar;
    private StoresAdapter storesAdapter;
    private RecyclerView recyclerView;
    private List<StoreResponse> storeResponseList = new ArrayList<>();

    public static Intent getStartIntent(Context context) {
        return new Intent(context, StoresActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, StoresActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        getExtra();
        initViews();
        setUpToolbar();
        setUpAdapter();
        getAllStores();
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            storeResponseList = bundle.getParcelableArrayList(STORES_LIST);
        }
    }

    private void setUpAdapter() {
        storesAdapter = new StoresAdapter(this);
        storesAdapter.setListener(this);
        storesAdapter.setStoreResponseList(storeResponseList);
        recyclerView.setAdapter(storesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_stores);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getAllStores() {
        ProgressDialogUtil.showProgressDialog(this);
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        String deviceToken = AppPreference.getInstance().getDeviceToken();
        String mobileNumber = AppPreference.getInstance().getUserMobile();

        Map<String, String> param = new HashMap<>();
        param.put("mobile", mobileNumber);
        param.put("device_id", deviceId);
        param.put("device_token", deviceToken);
        param.put("platform", Constant.PLATFORM);

        NetworkAdaper.getNetworkServices().getAdminStoresNew(param, new Callback<StoresResponse>() {
            @Override
            public void success(StoresResponse mobResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();

                if (mobResponse.getSuccess()) {
                    if (mobResponse.getStoresList().size() > 0) {
                        storeResponseList = mobResponse.getStoresList();
                        storesAdapter.setStoreResponseList(storeResponseList);
                    } else {
                        Toast.makeText(StoresActivity.this, "No store found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(StoresActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClickStore(int position) {

        StoreResponse store = storeResponseList.get(position);

        AppPreference.getInstance().setLoggedIn(Constant.Mode.LOGGED_IN);
        AppPreference.getInstance().saveStore(store);
        NetworkAdaper.setupRetrofitClient(NetworkAdaper.setBaseUrl(AppPreference.getInstance().getStoreId()));
        startActivity(DashboardActivity.getStartIntent(this));
        runAnimation();
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
