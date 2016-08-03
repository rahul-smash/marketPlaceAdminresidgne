package com.signity.shopkeeperapp.orders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.RvActiveOrderAdapter;
import com.signity.shopkeeperapp.adapter.RvGridSpacesItemDecoration;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
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
public class AllOrderFragment extends Fragment implements RvActiveOrderAdapter.OnItemClickListener {

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

    private RelativeLayout parent;

    FloatingActionButton fab;
    private String isRefreshRequire;
    boolean isForDueOrderOnly = false;
    boolean isForActiveOrderOnly = false;
    boolean isForALLOrderOnly = false;


    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                AllOrderFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRefreshRequire = "0";
        type = getArguments().getString("type");
        context = getActivity();
        prefManager = new PrefManager(getActivity());
        appDatabase = DbAdapter.getInstance().getDb();
//        orderListModel = appDatabase.getOrders();
        if (orderListModel == null) {
            orderListModel = new ArrayList<>();
        }
        prefManager.storeSharedValue(Constant.REFERESH_DATA_REQURIED, "0");
        if (type.equalsIgnoreCase(Constant.TYPE_DUE_ORDER)) {
            isForDueOrderOnly = true;
        } else if (type.equalsIgnoreCase(Constant.TYPE_ACTIVE_ORDER)) {
            isForActiveOrderOnly = true;
        } else {
            isForALLOrderOnly = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_all_order, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        parent = (RelativeLayout) rootView.findViewById(R.id.parent);
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
        getAllOrdersMethod();
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopupForFilter();
            }
        });
        return rootView;
    }


    /*Get all orders and Fileter on the basis of active shipped and delivered*/

    public void getAllOrdersMethod() {
        if (Util.checkIntenetConnection(getActivity())) {
            getActiveOrders();
        } else {
            listOrderMain = appDatabase.getOrders();
            orderListModel = getSortedListByType(appDatabase.getOrders());
            if (orderListModel != null) {
                adapter.updateListItem(orderListModel);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isRefreshRequire = prefManager.getSharedValue(Constant.REFERESH_DATA_REQURIED);
        if (!isRefreshRequire.isEmpty() && isRefreshRequire.equalsIgnoreCase("1")) {
            getAllOrdersMethod();
        }
    }

    public void getActiveOrders() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, String> param = new HashMap<String, String>();
        param.put("order_type", "all");

        NetworkAdaper.getInstance().getNetworkServices().getStoreOrders(param, new Callback<GetOrdersModel>() {
            @Override
            public void success(GetOrdersModel getValues, Response response) {
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (getValues.getData().getOrders().size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.GONE);
                        listOrderMain = getSortedList(getValues.getData().getOrders());
                        orderListModel = getSortedListByType(getValues.getData().getOrders());
                        adapter.updateListItem(orderListModel);
                        dbTask = new DbInsertUpdateTask();
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


    public void openPopupForFilter() {

        final CheckBox checkboxShippedOrder, checkboxActiveOrders, checkboxDueOrders, checkboxAllOrders;
        Button apply;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView
                = inflater.inflate(R.layout.layout_popup_filter_order,
                null);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        checkboxAllOrders = (CheckBox) dialogView.findViewById(R.id.checkboxAllOrders);
        checkboxShippedOrder = (CheckBox) dialogView.findViewById(R.id.checkboxShippedOrder);
        checkboxActiveOrders = (CheckBox) dialogView.findViewById(R.id.checkboxActiveOrders);
        checkboxDueOrders = (CheckBox) dialogView.findViewById(R.id.checkboxDueOrders);
        /*checkboxDueOrders.setTag("");
        checkboxActiveOrders.setTag("");
        checkboxShippedOrder.setTag("");
        checkboxAllOrders.setTag("");*/

        checkboxDueOrders.setTag("0");
        checkboxActiveOrders.setTag("1");
        checkboxShippedOrder.setTag("4");
        checkboxAllOrders.setTag("all");
        checkboxDueOrders.setChecked(true);
        checkboxActiveOrders.setChecked(true);
        checkboxShippedOrder.setChecked(true);
        checkboxAllOrders.setChecked(true);

        apply = (Button) dialogView.findViewById(R.id.buttonApply);

        checkboxDueOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (checkboxActiveOrders.isChecked() && checkboxShippedOrder.isChecked()) {
                        checkboxAllOrders.setChecked(true);
                    }
                    checkboxDueOrders.setTag("0");
                } else {
                    if (checkboxActiveOrders.isChecked() && checkboxShippedOrder.isChecked()) {
                        checkboxAllOrders.setChecked(false);
                    }
                    checkboxDueOrders.setTag("");
                }
            }
        });
        checkboxActiveOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if ((checkboxDueOrders.isChecked() && checkboxShippedOrder.isChecked())) {
                        checkboxAllOrders.setChecked(true);
                    }
                    checkboxActiveOrders.setTag("1");
                } else {
                    if ((checkboxDueOrders.isChecked() && checkboxShippedOrder.isChecked())) {
                        checkboxAllOrders.setChecked(false);
                    }
                    checkboxActiveOrders.setTag("");
                }
            }
        });
        checkboxShippedOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if ((checkboxActiveOrders.isChecked() && checkboxDueOrders.isChecked())) {
                        checkboxAllOrders.setChecked(true);
                    }
                    checkboxShippedOrder.setTag("4");
                } else {
                    if ((checkboxActiveOrders.isChecked() && checkboxDueOrders.isChecked())) {
                        checkboxAllOrders.setChecked(false);
                    }
                    checkboxShippedOrder.setTag("");
                }
            }
        });
        checkboxAllOrders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkboxAllOrders.setTag("all");
                    checkboxDueOrders.setChecked(true);
                    checkboxActiveOrders.setChecked(true);
                    checkboxShippedOrder.setChecked(true);
                } else {
                    checkboxAllOrders.setTag("");
                    checkboxDueOrders.setChecked(false);
                    checkboxActiveOrders.setChecked(false);
                    checkboxShippedOrder.setChecked(false);
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusAll = (String) checkboxAllOrders.getTag();
                String statusDue = (String) checkboxDueOrders.getTag();
                String statusActive = (String) checkboxActiveOrders.getTag();
                String statusShipped = (String) checkboxShippedOrder.getTag();

                if (statusDue.isEmpty() && statusActive.isEmpty() && statusShipped.isEmpty() && statusAll.isEmpty()
                        ) {
                    Toast.makeText(context, "Empty Selection", Toast.LENGTH_SHORT).show();
                    return;
                }
                filterResult(statusDue, statusActive, statusShipped, statusAll);
                dialog.dismiss();
            }
        });

        dialog.show();

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

    private List<OrdersListModel> getSortedListByType(List<OrdersListModel> orders) {
        List<OrdersListModel> list = new ArrayList<>();
        if (isForALLOrderOnly) {
            list = getSortedList(orders);
            return list;
        }
        if (isForDueOrderOnly) {
            for (OrdersListModel order : orders) {
                if (order.getStatus().equalsIgnoreCase("0")) {
                    list.add(order);
                }
            }
        }
        if (isForActiveOrderOnly) {
            for (OrdersListModel order : orders) {
                if (order.getStatus().equalsIgnoreCase("1")) {
                    list.add(order);
                }
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
            if (type.equalsIgnoreCase(Constant.TYPE_ALL_ORDER) ||
                    type.equalsIgnoreCase(Constant.TYPE_DUE_ORDER) ||
                    type.equalsIgnoreCase(Constant.TYPE_ACTIVE_ORDER)) {
                if (order.getStatus().equalsIgnoreCase("0")) {
                    intent = new Intent(context, DueOrderActivity.class);
                } else {
                    intent = new Intent(context, DueOrderActivityWithoutUpdations.class);
                }
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("object", order);
            intent.putExtras(bundle);
            context.startActivity(intent);
            AnimUtil.slideFromRightAnim((Activity) context);
        }
    }

}
