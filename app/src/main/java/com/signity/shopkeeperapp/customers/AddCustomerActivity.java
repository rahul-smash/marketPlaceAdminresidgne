package com.signity.shopkeeperapp.customers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.category.AddCategoryResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddCustomerActivity extends BaseActivity {

    private static final String TAG = "AddCustomerActivity";
    private Toolbar toolbar;
    private TextInputEditText textInputEditTextMobile, textInputEditTextName, textInputEditTextEmail, textInputEditTextAddress, textInputEditTextCity, textInputEditTextArea, textInputEditTextZip;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AddCustomerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        initViews();
        setUpToolbar();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textInputEditTextName = findViewById(R.id.edt_customer_name);
        textInputEditTextMobile = findViewById(R.id.edt_customer_mobile);
        textInputEditTextEmail = findViewById(R.id.edt_customer_email);
        textInputEditTextAddress = findViewById(R.id.edt_customer_address);
        textInputEditTextCity = findViewById(R.id.edt_customer_city);
        textInputEditTextArea = findViewById(R.id.edt_customer_area);
        textInputEditTextZip = findViewById(R.id.edt_customer_zip);

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
        String area = textInputEditTextArea.getText().toString().trim();
        String city = textInputEditTextCity.getText().toString().trim();
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

        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);
        param.put("name", name);
        param.put("email", email);
        param.put("address", address);
        param.put("area", area);
        param.put("city", city);
        param.put("zip", zip);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().addCustomer(param, new Callback<AddCategoryResponse>() {
            @Override
            public void success(AddCategoryResponse addCategoryResponse, Response response) {

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
            runAnimation();
        }
        return super.onOptionsItemSelected(item);
    }
}
