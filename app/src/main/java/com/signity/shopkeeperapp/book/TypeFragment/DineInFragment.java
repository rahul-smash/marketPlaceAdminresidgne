package com.signity.shopkeeperapp.book.TypeFragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.book.BookOrderCheckoutActivity;
import com.signity.shopkeeperapp.model.customers.addCustomer.AddCustomerResponse;
import com.signity.shopkeeperapp.model.orders.CustomerData;
import com.signity.shopkeeperapp.model.orders.Data;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DineInFragment extends Fragment {

    public static final String TAG = "DineInFragment";
    private TextInputEditText editTextMobileNumber, editTextName, editTextLoyaltyPoints, editTextEmail, editTextWaiter, editTextTableNumber, editTextPeople;
    private ImageView imageViewSearch;
    private String mobile;
    private LinearLayout linearLayoutNext;

    public static DineInFragment getInstance(Bundle bundle) {
        DineInFragment fragment = new DineInFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getExtra();
        checkNumber();
    }

    private void checkNumber() {

        if (TextUtils.isEmpty(mobile)) {
            return;
        }

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

    private void getExtra() {
        if (getArguments() != null) {
            mobile = getArguments().getString("NUMBER");
            editTextMobileNumber.setText(mobile);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        editTextMobileNumber = view.findViewById(R.id.edt_dinein_mobile);
        editTextName = view.findViewById(R.id.edt_dinein_name);
        editTextLoyaltyPoints = view.findViewById(R.id.edt_dinein_loyalty_points);
        editTextEmail = view.findViewById(R.id.edt_dinein_email);
        editTextWaiter = view.findViewById(R.id.edt_dinein_waiter);
        editTextTableNumber = view.findViewById(R.id.edt_dinein_table_number);
        editTextPeople = view.findViewById(R.id.edt_dinein_people);
        imageViewSearch = view.findViewById(R.id.iv_dinein_search);
        linearLayoutNext = view.findViewById(R.id.ll_next);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validMobile()) {
                    checkNumber();
                }
            }
        });

        editTextMobileNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    if (validMobile()) {
                        checkNumber();
                    }
                    return true;
                }
                return false;
            }
        });

        linearLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderCheckout();
            }
        });
    }

    private void orderCheckout() {

        String mobile = editTextMobileNumber.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "Mobile Number can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        getCustomerId(mobile);
    }

    private void getCustomerId(String mobile) {

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
                    if (customerData.getData() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ID, customerData.getData().getId());
                        startActivity(BookOrderCheckoutActivity.getIntent(getContext()));
                        AnimUtil.slideFromRightAnim(getActivity());
                    }
                } else {
                    addCustomer();
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

    private void addCustomer() {

        String mobile = editTextMobileNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "Mobile Number can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);
        param.put("name", name);
        param.put("email", email);
        param.put("address", "");
        param.put("area_id", "");
        param.put("area_name", "");
        param.put("city", "");
        param.put("state", "");
        param.put("zipcode", "");

        ProgressDialogUtil.showProgressDialog(getContext());
        NetworkAdaper.getNetworkServices().addCustomer(param, new Callback<AddCustomerResponse>() {
            @Override
            public void success(AddCustomerResponse addCategoryResponse, Response response) {

                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (addCategoryResponse.isSuccess()) {
                    if (addCategoryResponse.getData() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ID, addCategoryResponse.getData().getStoreUser().getUserId());
                        startActivity(BookOrderCheckoutActivity.getIntent(getContext()));
                        AnimUtil.slideFromRightAnim(getActivity());
                    }
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

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void populateFields(Data data) {

        if (data == null) {
            return;
        }

        editTextName.setText(data.getFullName());
        editTextEmail.setText(data.getEmail());
        editTextLoyaltyPoints.setText(String.valueOf(data.getLoyalityPoints()));
    }

    private boolean validMobile() {

        mobile = editTextMobileNumber.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "Mobile Number can't be empty", Toast.LENGTH_SHORT).show();
            editTextMobileNumber.requestFocus();
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dinein_type, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
