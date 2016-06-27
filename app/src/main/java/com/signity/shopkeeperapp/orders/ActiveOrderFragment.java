package com.signity.shopkeeperapp.orders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.RvActiveOrderAdapter;
import com.signity.shopkeeperapp.adapter.RvGridSpacesItemDecoration;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.PrefManager;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajinder on 29/9/15.
 */
public class ActiveOrderFragment extends Fragment {

    private Context context;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RvGridSpacesItemDecoration decoration;
    private RvActiveOrderAdapter adapter;

    List<OrdersListModel> orderListModel;
    List<OrdersListModel> listOrderParent;
    private PrefManager prefManager;
    private AppDatabase appDatabase;
    private TextView noDataFound;


    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                ActiveOrderFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        prefManager = new PrefManager(getActivity());
        appDatabase = DbAdapter.getInstance().getDb();
        orderListModel = appDatabase.getOrders();
        if (orderListModel == null) {
            orderListModel = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_order, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        noDataFound = (TextView) rootView.findViewById(R.id.noDataFound);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_view_spacing);
        decoration = new RvGridSpacesItemDecoration(spacingInPixels);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(decoration);
        adapter = new RvActiveOrderAdapter(context, orderListModel);
        recyclerView.setAdapter(adapter);
        getOrders();
        return rootView;
    }


    public void getOrders() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "all");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getValues, Response response) {
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getValues.getData().getOrders().size() > 0) {
//                        appDatabase.setListOrders(getValues.getData().getOrders());
                        orderListModel = getSortedList(getValues.getData().getOrders());
                        setUpRecylerViewData(orderListModel);
                    } else {
                        noDataFound.setVisibility(View.VISIBLE);
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

    private void setUpRecylerViewData(List<OrdersListModel> orderListModel) {
        if (orderListModel != null) {
            adapter = new RvActiveOrderAdapter(context, orderListModel);
            recyclerView.setAdapter(adapter);
        }
    }

    private List<OrdersListModel> getSortedList(List<OrdersListModel> orders) {
        List<OrdersListModel> list = new ArrayList<>();

        for (OrdersListModel order : orders) {
            if (order.getStatus().equalsIgnoreCase("1") || order.getStatus().equalsIgnoreCase("4") || order.getStatus().equalsIgnoreCase("5")) {
                list.add(order);
            }
        }

        return list;
    }


}
