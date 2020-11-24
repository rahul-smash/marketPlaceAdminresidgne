package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.creatives.CreativeModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Ketan Tetry on 26/11/19.
 */
public class FilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 123;
    public static final String MARKET_MODE = "MARKET_MODE";

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private FilterRecycleAdapter recycleAdapter;
    private List<String> tagsList = new ArrayList<>();
    private ArrayList<String> choosenList = new ArrayList<>();
    private Constant.MarketMode marketMode;

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FilterActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setUpToolbar();
        getExtra();
        setUpAdapter();
        fetchApiData();
    }

    private void getExtra() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            choosenList = bundle.getStringArrayList("choosen");
            marketMode = (Constant.MarketMode) bundle.getSerializable(MARKET_MODE);
        }
    }

    private void fetchApiData() {
        ProgressDialogUtil.showProgressDialog(this);

        switch (marketMode) {
            case CREATIVE:
                getCreatives();
                break;
            case FRAME:
                getFrames();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + marketMode);
        }

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
    }

    private void setUpAdapter() {
        recycleAdapter = new FilterRecycleAdapter(this);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getCreatives() {
        NetworkAdaper.marketStore().getCreatives(1,1,new Callback<List<CreativeModel>>() {
            @Override
            public void success(List<CreativeModel> creativeModels, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (creativeModels.isEmpty()) {
                    return;
                }

                for (CreativeModel creativeModel : creativeModels) {
                    if (creativeModel.getCreatives() == null || creativeModel.getCreatives().size() == 0) {
                        continue;
                    }
                    tagsList.add(creativeModel.getTitle());
                }

                recycleAdapter.setAllTags(tagsList);
                recycleAdapter.setChoosen(choosenList);
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void getFrames() {

        NetworkAdaper.marketStore().getFrames(1,1,new Callback<List<CreativeModel>>() {
            @Override
            public void success(List<CreativeModel> creativeModels, Response response) {
                ProgressDialogUtil.hideProgressDialog();
                if (creativeModels.isEmpty()) {
                    return;
                }

                for (CreativeModel creativeModel : creativeModels) {
                    if (creativeModel.getCreatives() == null || creativeModel.getCreatives().size() == 0) {
                        continue;
                    }
                    tagsList.add(creativeModel.getTitle());
                }

                recycleAdapter.setAllTags(tagsList);
                recycleAdapter.setChoosen(choosenList);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void onClickApply() {

        if (recycleAdapter.getIds().size() == 0) {
            Toast.makeText(this, "Select Categories", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra("result", (ArrayList<String>) recycleAdapter.getIds());
        setResult(RESULT_OK, intent);

        finish();
    }

    public void onClickClear() {
        recycleAdapter.clearChoosen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_close_filter) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
