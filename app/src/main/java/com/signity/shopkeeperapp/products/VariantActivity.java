package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

public class VariantActivity extends AppCompatActivity {

    public static final String STORES_LIST = "STORES_LIST";
    private static final String TAG = "VariantActivity";
    private Toolbar toolbar;
    private TextInputEditText editTextWeight;
    private TextInputEditText editTextUnitType;
    private TextInputEditText editTextMRP;
    private TextInputEditText editTextSellingPrice;
    private TextInputEditText editTextDiscount;
    private LinearLayout linearLayoutSave;

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
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        editTextWeight = findViewById(R.id.edt_weight);
        editTextUnitType = findViewById(R.id.edt_unit);
        editTextMRP = findViewById(R.id.edt_mrp);
        editTextDiscount = findViewById(R.id.edt_discount);
        editTextSellingPrice = findViewById(R.id.edt_selling_price);
        linearLayoutSave = findViewById(R.id.ll_save);

        linearLayoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - Finish and send data
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            AnimUtil.slideFromLeftAnim(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
