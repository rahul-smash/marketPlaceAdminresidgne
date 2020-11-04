package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class BookOrderCheckoutActivity extends BaseActivity {
    private static final String TAG = "BookOrderCheckoutActivity";
    private Toolbar toolbar;
    private BookOrderCheckoutAdapter bookOrderCheckoutAdapter;
    private PaymentModeAdapter paymentModeAdapter;
    private RecyclerView recyclerViewOrders;
    private RecyclerView recyclerViewPaymentMode;
    private TextView textViewCount;

    public static Intent getIntent(Context context) {
        return new Intent(context, BookOrderCheckoutActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order_checkout);
        initViews();
        setUpToolbar();
        setUpAdapter();
        populateData();
    }

    private void populateData() {
        List<GetProductData> list = new ArrayList<>();
        if (!OrderCart.isCartEmpty()) {
            list.addAll(OrderCart.getOrderCartMap().values());
        }
        bookOrderCheckoutAdapter.setProductData(list);
        String item = OrderCart.getOrderCartMap().size() > 1 ? "Items" : "Item";
        textViewCount.setText(String.format("%s %s", OrderCart.getOrderCartMap().size(), item));

        List<String> paymentModels = new ArrayList<>();
        paymentModels.add("Paytm");
        paymentModels.add("Credit/Debit Card");
        paymentModels.add("BHIM UPI");
        paymentModels.add("Google Pay");
        paymentModels.add("Cash");
        paymentModeAdapter.setPaymentModeList(paymentModels);
    }

    private void setUpAdapter() {
        bookOrderCheckoutAdapter = new BookOrderCheckoutAdapter(this);
        recyclerViewOrders.setAdapter(bookOrderCheckoutAdapter);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        paymentModeAdapter = new PaymentModeAdapter(this);
        recyclerViewPaymentMode.setAdapter(paymentModeAdapter);
        recyclerViewPaymentMode.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewPaymentMode.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 4)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewOrders = findViewById(R.id.rv_orders_checkout);
        recyclerViewPaymentMode = findViewById(R.id.rv_payment_mode);
        textViewCount = findViewById(R.id.tv_product_count);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                AnimUtil.slideFromLeftAnim(BookOrderCheckoutActivity.this);
            }
        });
    }
}
