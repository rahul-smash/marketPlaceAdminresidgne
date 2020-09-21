package com.signity.shopkeeperapp.dashboard.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.dashboard.StoreDashboardResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment implements HomeContentAdapter.HomeContentAdapterListener {

    public static final String TAG = "HomeFragment";
    private TextView textViewNotificationCount;
    private RecyclerView recyclerViewContent;
    private RecyclerView recyclerViewOrders;
    private HomeContentAdapter homeContentAdapter;
    private int notificationCount = 12;

    public static HomeFragment getInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        init(view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        storeDashboard();
    }

    public void storeDashboard() {
        Map<String, Integer> param = new HashMap<String, Integer>();
        param.put("days_filder", Constant.StoreDashboard.TODAY.getDays());

        NetworkAdaper.getNetworkServices().storeDashboard(param, new Callback<StoreDashboardResponse>() {
            @Override
            public void success(StoreDashboardResponse storeDashboardResponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                if (storeDashboardResponse.isSuccess()) {
                    homeContentAdapter.setUpData(storeDashboardResponse.getData());
                } else {
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, storeDashboardResponse.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }

    private void setUpAdapter() {
        homeContentAdapter = new HomeContentAdapter(getContext());
        homeContentAdapter.setListener(this);
        recyclerViewContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewContent.setAdapter(homeContentAdapter);
    }

    private void init(View view) {
        recyclerViewContent = view.findViewById(R.id.rv_content);
        recyclerViewOrders = view.findViewById(R.id.rv_orders);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = menuItem.getActionView();

        if (actionView == null) {
            return;
        }

        textViewNotificationCount = (TextView) actionView.findViewById(R.id.notification_count);

        setUpBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
    }

    private void setUpBadge() {
        if (textViewNotificationCount != null) {
            if (notificationCount == 0) {
                textViewNotificationCount.setVisibility(View.GONE);
            } else {
                textViewNotificationCount.setText(String.valueOf(Math.min(notificationCount, 99)));
                textViewNotificationCount.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_notification) {
            Toast.makeText(getContext(), "Notification", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClickItem(HomeContentAdapter.HomeItems homeItems) {
        Toast.makeText(getContext(), homeItems.getTitle(), Toast.LENGTH_SHORT).show();
        switch (homeItems) {
            case ORDERS:
                break;
            case REVENUE:
                break;
            case ALL_CUSTOMERS:
                break;
            case TOTAL_PRODUCT:
                break;
        }
    }
}
