package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookOrderActivity extends BaseActivity {
    public static final String CUSTOMER_NUMBER = "CUSTOMER_NUMBER";
    private static final String TAG = "BookOrderActivity";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ConstraintLayout constraintLayoutProceed;
    private TextView textViewOrderItems, textViewOrderTotal;
    private TranslateAnimation animationBottomUp, animationBottomDown;
    private boolean alreadyVisible;
    private String customerNumber = "";

    public static Intent getIntent(Context context) {
        return new Intent(context, BookOrderActivity.class);
    }

    public static Intent getIntent(Context context, Bundle bundle) {
        Intent intent = getIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order);
        getExtra();
        initViews();
        setUpToolbar();
        setUpTab();
        setUpAnimation();
        openFragment(0);
        if (!OrderCart.isCartEmpty()) {
            updateOrderData();
            showLayout();
        }
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerNumber = bundle.getString(CUSTOMER_NUMBER);
        }
    }

    private void setUpAnimation() {
        animationBottomUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0f);
        animationBottomUp.setDuration(300);
        animationBottomUp.setFillEnabled(true);

        animationBottomDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.75f);
        animationBottomDown.setDuration(300);
        animationBottomDown.setFillEnabled(false);
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            fragmentTransaction.replace(R.id.fl_book_order, fragment, tag);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }
    }

    private void setUpTab() {
        tabLayout.addTab(tabLayout.newTab().setText("Best Sellers"));
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorTextGrey), getResources().getColor(R.color.colorPrimary));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                openFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void openFragment(int position) {
        List<GetProductData> selected = new ArrayList<>();
        if (!OrderCart.isCartEmpty()) {
            selected.addAll(OrderCart.getOrderCartMap().values());
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BestSellerFragment.SELECTED_PRODUCTS, (ArrayList<? extends Parcelable>) selected);
        switch (position) {
            case 0:
                showFragment(BestSellerFragment.getInstance(bundle), BestSellerFragment.TAG);
                break;
            case 1:
                showFragment(CategoriesFragment.getInstance(bundle), CategoriesFragment.TAG);
                break;
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        textViewOrderTotal = findViewById(R.id.tv_order_total);
        textViewOrderItems = findViewById(R.id.tv_order_items);
        constraintLayoutProceed = findViewById(R.id.const_proceed);

        constraintLayoutProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BookOrderTypeActivity.CUSTOMER_NUMBER, customerNumber);
                startActivity(BookOrderTypeActivity.getIntent(BookOrderActivity.this, bundle));
                AnimUtil.slideFromRightAnim(BookOrderActivity.this);
            }
        });
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
                AnimUtil.slideFromLeftAnim(BookOrderActivity.this);
            }
        });
    }

    public void addOrders(GetProductData productData) {
        OrderCart.putOrder(productData);
        updateOrderData();
        showLayout();
    }

    private void updateOrderData() {
        int totalItems = 0;
        double totalPrice = 0;
        for (GetProductData productData1 : OrderCart.getOrderCartMap().values()) {
            totalItems += 1;
            totalPrice += Double.parseDouble(productData1.getVariants().get(0).getPrice()) * productData1.getCount();
        }

        textViewOrderItems.setText(String.format(Locale.getDefault(), "%d %s", totalItems, totalItems > 1 ? "Items" : "Item"));
        textViewOrderTotal.setText(Util.getPriceWithCurrency(totalPrice, AppPreference.getInstance().getCurrency()));
    }

    public void removeOrders(GetProductData productData) {
        if (productData.getCount() < 1) {
            OrderCart.removeOrder(productData.getId());
        } else {
            OrderCart.putOrder(productData);
        }

        hideLayout();
        updateOrderData();
    }

    private void showLayout() {

        if (alreadyVisible) {
            return;
        }

        alreadyVisible = true;
        constraintLayoutProceed.setAnimation(animationBottomUp);
        constraintLayoutProceed.setVisibility(View.VISIBLE);
    }

    private void hideLayout() {
        if (OrderCart.isCartEmpty() && constraintLayoutProceed.getVisibility() == View.VISIBLE) {
            alreadyVisible = false;
            constraintLayoutProceed.setAnimation(animationBottomDown);
            constraintLayoutProceed.setVisibility(View.GONE);
        }
    }
}
