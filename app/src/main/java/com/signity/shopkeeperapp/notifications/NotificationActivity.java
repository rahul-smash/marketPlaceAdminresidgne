package com.signity.shopkeeperapp.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.notification.NotificationModel;
import com.signity.shopkeeperapp.model.notification.NotificationResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.NotificationAdapterListener {

    private static final String TAG = "NotificationActivity";
    private Toolbar toolbar;
    private NotificationAdapter notificationAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int pageSize = 10, currentPageNumber = 1, start, totalOrders;
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
                        && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    if (start < totalOrders) {
                        getNotification();
                    }
                }
            }
        }
    };

    public static Intent getStartIntent(Context context) {
        return new Intent(context, NotificationActivity.class);
    }

    public boolean isLoading() {
        return ProgressDialogUtil.isProgressLoading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initViews();
        setUpToolbar();
        setUpAdapter();
        getNotification();
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(this);
        notificationAdapter = new NotificationAdapter(this);
        notificationAdapter.setListener(this);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_notification);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickNotification(int position) {
        NotificationResponse notificationResponse = notificationAdapter.getNotificationList().get(position);

        Bundle bundle = new Bundle();
        bundle.putParcelable(NotificationDialog.NOTIFICATION_DATA, notificationResponse);
        NotificationDialog notificationDialog = NotificationDialog.getInstance(bundle);
        notificationDialog.show(getSupportFragmentManager(), NotificationDialog.TAG);
    }

    public void getNotification() {

        if (!Util.checkIntenetConnection(this)) {
            DialogUtils.showAlertDialog(this, "Internet", "Please check your Internet Connection.");
            return;
        }

        ProgressDialogUtil.showProgressDialog(this);
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);

        NetworkAdaper.getNetworkServices().getStoreNotification(param, new Callback<NotificationModel>() {
            @Override
            public void success(NotificationModel notificationModel, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (notificationModel.isSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalOrders = notificationModel.getData().getTotal();

                    notificationAdapter.setNotificationList(notificationModel.getData().getNotification());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }
}
