package com.signity.shopkeeperapp.products;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.util.AnimUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VariantActivity extends AppCompatActivity {

    public static final String VARIANT_DATA = "VARIANT_DATA";
    private static final String TAG = "VariantActivity";
    private Toolbar toolbar;
    private TextInputEditText editTextWeight;
    private AppCompatSpinner appCompatSpinnerUnitType;
    private TextInputEditText editTextMRP;
    private TextInputEditText editTextSellingPrice;
    private TextInputEditText editTextDiscount;
    private LinearLayout linearLayoutSave;
    private List<String> unitList = new ArrayList<>(Arrays.asList("Kg", "gram", "Ltr", "ml"));
    private String unitType;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, VariantActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, VariantActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variant);
        initViews();
        setUpToolbar();
        setUpSpinner();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        editTextWeight = findViewById(R.id.edt_weight);
        appCompatSpinnerUnitType = findViewById(R.id.spinner_unit_type);
        editTextMRP = findViewById(R.id.edt_mrp);
        editTextDiscount = findViewById(R.id.edt_discount);
        editTextSellingPrice = findViewById(R.id.edt_selling_price);
        linearLayoutSave = findViewById(R.id.ll_save);

        editTextDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }

                String mrp = editTextMRP.getText().toString();

                if (TextUtils.isEmpty(mrp)) {
                    mrp = "0";
                }

                double doubleMrp = Double.parseDouble(mrp);
                double discount = Double.parseDouble(s.toString());

                if (discount < 0 || discount > 100) {
                    Toast.makeText(VariantActivity.this, "Invalid discount value", Toast.LENGTH_SHORT).show();
                    return;
                }

                final double price = doubleMrp - (doubleMrp * (discount / 100));
                editTextSellingPrice.setText(String.format("%.2f", price));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        linearLayoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVariant();
            }
        });
    }

    private void setUpSpinner() {
        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, unitList);
        appCompatSpinnerUnitType.setAdapter(stringArrayAdapter);
        appCompatSpinnerUnitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitType = stringArrayAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveVariant() {
        String weight = editTextWeight.getText().toString().trim();
        String mrp = editTextMRP.getText().toString().trim();
        String discount = editTextDiscount.getText().toString().trim();
        String price = editTextSellingPrice.getText().toString().trim();


        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Please enter product weight", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(unitType)) {
            Toast.makeText(this, "Please select unit type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mrp)) {
            Toast.makeText(this, "Please enter product MRP", Toast.LENGTH_SHORT).show();
            return;
        }

        Variant variant = new Variant();
        variant.setWeight(weight);
        variant.setUnitType(unitType);
        variant.setMrpPrice(mrp);
        variant.setDiscount(TextUtils.isEmpty(discount) ? "0" : discount);
        variant.setPrice(price);

        Intent intent = new Intent();
        intent.putExtra(VariantActivity.VARIANT_DATA, variant);
        setResult(Activity.RESULT_OK, intent);
        finishAnimate();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishAnimate();
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishAnimate() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }
}
