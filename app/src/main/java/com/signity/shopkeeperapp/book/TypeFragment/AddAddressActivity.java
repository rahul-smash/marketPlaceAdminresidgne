package com.signity.shopkeeperapp.book.TypeFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.AddAddressModel;
import com.signity.shopkeeperapp.model.customers.AreaCodesResp;
import com.signity.shopkeeperapp.model.customers.DataResp;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddAddressActivity extends AppCompatActivity {

    String address, area_name, area_id, city, state, zipcode, customerId, customerMobile, customerFirstName, customerAddressId;
    ImageView back;
    TextInputEditText editAddress, editAreaName, editCity, editState, editZipcode;
    LinearLayout save_address_lay;
    private ArrayAdapter<String> stringArrayAdapter;
    private List<String> areaList = new ArrayList<>();
    private List<DataResp> dataResps = new ArrayList<>();
    private Spinner spinner;
    private String areaId, areaName;
    private String deliveryCharges, minAmount;
    private boolean isNotAllow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initViews();
        setUpSpinner();
        getAreaCodes();
        getExtras();
    }

    private void getAreaCodes() {

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().getAreaCodes(new Callback<AreaCodesResp>() {
            @Override
            public void success(AreaCodesResp areaCodesResp, Response response) {


                int position = 0;
                int selected_index = 0;

                ProgressDialogUtil.hideProgressDialog();
                areaList.clear();
                if (areaCodesResp.isSuccess()) {
                    dataResps = areaCodesResp.getData();

                    for (DataResp resp : dataResps) {
                        areaList.add(resp.getName());
                        if (!TextUtils.isEmpty(area_id)) {
                            if (area_id.equals(resp.getId())) {
                                selected_index = position;
                            }
                        }
                        position++;

                    }

                    spinner.setSelection(selected_index);

                    Log.e("area_name", areaList.toString());
                    stringArrayAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void failure(RetrofitError error) {

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
                deliveryCharges = dataResps.get(position).getCharges();
                isNotAllow = dataResps.get(position).isNotAllow();
                minAmount = dataResps.get(position).getMinamount();
                Log.e("dataResps", dataResps.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void initViews() {
        editAddress = findViewById(R.id.edt_customer_address);
//        editAreaName = findViewById(R.id.edt_customer_address);
        editCity = findViewById(R.id.edt_customer_city);
        editState = findViewById(R.id.edt_customer_state);
        editZipcode = findViewById(R.id.edt_customer_zip);
        spinner = findViewById(R.id.spinner_area);

        back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_address_lay = findViewById(R.id.edit_address_save);
        save_address_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(customerId)) {
                    if (TextUtils.isEmpty(editAddress.getText().toString()) || TextUtils.isEmpty(areaName) ||
                            TextUtils.isEmpty(editCity.getText().toString()) || TextUtils.isEmpty(editState.getText().toString())
                            || TextUtils.isEmpty(editZipcode.getText().toString())) {
                        Constant.showToast(AddAddressActivity.this, "Please enter the empty fields.");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("address", editAddress.getText().toString());
                    intent.putExtra("area_name", areaName);
                    intent.putExtra("area_id", areaId);
                    intent.putExtra("city", editCity.getText().toString());
                    intent.putExtra("state", editState.getText().toString());
                    intent.putExtra("zipcode", editZipcode.getText().toString());
                    intent.putExtra("charges", deliveryCharges);
                    intent.putExtra("isNotAllow", isNotAllow);
                    intent.putExtra("minAmount", minAmount);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    if (!TextUtils.isEmpty(customerAddressId)) {
                        addAddress("EDIT", customerAddressId);
                    } else {
                        addAddress("ADD", "");
                    }

                }


            }
        });
    }

    void getExtras() {
        if (getIntent().getExtras() != null) {
            address = getIntent().getStringExtra("address");
            area_name = getIntent().getStringExtra("area_name");
            area_id = getIntent().getStringExtra("area_id");

            city = getIntent().getStringExtra("city");
            state = getIntent().getStringExtra("state");
            zipcode = getIntent().getStringExtra("zipcode");

            customerId = getIntent().getStringExtra("customer_id");
            customerFirstName = getIntent().getStringExtra("customer_first_name");
            customerMobile = getIntent().getStringExtra("customer_mobile");
            customerAddressId = getIntent().getStringExtra("customer_address_id");


            editAddress.setText(address);
            editCity.setText(city);
            editState.setText(state);
            editZipcode.setText(zipcode);
//            editAddress.setText(address);
        }
    }

    public void addAddress(String method, String addressId) {
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", customerId);
        param.put("method", method);
        param.put("first_name", customerFirstName);
        param.put("mobile", customerMobile);
        param.put("address", editAddress.getText().toString());
        param.put("area_name", areaName);
        param.put("area_id", areaId);
        param.put("state", editState.getText().toString());
        param.put("city", editCity.getText().toString());
        param.put("zipcode", editZipcode.getText().toString());
        param.put("address_id", addressId);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().addAddressForDelivery(param, new Callback<AddAddressModel>() {
            @Override
            public void success(AddAddressModel responseBody, Response response) {
                ProgressDialogUtil.hideProgressDialog();

                if (responseBody.isSuccess()) {
                    Log.e("Add Delivery response", response.toString());
                    Log.e("Add Delivery response", responseBody.getMessage());
                    Constant.showToast(AddAddressActivity.this, responseBody.getMessage());
                    Intent intent = new Intent();
                    intent.putExtra("address", editAddress.getText().toString());
                    intent.putExtra("area_name", areaName);
                    intent.putExtra("area_id", areaId);
                    intent.putExtra("city", editCity.getText().toString());
                    intent.putExtra("state", editState.getText().toString());
                    intent.putExtra("zipcode", editZipcode.getText().toString());
                    intent.putExtra("charges", deliveryCharges);
                    intent.putExtra("isNotAllow", isNotAllow);
                    intent.putExtra("minAmount", minAmount);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Constant.showToast(AddAddressActivity.this, responseBody.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                Constant.showToast(AddAddressActivity.this, "Something went wrong");
                Log.e("Add address", "failure: " + error.getMessage());
            }
        });
    }
}


