package com.signity.shopkeeperapp.book.TypeFragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.customers.detail.CustomerAddressResponse;
import com.signity.shopkeeperapp.model.orders.CustomerData;
import com.signity.shopkeeperapp.model.orders.Data;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeliveryFragment extends Fragment {

    public static final String TAG = "DeliveryFragment";
    private TextInputEditText editTextMobileNumber, editTextName, editTextLoyaltyPoints, editTextEmail;
    private TextView textViewCustomerName, textViewCustomerNumber, textViewCustomerAddress, textViewAddressType;
    private TextInputEditText editTextScheduleDate, editTextScheduleTime, editTextDescription;
    private ImageView imageViewSearch;
    private LinearLayout linearLayoutAddDelivery, linearLayoutDeliveryAddress;
    private String mobile;

    public static DeliveryFragment getInstance(Bundle bundle) {
        DeliveryFragment fragment = new DeliveryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        editTextMobileNumber = view.findViewById(R.id.edt_delivery_mobile);
        editTextName = view.findViewById(R.id.edt_delivery_name);
        editTextLoyaltyPoints = view.findViewById(R.id.edt_delivery_loyalty_points);
        editTextEmail = view.findViewById(R.id.edt_delivery_email);
        editTextScheduleDate = view.findViewById(R.id.edt_delivery_date);
        editTextScheduleTime = view.findViewById(R.id.edt_delivery_time);
        editTextDescription = view.findViewById(R.id.edt_delivery_description);
        imageViewSearch = view.findViewById(R.id.iv_delivery_search);
        linearLayoutDeliveryAddress = view.findViewById(R.id.ll_delivery);
        linearLayoutAddDelivery = view.findViewById(R.id.ll_add_address);

        textViewCustomerName = view.findViewById(R.id.tv_customer_name);
        textViewCustomerNumber = view.findViewById(R.id.tv_customer_number);
        textViewCustomerAddress = view.findViewById(R.id.tv_customer_address);
        textViewAddressType = view.findViewById(R.id.tv_address_type);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validMobile()) {
                    checkNumber();
                }
            }
        });

        linearLayoutAddDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - Open dialog to add address
            }
        });
    }

    private void checkNumber() {

        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);

        ProgressDialogUtil.showProgressDialog(getContext());
        NetworkAdaper.getNetworkServices().checkNumber(param, new Callback<CustomerData>() {
            @Override
            public void success(CustomerData customerData, Response response) {

                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (customerData.isSuccess()) {
                    populateFields(customerData.getData());
                } else {
                    Toast.makeText(getContext(), customerData.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void populateFields(Data data) {

        if (data == null) {
            return;
        }

        editTextName.setText(data.getFullName());
        editTextEmail.setText(data.getEmail());
        editTextLoyaltyPoints.setText(String.valueOf(data.getLoyalityPoints()));
        if (data.getCustomerAddress() != null && !data.getCustomerAddress().isEmpty()) {
            linearLayoutAddDelivery.setVisibility(View.GONE);
            linearLayoutDeliveryAddress.setVisibility(View.VISIBLE);

            CustomerAddressResponse response = data.getCustomerAddress().get(0);

            textViewCustomerName.setText(response.getFirstName());
            textViewCustomerNumber.setText(response.getMobile());
            textViewCustomerAddress.setText(String.format("%s, %s", response.getAddress(), response.getCity()));
            textViewAddressType.setText("Home");
        }

    }

    private boolean validMobile() {

        mobile = editTextMobileNumber.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "Number can't be empty", Toast.LENGTH_SHORT).show();
            editTextMobileNumber.requestFocus();
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery_type, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
