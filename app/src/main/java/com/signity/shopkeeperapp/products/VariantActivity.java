package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.StoreAttributes;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VariantActivity extends AppCompatActivity {

    public static final String VARIANT_DATA = "VARIANT_DATA";
    private static final String TAG = "VariantActivity";
    private Toolbar toolbar;
    private LinearLayout linearLayoutSave;
    private DynamicFieldAdapter dynamicFieldAdapter;
    private RecyclerView recyclerViewDynamicField;

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
        setUpAdapter();
        getStoreAttributes();
    }

    private void setUpAdapter() {
        dynamicFieldAdapter = new DynamicFieldAdapter(this);
        recyclerViewDynamicField.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDynamicField.setAdapter(dynamicFieldAdapter);
        recyclerViewDynamicField.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = (int) Util.pxFromDp(VariantActivity.this, 8);
                outRect.top = 0;
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        linearLayoutSave = findViewById(R.id.ll_save);
        recyclerViewDynamicField = findViewById(R.id.rv_dynamic_fields);

        linearLayoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveVariant();
            }
        });
    }

/*
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
*/

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

    private void getStoreAttributes() {
        NetworkAdaper.getNetworkServices().getStoreAttributes(new Callback<StoreAttributes>() {
            @Override
            public void success(StoreAttributes storeAttributes, Response response) {

                ProgressDialogUtil.hideProgressDialog();
                if (storeAttributes.isSuccess()) {
                    dynamicFieldAdapter.setDynamicFieldList(storeAttributes.getData().getDynamicFields());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }
}
