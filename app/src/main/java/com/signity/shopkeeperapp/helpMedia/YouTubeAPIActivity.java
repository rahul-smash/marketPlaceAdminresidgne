package com.signity.shopkeeperapp.helpMedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.helpMedia.adapter.YoutubeVideoAdapter;
import com.signity.shopkeeperapp.model.helpMediaModel.HelpVideoResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @blame Android Team
 */
public class YouTubeAPIActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "YouTubeAPIActivity";

    private Toolbar toolbar;
    private RecyclerView recyclerViewYoutube;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayoutEmpty;
    private YoutubeVideoAdapter youtubeVideoAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, YouTubeAPIActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_api);
        initViews();
        setUp();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewYoutube = findViewById(R.id.rv_youtube);
        swipeRefreshLayout = findViewById(R.id.swipe);
        constraintLayoutEmpty = findViewById(R.id.cl_empty);
    }

    protected void setUp() {
        setUpToolbar();
        setAdapter();
        setUpSwipe();
        getHelpVideosApi();
    }


    private void setUpSwipe() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark
                ));
    }

    private void setAdapter() {
        youtubeVideoAdapter = new YoutubeVideoAdapter(YouTubeAPIActivity.this);
        recyclerViewYoutube.setAdapter(youtubeVideoAdapter);
        recyclerViewYoutube.setLayoutManager(new LinearLayoutManager(YouTubeAPIActivity.this));
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getHelpVideosApi() {
        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().getHelpVideos(new Callback<HelpVideoResponse>() {
            @Override
            public void success(HelpVideoResponse helpVideos, Response response) {

                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (helpVideos.isSuccess()) {
                    constraintLayoutEmpty.setVisibility(View.GONE);
                    youtubeVideoAdapter.setHelpVideosArrayList(helpVideos.getData());
                } else {
                    constraintLayoutEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        getHelpVideosApi();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            AnimUtil.slideFromLeftAnim(this);
            hideKeyboard();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(this);
    }
}
