package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrdersFragment extends Fragment implements HomeOrdersAdapter.OrdersListener {
    public static final String TAG = "OrdersFragment";

    private List<OrdersListModel> orderListModel = new ArrayList<>();
    private PopupWindow rightMenuPopUpWindow;
    private View hiddenView;
    private RecyclerView recyclerViewOrders;
    private HomeOrdersAdapter ordersAdapter;
    private HomeOrdersAdapter.OrderType orderTypeFilter = HomeOrdersAdapter.OrderType.ALL;
    private LinearLayoutManager layoutManager;
    @IdRes
    private int checkedId;
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
                        currentPageNumber++;
                        getAllOrdersMethod();
                    }
                }
            }
        }
    };

    public static OrdersFragment getInstance(Bundle bundle) {
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewOrders.setLayoutManager(layoutManager);
        ordersAdapter = new HomeOrdersAdapter(getContext(), false);
        ordersAdapter.setListener(this);
        recyclerViewOrders.setAdapter(ordersAdapter);
        recyclerViewOrders.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private void initView(View rootView) {
        recyclerViewOrders = rootView.findViewById(R.id.rv_orders);
        hiddenView = rootView.findViewById(R.id.view);
    }

    public boolean isLoading() {
        return ProgressDialogUtil.isProgressLoading();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setHasOptionsMenu(true);
        getAllOrdersMethod();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_orders, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showPopUpMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAllOrdersMethod() {

        if (!Util.checkIntenetConnection(getContext())) {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            return;
        }

        getALLOrders();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getALLOrders() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, Object> param = new HashMap<>();
        param.put("order_type", orderTypeFilter.getSlug());
        param.put("page", currentPageNumber);
        param.put("pagesize", pageSize);

        NetworkAdaper.getNetworkServices().getStoreOrdersNew(param, new Callback<StoreOrdersReponse>() {
            @Override
            public void success(StoreOrdersReponse getValues, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (getValues.isSuccess()) {
                    start += pageSize;
                    orderListModel = getValues.getData().getOrders();
                    totalOrders = getValues.getData().getOrdersTotal();

                    ordersAdapter.setOrderTypeFilter(orderTypeFilter);
                    ordersAdapter.addOrdersListModels(orderListModel);
                } else {
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void showPopUpMenu() {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popwindow_orders_filter, null, false);

        rightMenuPopUpWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

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
        rightMenuPopUpWindow.showAsDropDown(hiddenView, 0, 0);

        final RadioGroup rdGroup = (RadioGroup) layout.findViewById(R.id.rdGroup);
        RadioButton radioAllOrders = (RadioButton) layout.findViewById(R.id.radioAllOrders);

        RadioButton radioPending = (RadioButton) layout.findViewById(R.id.radioPending);
        RadioButton radioAccepted = (RadioButton) layout.findViewById(R.id.radioAccepted);

        RadioButton radioShipped = (RadioButton) layout.findViewById(R.id.radioShipped);

        RadioButton radioDelivered = (RadioButton) layout.findViewById(R.id.radioDelivered);

        switch (checkedId) {
            case R.id.radioPending:
                radioPending.setChecked(true);
                break;
            case R.id.radioAccepted:
                radioAccepted.setChecked(true);
                break;
            case R.id.radioShipped:
                radioShipped.setChecked(true);
                break;
            case R.id.radioDelivered:
                radioDelivered.setChecked(true);
                break;
            case R.id.radioAllOrders:
            default:
                radioAllOrders.setChecked(true);
        }

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = rdGroup.findViewById(checkedId);
                int index = rdGroup.indexOfChild(radioButton);

                OrdersFragment.this.checkedId = checkedId;
                currentPageNumber = 1;
                start = 0;
                // Add logic here

                switch (index) {
                    case 0: // first button
                        orderTypeFilter = HomeOrdersAdapter.OrderType.ALL;
                        break;
                    case 1:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.PENDING;
                        break;
                    case 2:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.ACCEPTED;
                        break;
                    case 3:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.SHIPPED;
                        break;
                    case 4:
                        orderTypeFilter = HomeOrdersAdapter.OrderType.DELIVERED;
                        break;
                }
                getAllOrdersMethod();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rightMenuPopUpWindow.dismiss();
                    }
                }, 700);
            }
        });
    }

    private void updateOrderStatus(HomeOrdersAdapter.OrderType orderStatus, String orderId, final int position) {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", AppPreference.getInstance().getUserId());
        param.put("order_status", String.valueOf(orderStatus.getStatusId()));
        param.put("order_ids", orderId);

        NetworkAdaper.getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {

                ProgressDialogUtil.hideProgressDialog();
                if (getValues.getSuccess()) {
                    ordersAdapter.removeItem(position);
                } else {
                    Toast.makeText(getContext(), getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    @Override
    public void onClickOrder(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderDetailActivity.ORDER_OBJECT, order);
        startActivity(OrderDetailActivity.getStartIntent(getContext(), bundle));
        AnimUtil.slideFromRightAnim(getActivity());
    }

    @Override
    public void onRejectOrder(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.REJECTED, order.getOrderId(), position);
    }

    @Override
    public void onAcceptOrder(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ACCEPTED, order.getOrderId(), position);
    }

    @Override
    public void onShipOrder(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.SHIPPED, order.getOrderId(), position);
    }

    @Override
    public void onDeliverOrder(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.DELIVERED, order.getOrderId(), position);
    }
}
