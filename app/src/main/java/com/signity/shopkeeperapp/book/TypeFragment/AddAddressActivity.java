package com.signity.shopkeeperapp.book.TypeFragment;

import androidx.appcompat.app.AppCompatActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.customers.AreaCodesResp;
import com.signity.shopkeeperapp.model.customers.DataResp;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

public class AddAddressActivity extends AppCompatActivity {

    String address,area_name,city,state,zipcode;
    private ArrayAdapter<String> stringArrayAdapter;
    private List<String> areaList = new ArrayList<>();
    private List<DataResp> dataResps = new ArrayList<>();


    TextInputEditText editAddress,editAreaName,editCity,editState,editZipcode;
    LinearLayout save_address_lay;
    private Spinner spinner;
    private String areaId,areaName;

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

                ProgressDialogUtil.hideProgressDialog();
                areaList.clear();
                if (areaCodesResp.isSuccess()) {
                    dataResps = areaCodesResp.getData();

                    for (DataResp resp : dataResps) {
                        areaList.add(resp.getName());
                    }
                    Log.e("area_name",areaList.toString());
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
                Log.e("dataResps",dataResps.get(position).getName());

                Toast.makeText(AddAddressActivity.this, areaId+"-----"+areaName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void initViews()
    {
        editAddress = findViewById(R.id.edt_customer_address);
//        editAreaName = findViewById(R.id.edt_customer_address);
        editCity = findViewById(R.id.edt_customer_city);
        editState = findViewById(R.id.edt_customer_state);
        editZipcode = findViewById(R.id.edt_customer_zip);
        spinner = findViewById(R.id.spinner_area);


        save_address_lay = findViewById(R.id.edit_address_save);
        save_address_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("address",editAddress.getText().toString());
                intent.putExtra("area_name",areaName);
                intent.putExtra("city",editCity.getText().toString());
                intent.putExtra("state",editState.getText().toString());
                intent.putExtra("zipcode",editZipcode.getText().toString());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    void getExtras()
    {
        if(getIntent().getExtras() != null)
        {
            address = getIntent().getStringExtra("address");
            area_name = getIntent().getStringExtra("area_name");
            city = getIntent().getStringExtra("city");
            state = getIntent().getStringExtra("state");
            zipcode = getIntent().getStringExtra("zipcode");

            editAddress.setText(address);
            editCity.setText(city);
            editState.setText(state);
            editZipcode.setText(zipcode);
//            editAddress.setText(address);



        }
    }
}


