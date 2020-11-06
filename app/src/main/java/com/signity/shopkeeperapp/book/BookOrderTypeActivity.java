package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.book.TypeFragment.DeliveryFragment;
import com.signity.shopkeeperapp.book.TypeFragment.DineInFragment;
import com.signity.shopkeeperapp.book.TypeFragment.OrderTypeAdapter;
import com.signity.shopkeeperapp.book.TypeFragment.PickUpFragment;
import com.signity.shopkeeperapp.util.AnimUtil;

public class BookOrderTypeActivity extends BaseActivity implements OrderTypeAdapter.OrderTypeListener {
    public static final String CUSTOMER_NUMBER = "CUSTOMER_NUMBER";
    private static final String TAG = "BookOrderTypeActivity";
    private Toolbar toolbar;
    private DeliveryFragment deliveryFragment;
    private DineInFragment dineInFragment;
    private PickUpFragment pickUpFragment;
    private String customerNumber = "";
    private RecyclerView recyclerViewOrderType;
    private OrderTypeAdapter orderTypeAdapter;

    public static Intent getIntent(Context context) {
        return new Intent(context, BookOrderTypeActivity.class);
    }

    public static Intent getIntent(Context context, Bundle bundle) {
        Intent intent = getIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order_type);
        getExtra();
        initViews();
        setUpToolbar();
        setUpAdapter();
        initFragments();
        openFragment(0);
    }

    private void setUpAdapter() {
        orderTypeAdapter = new OrderTypeAdapter(this, this);
        recyclerViewOrderType.setAdapter(orderTypeAdapter);
        recyclerViewOrderType.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initFragments() {
        Bundle bundle = new Bundle();
        bundle.putString("NUMBER", customerNumber);
        deliveryFragment = DeliveryFragment.getInstance(bundle);
        pickUpFragment = PickUpFragment.getInstance(bundle);
        dineInFragment = DineInFragment.getInstance(bundle);
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerNumber = bundle.getString(CUSTOMER_NUMBER);
        }
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            fragmentTransaction.replace(R.id.fl_book_order_type, fragment, tag);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }
    }

    private void openFragment(int position) {
        switch (position) {
            case 0:
                showFragment(deliveryFragment, DeliveryFragment.TAG);
                break;
            case 1:
                showFragment(pickUpFragment, PickUpFragment.TAG);
                break;
            case 2:
                showFragment(dineInFragment, DineInFragment.TAG);
                break;
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewOrderType = findViewById(R.id.rv_order_type);
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
                AnimUtil.slideFromLeftAnim(BookOrderTypeActivity.this);
            }
        });
    }

    @Override
    public void onSelectOrderType(int position) {
        hideKeyboard();
        openFragment(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(DeliveryFragment.TAG);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
