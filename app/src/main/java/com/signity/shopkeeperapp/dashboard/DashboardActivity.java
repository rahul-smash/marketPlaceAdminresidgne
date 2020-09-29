package com.signity.shopkeeperapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.signity.shopkeeperapp.BuildConfig;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.home.HomeFragment;
import com.signity.shopkeeperapp.dashboard.orders.OrdersFragment;
import com.signity.shopkeeperapp.stores.StoresActivity;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener, NavDrawerAdapter.NavigationListener {

    private static final String TAG = "DashboardActivity";
    public BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView textViewToolbarTitle;
    private TextView textViewNavigationTitle;
    private TextView textViewAppVersion;
    private boolean doubleBackToExitPressedOnce;
    private NavDrawerAdapter navDrawerAdapter;
    private ListView listViewNavigation;
    private int navSelectedId;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();
        setUpToolbar();
        setUpStoreData();
        setUpNavigationAdapter();
        setUpDrawerToggle();
        setUpBottomNavigation();

        Log.d(TAG, "onCreate: " + AppPreference.getInstance().getDeviceToken());
    }

    private void setUpStoreData() {
        String storeName = AppPreference.getInstance().getStoreName();
        textViewNavigationTitle.setText(storeName);
        textViewAppVersion.setText(String.format("Version %s", BuildConfig.VERSION_NAME));
    }

    private void setUpNavigationAdapter() {
        navDrawerAdapter = new NavDrawerAdapter(this, this);
        listViewNavigation.setAdapter(navDrawerAdapter);
        listViewNavigation.setDivider(null);
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        textViewToolbarTitle = findViewById(R.id.tv_toolbar_dashboard);
        listViewNavigation = findViewById(R.id.lv_navigation);
        textViewNavigationTitle = findViewById(R.id.tv_store_name_nav);
        textViewAppVersion = findViewById(R.id.tv_app_version);
    }

    private void createOrdersBadge() {
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.action_bottom_orders);
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.colorBadge));
        badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.colorTextDark));
        badgeDrawable.setNumber(0);
        badgeDrawable.setVisible(true);
    }

    private void setUpDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.menuicon);
        mDrawerToggle.syncState();

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
    }

    private void setUpBottomNavigation() {
//        createOrdersBadge();
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setItemHorizontalTranslationEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_bottom_home);
    }

    private void toggleDrawer() {
        hideKeyboard();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        navDrawerAdapter.setSelectedId(navSelectedId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_bottom_home:
                navSelectedId = 0;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText("");
                menuItem.setChecked(true);
                showFragment(HomeFragment.getInstance(null), HomeFragment.TAG);
                break;
            case R.id.action_bottom_orders:
                navSelectedId = 1;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText("Orders");
                menuItem.setChecked(true);
                showFragment(OrdersFragment.getInstance(null), OrdersFragment.TAG);
                break;
            case R.id.action_bottom_products:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
//                toolbarTitle.setText("Products");
//                showFragment(ProductFragment.getInstance(null), ProductFragment.TAG);
                break;
            case R.id.action_bottom_categories:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
//                toolbarTitle.setText("Categories");
//                showFragment(CategoriesFragment.getInstance(null), CategoriesFragment.TAG);
                break;
            case R.id.action_bottom_account:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
//                toolbarTitle.setText("Account");
                break;
        }
        return false;
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            fragmentTransaction.replace(R.id.fl_dashboard, fragment, tag);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_bottom_home);
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            hideKeyboard();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClickViewAllOrders() {
        bottomNavigationView.setSelectedItemId(R.id.action_bottom_orders);
    }

    @Override
    public void onClickNavigation(NavDrawerAdapter.NavigationItems navigationItems) {
        switch (navigationItems) {
            case DASHBOARD:
                bottomNavigationView.setSelectedItemId(R.id.action_bottom_home);
                break;
            case ORDERS:
                bottomNavigationView.setSelectedItemId(R.id.action_bottom_orders);
                break;
            case PRODUCTS:
                Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;
            case SWITCH_STORE:
                startActivity(StoresActivity.getStartIntent(DashboardActivity.this));
                break;
        }
        toggleDrawer();
    }
}
