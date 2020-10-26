package com.signity.shopkeeperapp.products;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.Product.DynamicField;
import com.signity.shopkeeperapp.model.Product.StoreAttributes;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VariantActivity extends BaseActivity {

    public static final String VARIANT_DATA = "VARIANT_DATA";
    private static final String TAG = "VariantActivity";
    private Toolbar toolbar;
    private List<DynamicField> dynamicFieldList = new ArrayList<>();
    private VariantFragment variantFragment;

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
        getStoreAttributes();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

        findViewById(R.id.ll_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVariant();
            }
        });
    }

    private void saveVariant() {

        Map<String, String> fieldMap = variantFragment != null ? variantFragment.getVariantMap() : new HashMap<String, String>();

        if (fieldMap.isEmpty()) {
            Toast.makeText(this, "Please add variant details", Toast.LENGTH_SHORT).show();
            return;
        }

        for (DynamicField dynamicField : dynamicFieldList) {
            if (dynamicField.getValidation().equalsIgnoreCase("true")) {
                if (fieldMap.containsKey(dynamicField.getVariantFieldName())) {
                    if (TextUtils.isEmpty(fieldMap.get(dynamicField.getVariantFieldName()))) {
                        Toast.makeText(this, String.format("%s is empty", dynamicField.getLabel()), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dynamicField.getVariantFieldName().equalsIgnoreCase("custom_field2")) {
                        String minStocks = fieldMap.get("custom_field2");
                        String stocks = fieldMap.get("custom_field1");

                        try {
                            int stock = Integer.parseInt(stocks);
                            int minStock = Integer.parseInt(minStocks);

                            if (minStock >= stock) {
                                Toast.makeText(this, "Min stock should be less than stock", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(this, String.format("%s is empty", dynamicField.getLabel()), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        Intent intent = new Intent();
        intent.putExtra(VariantActivity.VARIANT_DATA, (Serializable) fieldMap);
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

    private void getStoreAttributes() {
        NetworkAdaper.getNetworkServices().getStoreAttributes(new Callback<StoreAttributes>() {
            @Override
            public void success(StoreAttributes storeAttributes, Response response) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (storeAttributes.isSuccess()) {
                    if (storeAttributes.getData() != null) {
                        dynamicFieldList = storeAttributes.getData().getDynamicFields();
                        setVariantFragment();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(VariantActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariantFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(VariantFragment.DYNAMIC_LIST, (ArrayList<? extends Parcelable>) dynamicFieldList);
        variantFragment = VariantFragment.getInstance(bundle);
        fragmentTransaction.replace(R.id.variant_frame, variantFragment, VariantFragment.TAG);
        fragmentTransaction.commit();
    }
}
