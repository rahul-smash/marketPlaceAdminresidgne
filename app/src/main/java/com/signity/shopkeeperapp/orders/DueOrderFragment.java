package com.signity.shopkeeperapp.orders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.DueOrderAdapter;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajinder on 28/9/15.
 */
public class DueOrderFragment extends Fragment {

    ListView listDueOrders;
    DueOrderAdapter adapter;
    TextView noDataFound;
    AppDatabase appDatabase;

    ImageView btnOrderProceed, btnMoveToShipping, btnMoveToDeliver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = DbAdapter.getInstance().getDb();
    }

    public static Fragment newInstance(Context context) {
        Bundle args = new Bundle();
        return Fragment.instantiate(context,
                DueOrderFragment.class.getName(), args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layout_order_list, container, false);
        listDueOrders = (ListView) rootView.findViewById(R.id.listDueOrders);
        noDataFound = (TextView) rootView.findViewById(R.id.noDataFound);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getDueOrdersMethod();

    }

    @Override
    public void onResume() {
        super.onResume();
        getDueOrdersMethod();
    }

    public void getDueOrdersMethod() {
        if (Util.checkIntenetConnection(getActivity())) {
            getDueOrders();
        } else {
            List<OrdersListModel> dueOrders = appDatabase.getDueOrders();
            if (dueOrders != null) {
                adapter = new DueOrderAdapter(getActivity(), dueOrders,
                        getFragmentManager());
                listDueOrders.setAdapter(adapter);
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
                        noDataFound.setVisibility(View.GONE);
                        adapter = new DueOrderAdapter(getActivity(), getValues.getData().getOrders(),
                                getFragmentManager());
                        listDueOrders.setAdapter(adapter);
                        listDueOrders.setVisibility(View.VISIBLE);
                        appDatabase.setListOrders(getValues.getData().getOrders());
                    } else {
                        listDueOrders.setVisibility(View.GONE);
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
}
