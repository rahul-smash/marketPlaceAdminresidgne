package com.signity.shopkeeperapp.orders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.RvActiveOrderAdapter;
import com.signity.shopkeeperapp.adapter.RvGridSpacesItemDecoration;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.canceled_orders.CanceledOrdersItemsListActivity;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.rejected_orders.RejectedItemsListActivity;
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

/**
 * Created by Rajinder on 29/9/15.
 */
public class ActiveOrderFragment extends Fragment implements RvActiveOrderAdapter.OnItemClickListener {

    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RvGridSpacesItemDecoration decoration;
    private RvActiveOrderAdapter adapter;

    List<OrdersListModel> orderListModel;
    List<OrdersListModel> listOrderMain;
    private PrefManager prefManager;
    private DbInsertUpdateTask dbTask;
    private AppDatabase appDatabase;
    private TextView noDataFound;

    private String type = null;

    String isRefreshRequrie = "";

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                ActiveOrderFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRefreshRequrie = "0";
        type = getArguments().getString("type");
        context = getActivity();
        prefManager = new PrefManager(getActivity());
        appDatabase = DbAdapter.getInstance().getDb();
        dbTask = new DbInsertUpdateTask();
//        orderListModel = appDatabase.getOrders();
        if (orderListModel == null) {
            orderListModel = new ArrayList<>();
        }
        prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "0");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_order_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        noDataFound = (TextView) rootView.findViewById(R.id.noDataFound);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_view_spacing);
        decoration = new RvGridSpacesItemDecoration(spacingInPixels);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(decoration);
        adapter = new RvActiveOrderAdapter(context, orderListModel);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        getOrders();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        isRefreshRequrie = prefManager.getSharedValue(Constant.REFERESH_DATA_REQURIED);
        if (!isRefreshRequrie.isEmpty() && isRefreshRequrie.equalsIgnoreCase("1")) {
            getOrders();
        }
    }

    public void getOrders() {
        if (type.equalsIgnoreCase(Constant.TYPE_REJECTED)) {
            getRejectedOrderMethod();
        } else if (type.equalsIgnoreCase(Constant.TYPE_CANCELLED)) {
            getCancelledOrderMethod();
        } else if (type.equalsIgnoreCase(Constant.TYPE_DELIVERED)) {
            getAllDeliveredOrderMethod();
        }
    }

    /* Rejected Orders Calling type CONSTANT.TYPE_REJECTED*/
    public void getRejectedOrderMethod() {
        if (Util.checkIntenetConnection(getActivity())) {
            getRejectedOrder();
        } else {
            List<OrdersListModel> rejctedOrders = appDatabase.getRejctedOrders();
            if (rejctedOrders != null) {
                adapter.updateListItem(rejctedOrders);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
        }
    }

    /*Rejected orders*/
    private void getRejectedOrder() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("api_key", "");
        param.put("order_type", "rejected");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {

            @Override
            public void success(GetOrdersModel getOrdersModel, Response response) {
                if (getOrdersModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getOrdersModel != null) {
                        if (getOrdersModel.getData().getOrders().size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.GONE);
                            orderListModel = getOrdersModel.getData().getOrders();
                            adapter.updateListItem(orderListModel);
                            dbTask.execute(orderListModel);
                            prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "0");
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    recyclerView.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getOrdersModel.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }

    public void getCancelledOrderMethod() {
        if (Util.checkIntenetConnection(getActivity())) {
            getCanceledOrder();
        } else {
            List<OrdersListModel> cancelOrders = appDatabase.getCancelOrders();
            if (cancelOrders != null) {
                adapter.updateListItem(cancelOrders);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
        }
    }


    private void getCanceledOrder() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("api_key", "");
        param.put("order_type", "cancel");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {

            @Override
            public void success(GetOrdersModel getOrdersModel, Response response) {
                if (getOrdersModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getOrdersModel != null) {
                        if (getOrdersModel.getData().getOrders().size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.GONE);
                            orderListModel = getOrdersModel.getData().getOrders();
                            adapter.updateListItem(orderListModel);
                            dbTask.execute(orderListModel);
                            prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "0");
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    recyclerView.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getOrdersModel.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }

    public void getAllDeliveredOrderMethod() {
        if (Util.checkIntenetConnection(getActivity())) {
            getDeliveredOrder();
        } else {
            List<OrdersListModel> cancelOrders = appDatabase.getShippingOrders();
            if (cancelOrders != null) {
                adapter.updateListItem(cancelOrders);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
        }
    }


    private void getDeliveredOrder() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("api_key", "");
        param.put("order_type", "delivered");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {

            @Override
            public void success(GetOrdersModel getOrdersModel, Response response) {
                if (getOrdersModel.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getOrdersModel != null) {
                        if (getOrdersModel.getData().getOrders().size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.GONE);
                            orderListModel = getOrdersModel.getData().getOrders();
                            adapter.updateListItem(orderListModel);
                            dbTask.execute(orderListModel);
                            prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "0");
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    recyclerView.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getOrdersModel.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    /*Get all orders and Fileter on the basis of active shipped and delivered*/


    public void getOrdersActiveOrders() {
        if (Util.checkIntenetConnection(getActivity())) {
            getActiveOrders();
        } else {
            List<OrdersListModel> dueOrders = appDatabase.getProcessingOrders();
            if (dueOrders != null) {
                adapter.updateListItem(dueOrders);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
        }

    }

    /*Due Order module with Type CONSTANT.TYPE_*/

    public void getActiveOrders() {

        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "active");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getValues, Response response) {
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getValues.getData().getOrders().size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.GONE);
                        orderListModel = getValues.getData().getOrders();
                        adapter.updateListItem(orderListModel);
                        dbTask.execute(orderListModel);
                        prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "0");
                    } else {
                        recyclerView.setVisibility(View.GONE);
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

    public void getDueOrdersMethod() {
        if (Util.checkIntenetConnection(getActivity())) {
            getDueOrders();
        } else {
            List<OrdersListModel> dueOrders = appDatabase.getDueOrders();
            if (dueOrders != null) {
                adapter.updateListItem(dueOrders);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
        }
    }

    public void getDueOrders() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "pending");
        param.put("api_key", "");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getValues.getData().getOrders().size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.GONE);
                        orderListModel = getValues.getData().getOrders();
                        adapter.updateListItem(orderListModel);
                        dbTask.execute(orderListModel);
                        prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "0");
                    } else {
                        recyclerView.setVisibility(View.GONE);
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
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }



    private void filterResult(String statusDue, String statusActive, String statusShipped, String statusDelivered) {

        orderListModel.clear();
        for (OrdersListModel order : listOrderMain) {
            if (order.getStatus().equalsIgnoreCase(statusDue) || order.getStatus().equalsIgnoreCase(statusActive) ||
                    order.getStatus().equalsIgnoreCase(statusShipped) || order.getStatus().equalsIgnoreCase(statusDelivered)) {
                orderListModel.add(order);
            }
        }
        adapter.updateListItem(orderListModel);
    }


    private List<OrdersListModel> getSortedList(List<OrdersListModel> orders) {
        List<OrdersListModel> list = new ArrayList<>();
        for (OrdersListModel order : orders) {
            if (order.getStatus().equalsIgnoreCase("0") || order.getStatus().equalsIgnoreCase("1") || order.getStatus().equalsIgnoreCase("4") || order.getStatus().equalsIgnoreCase("5")) {
                list.add(order);
            }
        }
        return list;
    }


    class DbInsertUpdateTask extends AsyncTask<List<OrdersListModel>, Void, Void> {

        @Override
        protected Void doInBackground(List<OrdersListModel>... list) {
            appDatabase.setListOrders(list[0]);
            return null;
        }
    }


    @Override
    public void onItemClick(View itemView, int position, OrdersListModel order) {
        if (order != null) {
            Intent intent = null;
            if (type.equalsIgnoreCase(Constant.TYPE_REJECTED)) {
                intent = new Intent(getActivity(), RejectedItemsListActivity.class);
            } else if (type.equalsIgnoreCase(Constant.TYPE_CANCELLED)) {
                intent = new Intent(getActivity(), CanceledOrdersItemsListActivity.class);
            } else if (type.equalsIgnoreCase(Constant.TYPE_DELIVERED)) {
                intent = new Intent(getActivity(), DueOrderActivityWithoutUpdations.class);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("object", order);
            intent.putExtras(bundle);
            context.startActivity(intent);
            AnimUtil.slideFromRightAnim((Activity) context);
        }
    }

}
