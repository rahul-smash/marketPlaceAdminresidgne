package com.signity.shopkeeperapp.dashboard.orders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.RvActiveOrderAdapter;
import com.signity.shopkeeperapp.adapter.RvGridSpacesItemDecoration;

import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.orders.AllOrderFragment;
import com.signity.shopkeeperapp.orders.DueOrderActivity;
import com.signity.shopkeeperapp.orders.DueOrderActivityWithoutUpdations;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrdersFragment extends Fragment implements OrdersAdapter.OnItemClickListener {
    public static final String TAG = "OrdersFragment";

    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RvGridSpacesItemDecoration decoration;
    private OrdersAdapter adapter;

    List<OrdersListModel> orderListModel;
    List<OrdersListModel> listOrderMain;

    private String type = null;

    private RelativeLayout parent;

    FloatingActionButton fab;

    public static OrdersFragment getInstance(Bundle bundle) {
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (orderListModel == null) {
            orderListModel = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        Log.i("@@AllOrderFragment", "AllOrderFragment");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_orders);
        parent = (RelativeLayout) rootView.findViewById(R.id.parent);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_view_spacing);
        decoration = new RvGridSpacesItemDecoration(spacingInPixels);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(decoration);
        adapter = new OrdersAdapter(context, orderListModel);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
       /* fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopupForFilter();
            }
        });*/
        return rootView;
    }


    /*Get all orders and Fileter on the basis of active shipped and delivered*/

    public void getAllOrdersMethod() {
        if (Util.checkIntenetConnection(getActivity())) {
            getALLOrders();
        } else {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllOrdersMethod();
    }

    public void getALLOrders() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", Constant.KEY_ALL);

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getValues, Response response) {
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getValues.getData().getOrders().size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        List<OrdersListModel> list = new ArrayList<OrdersListModel>();
                        list.addAll(getValues.getData().getOrders());
                        listOrderMain = list;
                        orderListModel = getValues.getData().getOrders();
                        adapter.updateListItem(orderListModel);

                    } else {
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(),
                        Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    private void filterResult(String statusDue, String statusActive, String statusShipped, String statusAll) {
        orderListModel.clear();
        if (statusAll.equalsIgnoreCase("all")) {
            statusDue = "0";
            statusActive = "1";
            statusShipped = "4";
        }
        for (OrdersListModel order : listOrderMain) {
            if (order.getStatus().equalsIgnoreCase(statusDue) || order.getStatus().equalsIgnoreCase(statusActive) ||
                    order.getStatus().equalsIgnoreCase(statusShipped)) {
                orderListModel.add(order);
            }
        }

        adapter.updateListItem(orderListModel);
    }


    private List<OrdersListModel> getSortedList(List<OrdersListModel> orders) {
        List<OrdersListModel> list = new ArrayList<>();
        for (OrdersListModel order : orders) {
            if (order.getStatus().equalsIgnoreCase("0") || order.getStatus().equalsIgnoreCase("1") || order.getStatus().equalsIgnoreCase("4")) {
                list.add(order);
            }
        }
        return list;
    }


    @Override
    public void onItemClick(View itemView, int position, OrdersListModel order) {
        if (order != null) {
            Log.i("@@particular_click", "clcikEvent");


        }
    }

}
