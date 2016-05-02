package com.signity.shopkeeperapp.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.customer.CustomerFragment;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.model.DashBoardModel;
import com.signity.shopkeeperapp.model.DashBoardModelDetail;
import com.signity.shopkeeperapp.model.DashBoardModelStoreDetail;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.orders.ActiveOrderFragment;
import com.signity.shopkeeperapp.orders.DueOrderFragment;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    View fragmentView;
    TextView mOutstandingPayment, mDueOrders, mActiveOrders, mAllCustomers;
    TextView labelOutstandingPayment, labelDueOrders, labelActiveOrders, labelAllCutomers;
    ImageView imageDueOrders, imageActiveOrders, customerImageView;
    String[] titleText = {"Due Orders", "Active Orders", "Customers"};

    AppDatabase appDatabase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = DbAdapter.getInstance().getDb();
    }

    public MainActivityFragment() {
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                MainActivityFragment.class.getName());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initialize();

        return fragmentView;
    }

    private void initialize() {

        mOutstandingPayment = (TextView) fragmentView.findViewById(R.id.outstandingPayment);
        mOutstandingPayment.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));
        mDueOrders = (TextView) fragmentView.findViewById(R.id.dueOrders);
        mDueOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));
        mActiveOrders = (TextView) fragmentView.findViewById(R.id.activeOrder);
        mActiveOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));
        mAllCustomers = (TextView) fragmentView.findViewById(R.id.allCustomers);
        mAllCustomers.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_THIN));

        labelOutstandingPayment = (TextView) fragmentView.findViewById(R.id.labelOutStanding);
        labelOutstandingPayment.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));
        labelDueOrders = (TextView) fragmentView.findViewById(R.id.labelDueOrder);
        labelDueOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));
        labelActiveOrders = (TextView) fragmentView.findViewById(R.id.labelActiveOrder);
        labelActiveOrders.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));
        labelAllCutomers = (TextView) fragmentView.findViewById(R.id.labelAllCust);
        labelAllCutomers.setTypeface(FontUtil.getTypeface(getActivity(), FontUtil.FONT_ROBOTO_REGULAR));

        customerImageView = (ImageView) fragmentView.findViewById(R.id.imageView2);
        customerImageView.setOnClickListener(this);
        imageDueOrders = (ImageView) fragmentView.findViewById(R.id.imageDueOrders);
        imageDueOrders.setOnClickListener(this);
        imageActiveOrders = (ImageView) fragmentView.findViewById(R.id.imageActiveOrders);
        imageActiveOrders.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Util.checkIntenetConnection(getActivity())) {
            getDashBoardValues();
        } else {
            DashBoardModelDetail dashBoardModelDetail = appDatabase.getDashBoard();
//            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            setDashBoardValues(dashBoardModelDetail);
        }
    }

    public void getDashBoardValues() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("api_key", "123");

        NetworkAdaper.getInstance().getNetworkServices().getDashBoard(param, new Callback<DashBoardModel>() {
            @Override
            public void success(DashBoardModel getDashBoard, Response response) {
                Log.e("Tab", getDashBoard.toString());
                if (getDashBoard.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    appDatabase.setDashBoard(getDashBoard.getData());
                    saveStoreDetails(getDashBoard.getData().getStore());
                    setDashBoardValues(getDashBoard.getData());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getDashBoard.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }


    private void saveStoreDetails(DashBoardModelStoreDetail store) {

        try {
            Util.savePreferenceValue(getActivity(), Constant.STORE_DETAILS, getStoreDataAsString(store));
            Util.savePreferenceValue(getActivity(), Constant.STORE_STATUS_MESSAGE, store.getStoreMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setDashBoardValues(DashBoardModelDetail dashBoardValues) {

        mOutstandingPayment.setText(getActivity().getString(R.string.text_rs) + " " + dashBoardValues.getOutstanding());
        mDueOrders.setText("" + dashBoardValues.getDueOrders());
        mActiveOrders.setText("" + dashBoardValues.getActiveOrders());
        mAllCustomers.setText("" + dashBoardValues.getCustomers());

    }


    public String getStoreDataAsString(DashBoardModelStoreDetail store) {
        Gson gson = new Gson();
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        return gson.toJson(store, type);
    }

    public void replace(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }


    @Override
    public void onClick(View view) {

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        int id = view.getId();

        switch (id) {

            case R.id.imageDueOrders:
                com.signity.shopkeeperapp.home.MainActivity.fragmentName = titleText[0];
                title.setText(titleText[0]);
                replace(DueOrderFragment.newInstance(getActivity()));

                break;

            case R.id.imageActiveOrders:
                com.signity.shopkeeperapp.home.MainActivity.fragmentName = titleText[1];
                title.setText(titleText[1]);
                replace(ActiveOrderFragment.newInstance(getActivity()));
                break;

            case R.id.imageView2:
                MainActivity.fragmentName = titleText[2];
                title.setText(titleText[2]);
                replace(CustomerFragment.newInstance(getActivity()));
                break;
        }
    }
}
