package com.signity.shopkeeperapp.book;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.tabs.TabLayout;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

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
    private double totalPrice;
    private FragmentType selectedTab = FragmentType.BEST_SELLER;
    private SearchView searchView;
    private CategoriesFragment categoriesFragment;
    private BestSellerFragment bestSellerFragment;

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
                selectedTab = FragmentType.values()[tab.getPosition()];
                openFragment(selectedTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void openFragment(FragmentType fragmentType) {
        searchView.clearFocus();
        switch (fragmentType) {
            case BEST_SELLER:
                bestSellerFragment = BestSellerFragment.getInstance(null);
                showFragment(bestSellerFragment, BestSellerFragment.TAG);
                break;
            case CATEGORIES:
                categoriesFragment = CategoriesFragment.getInstance(null);
                showFragment(categoriesFragment, CategoriesFragment.TAG);
                break;
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        textViewOrderTotal = findViewById(R.id.tv_order_total);
        textViewOrderItems = findViewById(R.id.tv_order_items);
        constraintLayoutProceed = findViewById(R.id.const_proceed);
        searchView = findViewById(R.id.search_book_order);

        constraintLayoutProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BookOrderTypeActivity.CUSTOMER_NUMBER, customerNumber);
                bundle.putDouble(BookOrderTypeActivity.TOTAL, totalPrice);
                startActivity(BookOrderTypeActivity.getIntent(BookOrderActivity.this, bundle));
                AnimUtil.slideFromRightAnim(BookOrderActivity.this);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchProductCategory(query.trim());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) && selectedTab == FragmentType.CATEGORIES) {
                    searchProductCategory(newText.trim());
                }

                if (TextUtils.isEmpty(newText)) {
                    onCloseSearch();
                }

                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onCloseSearch();
                return false;
            }
        });
    }

    private void searchProductCategory(String query) {
        switch (selectedTab) {
            case BEST_SELLER:
                if (bestSellerFragment != null) {
                    bestSellerFragment.filterProduct(query);
                }
                break;
            case CATEGORIES:
                if (categoriesFragment != null) {
                    categoriesFragment.searchFilterCategories(query);
                }
                break;
        }
    }

    private void onCloseSearch() {
        switch (selectedTab) {
            case BEST_SELLER:
                if (bestSellerFragment != null) {
                    bestSellerFragment.onCloseSearch();
                }
                break;
            case CATEGORIES:
                if (categoriesFragment != null) {
                    categoriesFragment.onCloseSearch();
                }
                break;
        }
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
        totalPrice = 0;
        for (GetProductData productData1 : OrderCart.getOrderCartMap().values()) {
            totalItems += 1;
            if (productData1.getVariants() != null && !productData1.getVariants().isEmpty()) {
                String price = productData1.getVariants().get(productData1.getSelectedVariantIndex()).getPrice();
                if (TextUtils.isEmpty(price)) {
                    price = "0";
                }
                totalPrice += Double.parseDouble(price) * productData1.getCount();
            }
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

    @Override
    protected void onResume() {
        super.onResume();
        openFragment(selectedTab);
        if (!OrderCart.isCartEmpty()) {
            updateOrderData();
            showLayout();
        } else {
            hideLayout();
        }
    }

    public enum FragmentType {
        BEST_SELLER, CATEGORIES
    }
}
