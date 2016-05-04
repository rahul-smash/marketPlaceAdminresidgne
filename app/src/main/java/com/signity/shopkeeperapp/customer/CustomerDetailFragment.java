package com.signity.shopkeeperapp.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.app.DbAdapter;
import com.signity.shopkeeperapp.db.AppDatabase;
import com.signity.shopkeeperapp.home.MainActivity;
import com.signity.shopkeeperapp.model.CustomerDetailModel;
import com.signity.shopkeeperapp.model.GetCustomerDetailModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rajinder on 28/9/15.
 */
public class CustomerDetailFragment extends Fragment {


    Button backButton, btnCall, btnActiveOrders;
    String name;
    String id;
    String address;
    String email;
    String phoneNumber;
    TextView mTotalOrderValue, mAmountPaidValue, mActiveOrderValue, mAmountDueValue;
    TextView mCustomerFullName, mCustomerAddress, mCustomerEmail, mCustomerContact;

    AppDatabase appDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        id = getArguments().getString("id");
        address = getArguments().getString("address");
        email = getArguments().getString("email");
        phoneNumber = getArguments().getString("phone");
        appDatabase = DbAdapter.getInstance().getDb();
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                CustomerDetailFragment.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_customer_detail, container, false);
        initialization(rootView);
        handleBackButton(rootView);
        handleCallButton(rootView);
        handleActiveOrderButton(rootView);
        setHeader(rootView);
        return rootView;
    }

    private void handleActiveOrderButton(View rootView) {


        ((Button) rootView.findViewById(R.id.btnShowActiveOrder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("Active", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                AnimUtil.slideFromLeftAnim(getActivity());

            }
        });
    }

    private void initialization(View rootView) {

        mCustomerFullName = (TextView) rootView.findViewById(R.id.txtName);
        mCustomerAddress = (TextView) rootView.findViewById(R.id.txtAddress);
        mCustomerEmail = (TextView) rootView.findViewById(R.id.txtEmail);
        mCustomerContact = (TextView) rootView.findViewById(R.id.txtContact);

        mTotalOrderValue = (TextView) rootView.findViewById(R.id.txtTotalOrderVal);
        mAmountPaidValue = (TextView) rootView.findViewById(R.id.txtAmoutPaidVal);
        mActiveOrderValue = (TextView) rootView.findViewById(R.id.txtActiveOrdersVal);
        mAmountDueValue = (TextView) rootView.findViewById(R.id.txtAmtDueVal);
    }

    private void handleCallButton(View rootView) {

        ((Button) rootView.findViewById(R.id.btnCall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAlert();
            }
        });
    }

    public void handleBackButton(View rootView) {
        ((Button) rootView.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                AnimUtil.slideFromLeftAnim(getActivity());

            }
        });
    }

    public void setHeader(View rootView) {
        if (name.equals(null) || name.equalsIgnoreCase("")) {
            ((TextView) rootView.findViewById(R.id.textTitle)).setText("Guest User");
        } else {
            ((TextView) rootView.findViewById(R.id.textTitle)).setText(name);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (name.equals(null) || name.equalsIgnoreCase("")) {
            mCustomerFullName.setText("Guest User");
        } else {
            mCustomerFullName.setText(name);
        }

        if (address.equals(null) || address.equalsIgnoreCase("")) {
            mCustomerAddress.setText("");
        } else {
            mCustomerAddress.setText("" + address);
        }

        if (email.equals(null) || email.equalsIgnoreCase("")) {
            mCustomerEmail.setText("");
        } else {
            mCustomerEmail.setText(email);
        }

        if (phoneNumber.equals(null) || phoneNumber.equalsIgnoreCase("")) {
            mCustomerContact.setText("");
        } else {
            mCustomerContact.setText(phoneNumber);
        }


        if (Util.checkIntenetConnection(getActivity())) {
            getCutomerDetails();
        } else {
            CustomerDetailModel customerDetailModel = appDatabase.getCustomerDetailModel(id);
            if (customerDetailModel != null && customerDetailModel.getPaidAmount() != null && customerDetailModel.getDueAmount() != null) {
                setCutomerDetailValues(customerDetailModel);
            } else {
                DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            }
            Log.e("TAG", "No internet option");
        }
    }

    public void getCutomerDetails() {

        ProgressDialogUtil.showProgressDialog(getActivity());

        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", id);

        NetworkAdaper.getInstance().getNetworkServices().getCustomerDetail(param, new Callback<GetCustomerDetailModel>() {
            @Override
            public void success(GetCustomerDetailModel getValues, Response response) {
                Log.e("Tab", getValues.toString());
                if (getValues.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    appDatabase.updateCustomer(getValues.getData());
                    setCutomerDetailValues(getValues.getData());
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                    DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, getValues.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                Log.e("Retrofit-Error", error.getMessage());
                DialogUtils.showAlertDialog(getActivity(), Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });

    }

    public void setCutomerDetailValues(CustomerDetailModel cutomerDetailValues) {
        mTotalOrderValue.setText("" + cutomerDetailValues.getTotalOrders());
        mAmountPaidValue.setText(Util.getCurrency(getActivity()) + " " + cutomerDetailValues.getPaidAmount());
        mActiveOrderValue.setText(Util.getCurrency(getActivity()) + " " + cutomerDetailValues.getActiveOrders());
        mAmountDueValue.setText(Util.getCurrency(getActivity()) + " " + cutomerDetailValues.getDueAmount());
    }

    private void callAlert() {

        android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(getActivity());


        adb.setTitle("Call " + phoneNumber + " ?");


        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                actionCall();


            }
        });


        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    private void actionCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
        AnimUtil.slideFromLeftAnim(getActivity());
    }
}
