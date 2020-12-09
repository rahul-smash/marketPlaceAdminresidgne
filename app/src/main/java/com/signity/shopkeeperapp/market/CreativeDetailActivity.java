package com.signity.shopkeeperapp.market;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.classes.StatusBarView;
import com.signity.shopkeeperapp.model.creatives.Creative;
import com.signity.shopkeeperapp.model.creatives.CreativeModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ketan Tetry on 22/11/19.
 */
public class CreativeDetailActivity extends BaseActivity implements CreativeDetailsAdapter.CreativeListener {
    public static final String MARKET_MODE = "MARKET_MODE";
    private static final int REQUEST_PERMISSION = 1002;
    private static final String TAG = CreativeDetailActivity.class.getSimpleName();
    private RecyclerView recyclerViewCreativeDetail;
    private Toolbar toolbar;
    private StatusBarView statusBarView;
    private TextView textViewCreative;
    private List<Creative> creativeList;
    private CreativeDetailsAdapter creativeDetailsAdapter;
    private int tagId;
    private String title;
    private int position;
    private Constant.MarketMode marketMode;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CreativeDetailActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creative_detail);
        initViews();
        getExtra();
        setUpToolbar();
        setUpAdapter();

        ProgressDialogUtil.showProgressDialog(this);
        fetchApiData();
        textViewCreative.setText(title);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textViewCreative = findViewById(R.id.toolbar_title);
        recyclerViewCreativeDetail = findViewById(R.id.rv_creative_detail);
    }

    private void fetchApiData() {
        switch (marketMode) {
            case CREATIVE:
                getCreativeByTagApi();
                break;
            case FRAME:
                getFramesByTagApi();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarGradient(boolean value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            statusBarView.setBackground(value ? getResources().getDrawable(R.drawable.background_white) : getResources().getDrawable(R.drawable.background_white));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(value ? 0 : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                AnimUtil.slideFromLeftAnim(CreativeDetailActivity.this);
            }
        });
    }

    private void getCreativeByTagApi() {

        NetworkAdaper.marketStore().getCreativesById(AppPreference.getInstance().getStoreId(), tagId, 0, new Callback<CreativeModel>() {
            @Override
            public void success(CreativeModel creativeModel, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                creativeList = creativeModel.getCreatives();
                if (creativeList != null && creativeList.size() != 0) {
                    creativeDetailsAdapter.setCreativeList(creativeList);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                }
            }
        });
    }

    private void getFramesByTagApi() {
        NetworkAdaper.marketStore().getFramesById(AppPreference.getInstance().getStoreId(), tagId, new Callback<CreativeModel>() {
            @Override
            public void success(CreativeModel creativeModel, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                creativeList = creativeModel.getCreatives();
                if (creativeList != null && creativeList.size() != 0) {
                    creativeDetailsAdapter.setCreativeList(creativeList);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                }
            }
        });
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            tagId = getIntent().getExtras().getInt("tagId");
            title = getIntent().getExtras().getString("creativeTitle");
            marketMode = (Constant.MarketMode) getIntent().getSerializableExtra(MARKET_MODE);
        }
    }

    private void setUpAdapter() {
        creativeDetailsAdapter = new CreativeDetailsAdapter(this, marketMode);
        creativeDetailsAdapter.setListener(this);
        recyclerViewCreativeDetail.setAdapter(creativeDetailsAdapter);
        recyclerViewCreativeDetail.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_creative_detail, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_creative_details_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                filterCreative(newText.trim());
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        return true;
    }

    private void filterCreative(String newText) {
        List<Creative> creativeFilterList = new ArrayList<>();

        if (creativeList == null) {
            return;
        }

        for (Creative creative : creativeList) {
            if (creative.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                creativeFilterList.add(creative);
            }
        }
        creativeDetailsAdapter.setCreativeList(creativeFilterList);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_creative_details_search:
                break;
            case android.R.id.home:
                super.onBackPressed();
                hideKeyboard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickShare(int position) {

        this.position = position;
        Creative creative = creativeList.get(position);

        switch (marketMode) {
            case CREATIVE:
                openShareCreative(creative);
                break;
            case FRAME:
                /*if (AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic")) {
                    openSubscribeDialog();
                    return;
                }*/
                Bundle bundle = new Bundle();
                bundle.putString("url", creative.getImageUrl());
                bundle.putString("desc", creative.getDescription());
                bundle.putString("title", creative.getTitle());
                bundle.putString("creativeId", String.valueOf(creative.getId()));
                startActivity(CustomFrameActivity.getStartIntent(CreativeDetailActivity.this, bundle));
                break;
        }

    }

/*    private void openSubscribeDialog() {
        if (getSupportFragmentManager().findFragmentByTag(SubscribeDialog.TAG) == null) {
            Bundle bundle = new Bundle();
            bundle.putString("message", "");
            bundle.putString("upgradeMessage", "Kindly upgrade your pack to\naccess custom frames");
            bundle.putString("title", "Upgrade Pack");
            bundle.putString("btnTitle", "Buy Now");
            SubscribeDialog subscribeDialog = SubscribeDialog.getInstance(bundle);
            subscribeDialog.setListener(new SubscribeDialog.DialogListener() {
                @Override
                public void onSubscribe() {
                    subscribeDialog.dismiss();
                    startActivity(SubscriptionActivity.getStartIntent(CreativeDetailActivity.this));
                }
            });
            subscribeDialog.show(getSupportFragmentManager(), SubscribeDialog.TAG);
        }
    }*/

    private void openShareCreative(Creative creative) {
        Intent intent = ShareCreativeActivity.getStartIntent(this);
        intent.putExtra("url", creative.getImageUrl());
        intent.putExtra("desc", creative.getDescription());
        intent.putExtra("title", creative.getTitle());
        intent.putExtra("creativeId", String.valueOf(creative.getId()));
        intent.putExtra("isShared", creative.isShared());
        intent.putExtra("tagId", tagId);
        intent.putExtra(CreativeFragment.MARKET_MODE, marketMode);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickShare(position);
            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClickCreative(int id) {
/*        Bundle bundle = new Bundle();
        bundle.putLong(WebViewActivity.BRAND_ID, AppPreferenceHelper.getInstance().getBrandId());
        bundle.putLong(WebViewActivity.STORE_ID, AppPreferenceHelper.getInstance().getStoreId());
        bundle.putLong(WebViewActivity.CREATIVE_ID, id);
        startActivity(WebViewActivity.getStartIntent(this, bundle));*/
    }

    private void shareIntent(String text, Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Creative"));
    }
}
