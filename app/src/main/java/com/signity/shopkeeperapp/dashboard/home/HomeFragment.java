package com.signity.shopkeeperapp.dashboard.home;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.dashboard.orders.HomeOrdersAdapter;
import com.signity.shopkeeperapp.model.dashboard.StoreDashboardResponse;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;

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
    private HomeOrdersAdapter homeOrdersAdapter;
    private LinearLayout linearLayoutViewAllOrders;
    private ContentLoadingProgressBar progressBar;
    private ChipGroup chipGroup;
    private HomeFragmentListener listener;
    private int notificationCount = 12;
    LinearLayout llToday;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DashboardActivity) {
            listener = (HomeFragmentListener) context;
        }
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
        getOrders(HomeOrdersAdapter.OrderType.ALL);
    }

    @Override
    public void onResume() {
        super.onResume();
        storeDashboard(Constant.StoreDashboard.TODAY);
    }

    public void getOrders(final HomeOrdersAdapter.OrderType orderType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", orderType.name().toLowerCase());

        progressBar.show();
        NetworkAdaper.getNetworkServices().getDashbaordStoreOrders(param, new Callback<StoreOrdersReponse>() {
            @Override
            public void success(StoreOrdersReponse ordersReponse, Response response) {

                progressBar.hide();
                if (ordersReponse.isSuccess()) {
                    homeOrdersAdapter.setOrdersListModels(ordersReponse.getData().getOrders());
                } else {

                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.hide();
                Log.d(TAG, "failure: " + error.getMessage());
            }
        });
    }

    public void storeDashboard(Constant.StoreDashboard typeofDay) {
        Map<String, Integer> param = new HashMap<String, Integer>();
        param.put("days_filder", typeofDay.getDays());

        NetworkAdaper.getNetworkServices().storeDashboard(param, new Callback<StoreDashboardResponse>() {
            @Override
            public void success(StoreDashboardResponse storeDashboardResponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                if (storeDashboardResponse.isSuccess()) {
                    homeContentAdapter.setUpData(storeDashboardResponse.getData());
                } else {
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void setUpAdapter() {
        homeContentAdapter = new HomeContentAdapter(getContext());
        homeContentAdapter.setListener(this);
        recyclerViewContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewContent.setAdapter(homeContentAdapter);

        homeOrdersAdapter = new HomeOrdersAdapter(getContext());
        recyclerViewOrders.setAdapter(homeOrdersAdapter);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void init(View view) {
        llToday = view.findViewById(R.id.llToday);
        recyclerViewContent = view.findViewById(R.id.rv_content);
        recyclerViewOrders = view.findViewById(R.id.rv_orders);
        linearLayoutViewAllOrders = view.findViewById(R.id.ll_view_all_orders);
        chipGroup = view.findViewById(R.id.chip_group);
        progressBar = view.findViewById(R.id.content_progress);
        Chip allChip = view.findViewById(R.id.chip_all);
        allChip.setChecked(true);
        llToday.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Log.i("@@Today_event", "click_open");
                                           LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                           View layout = inflater.inflate(R.layout.popup_view_today,
                                                   (ViewGroup) getActivity().findViewById(R.id.popups));


                                           final PopupWindow rightMenuPopUpWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

                                           rightMenuPopUpWindow.setOutsideTouchable(true);
                                           rightMenuPopUpWindow.setBackgroundDrawable(new ColorDrawable());
                                           rightMenuPopUpWindow.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
                                               @Override
                                               public boolean onTouch(View v, MotionEvent event) {
                                                   if (event.getAction() == MotionEvent.ACTION_OUTSIDE) // here I want to close the pw when clicking outside it but at all this is just an example of how it works and you can implement the onTouch() or the onKey() you want
                                                   {
                                                       rightMenuPopUpWindow.dismiss();
                                                       return true;
                                                   }
                                                   return false;
                                               }

                                           });

                                           float den = getActivity().getResources().getDisplayMetrics().density;
                                           int offsetY = (int) (den * 2);
                                           rightMenuPopUpWindow.showAsDropDown(view, offsetY, offsetY);

                                           final RadioGroup rdGroup = (RadioGroup) layout.findViewById(R.id.rdGroup);

                                           rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                   View radioButton = rdGroup.findViewById(checkedId);
                                                   int index = rdGroup.indexOfChild(radioButton);

                                                   // Add logic here

                                                   switch (index) {
                                                       case 0: // first button

                                                           Toast.makeText(getActivity(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                                                           storeDashboard(Constant.StoreDashboard.TODAY);

                                                           break;
                                                       case 1:

                                                           Toast.makeText(getActivity(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                                                           storeDashboard(Constant.StoreDashboard.YESTERDAY);
                                                           break;
                                                       case 2:
                                                           storeDashboard(Constant.StoreDashboard.LAST_WEEK);

                                                           Toast.makeText(getActivity(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                                                           break;

                                                   }
                                               }
                                           });
                                       }

                                   }
        );
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.chip_all:
                        getOrders(HomeOrdersAdapter.OrderType.ALL);
                        break;
                    case R.id.chip_pending:
                        getOrders(HomeOrdersAdapter.OrderType.PENDING);
                        break;
                    case R.id.chip_accepted:
                        getOrders(HomeOrdersAdapter.OrderType.ACCEPTED);
                        break;
                    case R.id.chip_shipped:
                        getOrders(HomeOrdersAdapter.OrderType.SHIPPED);
                        break;
                    case R.id.chip_delivered:
                        getOrders(HomeOrdersAdapter.OrderType.DELIVERED);
                        break;
                }
            }
        });

        linearLayoutViewAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickViewAllOrders();
                }
            }
        });
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

    public interface HomeFragmentListener {
        void onClickViewAllOrders();
    }
}
