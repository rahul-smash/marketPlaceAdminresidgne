package com.signity.shopkeeperapp.stores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.model.verify.DataResponse;
import com.signity.shopkeeperapp.model.verify.StoreResponse;
import com.signity.shopkeeperapp.model.verify.UserResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity implements StoresAdapter.StoresAdapterListener {

    public static final String STORES_LIST = "STORES_LIST";
    private static final String TAG = "StoresActivity";
    private Toolbar toolbar;
    private StoresAdapter storesAdapter;
    private RecyclerView recyclerView;
    private List<DataResponse> dataResponses = new ArrayList<>();

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
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            dataResponses = bundle.getParcelableArrayList(STORES_LIST);
        }
    }

    private void setUpAdapter() {
        storesAdapter = new StoresAdapter(this);
        storesAdapter.setListener(this);
        storesAdapter.setStoreResponseList(dataResponses);
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
    }

    @Override
    public void onClickStore(int position) {

        StoreResponse store = dataResponses.get(position).getStoreResponse();
        UserResponse user = dataResponses.get(position).getUserResponse();

        AppPreference.getInstance().setLoggedIn(Constant.Mode.LOGGED_IN);
        AppPreference.getInstance().saveStore(store);
        AppPreference.getInstance().saveUser(user);
        NetworkAdaper.setupRetrofitClient(NetworkAdaper.setBaseUrl(AppPreference.getInstance().getStoreId()));
        startActivity(DashboardActivity.getStartIntent(this));
        AnimUtil.slideFromRightAnim(this);
        finish();
    }
}
