package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.creatives.Creative;
import com.signity.shopkeeperapp.model.creatives.SharedPostModel;
import com.signity.shopkeeperapp.model.market.facebook.EngagementModel;
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

public class SharedPostActivity extends BaseActivity implements SharedPostAdapter.CreativeListener {

    private static final String TAG = "SharedPostActivity";
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayoutEmpty;
    private int limit = 10, start = 0, totalCreatives;
    private SharedPostAdapter sharedPostAdapter;
    private List<Creative> creativeList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
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

            if (!isLoading()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= limit) {
                    if (start < totalCreatives) {
                        getSharedPosts(limit, start);
                    }
                }
            }
        }
    };

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SharedPostActivity.class);
    }

    private boolean isLoading() {
        return ProgressDialogUtil.isProgressLoading();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_post);
        initView();
        setUp();
    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_shared_creative);
        toolbar = findViewById(R.id.toolbar);
        constraintLayoutEmpty = findViewById(R.id.cl_empty);
    }

    protected void setUp() {
        setUpToolbar();
        setUpAdapter();
        getSharedPosts(limit, start);
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
                AnimUtil.slideFromLeftAnim(SharedPostActivity.this);
            }
        });
    }

    private void getSharedPosts(final int limit, int start) {
        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.marketStore().getSharedPosts(AppPreference.getInstance().getStoreId(), limit, start, new Callback<SharedPostModel>() {
            @Override
            public void success(SharedPostModel sharedPostModel, Response response) {

                if (sharedPostModel.isStatus()) {
                    constraintLayoutEmpty.setVisibility(View.GONE);
                    SharedPostActivity.this.start += limit;
                    totalCreatives = sharedPostModel.getTotalSharedCreative();
                    creativeList = sharedPostModel.getCreatives();
                    fetchLSC();
                } else {
                    constraintLayoutEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void fetchLSC() {
        List<GraphRequest> graphRequests = new ArrayList<>();

        if (creativeList.size() == 0) {
            ProgressDialogUtil.hideProgressDialog();
            return;
        }

        for (final Creative creative1 : creativeList) {
            GraphRequest request1 = new GraphRequest();
            request1.setAccessToken(AccessToken.getCurrentAccessToken());
            request1.setGraphPath("/" + creative1.getPostId());
            request1.setCallback(new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {

                    if (response.getJSONObject() == null) {
                        return;
                    }

                    EngagementModel engagementModel = new Gson().fromJson(response.getJSONObject().toString(), EngagementModel.class);

                    int likes = engagementModel.getLikes().getSummary().getTotalCount();
                    int comments = engagementModel.getComments().getSummary().getTotalCount();
                    int shares = engagementModel.getShares() != null ? engagementModel.getShares().getCount() : 0;

                    creative1.setLikeCount(likes);
                    creative1.setShareCount(shares);
                    creative1.setCommentCount(comments);

                    saveFacebookEngagement(likes, comments, shares, creative1.getId(), creative1.getPostId());
                    Log.d(TAG, "onCompleted: Likes,Comments,Shares" + likes + " " + comments + " " + shares);
                }
            });
            Bundle param = new Bundle();
            param.putString("fields", "comments.summary(total_count),likes.summary(total_count),shares");
            request1.setParameters(param);

            graphRequests.add(request1);
        }

        GraphRequestBatch graphRequestBatch = new GraphRequestBatch(graphRequests);
        graphRequestBatch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch batch) {
                ProgressDialogUtil.hideProgressDialog();
                sharedPostAdapter.addCreativeData(creativeList);
                if (sharedPostAdapter.getItemCount() > 0) {
                    constraintLayoutEmpty.setVisibility(View.GONE);
                }
            }
        });

        graphRequestBatch.executeAsync();
    }

    private void saveFacebookEngagement(int likes, int comments, int shares, Integer id, String postId) {

/*        EngagementData engagementData = new EngagementData();
        engagementData.setBrandId(AppPreferenceHelper.getInstance().getBrandId());
        engagementData.setStoreId(AppPreferenceHelper.getInstance().getStoreId());
        engagementData.setCreativeId(id);
        engagementData.setPostId(postId);
        engagementData.setTotalLike(likes);
        engagementData.setTotalComment(comments);
        engagementData.setTotalShare(shares);

        AppApiHelper.getApiHelper()
                .updateEngagement(engagementData)
                .enqueue(new DigiCallback<ResponseBody>(this) {
                    @Override
                    public void onSuccess(ResponseBody response) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                hideKeyboard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpAdapter() {
        sharedPostAdapter = new SharedPostAdapter(this);
        sharedPostAdapter.setListener(this);
        recyclerView.setAdapter(sharedPostAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    }

    @Override
    public void onClickShare(int position) {
        openShareCreative(sharedPostAdapter.getCreativeList().get(position));
    }

    private void openShareCreative(Creative creative) {
        Intent intent = ShareCreativeActivity.getStartIntent(this);
        intent.putExtra("url", creative.getImageUrl());
        intent.putExtra("desc", creative.getDescription());
        intent.putExtra("title", creative.getTitle());
        intent.putExtra("creativeId", String.valueOf(creative.getId()));
        intent.putExtra("isShared", true);
        intent.putExtra("tagId", creative.getTags() != null ? creative.getTags().get(0).getId().intValue() : 0);
        intent.putExtra(CreativeFragment.MARKET_MODE, Constant.MarketMode.CREATIVE);
        startActivity(intent);
    }
}
