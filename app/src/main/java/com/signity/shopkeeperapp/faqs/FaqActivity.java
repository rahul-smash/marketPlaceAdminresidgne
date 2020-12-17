package com.signity.shopkeeperapp.faqs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.faqs.adapter.FaqsAdapter;
import com.signity.shopkeeperapp.model.faq.FaqResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * FaqActivity
 *
 * @blame Android Team
 */
public class FaqActivity extends BaseActivity {
    public static final String TAG = "FaqActivity";

    private Toolbar toolbar;
    private ExpandableListView expandableListView;
    private ConstraintLayout constraintLayoutEmpty;
    private FaqsAdapter faqsAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, FaqActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_faqs);
        intiViews();
        setUpToolbar();
        setUp();
        getFaqApi();
    }

    private void intiViews() {
        toolbar = findViewById(R.id.toolbar);
        expandableListView = findViewById(R.id.expandable_faqs);
        constraintLayoutEmpty = findViewById(R.id.cl_empty);
    }

    protected void setUp() {
        faqsAdapter = new FaqsAdapter(this);
        expandableListView.setAdapter(faqsAdapter);
        expandableListView.setDivider(null);
        expandableListView.setGroupIndicator(null);
    }

    private void getFaqApi() {
        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().getFAQs(new Callback<FaqResponse>() {
            @Override
            public void success(FaqResponse faqModels, Response response) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (faqModels.isSuccess()) {
                    constraintLayoutEmpty.setVisibility(View.GONE);
                    faqsAdapter.setFaqList(faqModels.getData());
                } else {
                    constraintLayoutEmpty.setVisibility(View.VISIBLE);
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

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
