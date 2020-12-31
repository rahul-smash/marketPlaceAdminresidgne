package com.signity.shopkeeperapp.customers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.customers.AreaCodesResp;
import com.signity.shopkeeperapp.model.customers.DataResp;
import com.signity.shopkeeperapp.model.customers.addCustomer.AddCustomerResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddCustomerActivity extends BaseActivity {

    private static final String TAG = "AddCustomerActivity";
    private Toolbar toolbar;
    private Spinner spinner;
    private TextInputEditText textInputEditTextState, textInputEditTextMobile, textInputEditTextName, textInputEditTextEmail, textInputEditTextAddress, textInputEditTextCity, textInputEditTextZip;
    private String areaId;
    private List<String> areaList = new ArrayList<>();
    private ArrayAdapter<String> stringArrayAdapter;
    private List<DataResp> dataResps = new ArrayList<>();
    private String areaName;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AddCustomerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        initViews();
        setUpToolbar();
        setUpSpinner();
//        getAreaCodes();
    }

    private void getAreaCodes() {

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().getAreaCodes(new Callback<AreaCodesResp>() {
            @Override
            public void success(AreaCodesResp areaCodesResp, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                areaList.clear();
                if (areaCodesResp.isSuccess()) {
                    dataResps = areaCodesResp.getData();
                    for (DataResp resp : dataResps) {
                        areaList.add(resp.getName());
                    }
                    stringArrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
            }
        });

    }

    private void setUpSpinner() {
        stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, areaList);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaId = dataResps.get(position).getId();
                areaName = dataResps.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textInputEditTextName = findViewById(R.id.edt_customer_name);
        textInputEditTextMobile = findViewById(R.id.edt_customer_mobile);
        textInputEditTextEmail = findViewById(R.id.edt_customer_email);
        textInputEditTextAddress = findViewById(R.id.edt_customer_address);
        textInputEditTextCity = findViewById(R.id.edt_customer_city);
        textInputEditTextState = findViewById(R.id.edt_customer_state);
        textInputEditTextZip = findViewById(R.id.edt_customer_zip);
        spinner = findViewById(R.id.spinner_area);

        findViewById(R.id.ll_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomer();
            }
        });
    }

    private void saveCustomer() {

        String mobile = textInputEditTextMobile.getText().toString().trim();
        String name = textInputEditTextName.getText().toString().trim();
        String email = textInputEditTextEmail.getText().toString().trim();
        String address = textInputEditTextAddress.getText().toString().trim();
        String city = textInputEditTextCity.getText().toString().trim();
        String state = textInputEditTextState.getText().toString().trim();
        String zip = textInputEditTextZip.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            textInputEditTextMobile.setError("Enter mobile");
            textInputEditTextMobile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            textInputEditTextName.setError("Enter name");
            textInputEditTextName.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(email) && !Util.checkValidEmail(email)) {
            textInputEditTextEmail.setError("Enter valid email");
            textInputEditTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            textInputEditTextAddress.setError("Enter Address");
            textInputEditTextAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(city)) {
            textInputEditTextCity.setError("Enter City");
            textInputEditTextCity.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(state)) {
            textInputEditTextCity.setError("Enter State");
            textInputEditTextCity.requestFocus();
            return;
        }


        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);
        param.put("name", name);
        param.put("email", email);
        param.put("address", address);
//        param.put("area_id", areaId);
//        param.put("area_name", areaName);
        param.put("city", city);
        param.put("state", state);
        param.put("zipcode", zip);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().addCustomer(param, new Callback<AddCustomerResponse>() {
            @Override
            public void success(AddCustomerResponse addCategoryResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (addCategoryResponse.isSuccess()) {
                    runAnimation();
                }
                Toast.makeText(AddCustomerActivity.this, addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void runAnimation() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
            runAnimation();
        }
        return super.onOptionsItemSelected(item);
    }
}
