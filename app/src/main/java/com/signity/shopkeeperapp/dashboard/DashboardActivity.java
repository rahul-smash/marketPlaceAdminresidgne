package com.signity.shopkeeperapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.signity.shopkeeperapp.SplashActivity;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.book.BookOrderActivity;
import com.signity.shopkeeperapp.customers.CustomersActivity;
import com.signity.shopkeeperapp.dashboard.Products.ProductFragment;
import com.signity.shopkeeperapp.dashboard.account.AccountFragment;
import com.signity.shopkeeperapp.dashboard.categories.CategoriesFragment;
import com.signity.shopkeeperapp.dashboard.home.HomeFragment;
import com.signity.shopkeeperapp.dashboard.orders.OrdersFragment;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.model.ModelForceUpdate;
import com.signity.shopkeeperapp.model.ResponseForceUpdate;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.stores.StoresActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashboardActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener, NavDrawerAdapter.NavigationListener {

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
    private TextView textViewLogout;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();
        setUpToolbar();
        setUpNavigationAdapter();
        setUpDrawerToggle();
        setUpBottomNavigation();
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
        textViewLogout = findViewById(R.id.tv_logout_title);

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogOutApi();
            }
        });
    }

    private void createOrdersBadge(int count) {
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.action_bottom_orders);
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.colorBadge));
        badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.colorTextDark));
        badgeDrawable.setNumber(count);
        badgeDrawable.setVisible(count > 0);
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
        checkForceDownload();
        setUpStoreData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.action_bottom_home:
                navSelectedId = 0;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText("");
                showFragment(HomeFragment.getInstance(null), HomeFragment.TAG);
                break;
            case R.id.action_bottom_orders:
                navSelectedId = 1;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText("Orders");
                showFragment(OrdersFragment.getInstance(null), OrdersFragment.TAG);
                break;
            case R.id.action_bottom_products:
                navSelectedId = 2;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText("Products");
                showFragment(ProductFragment.getInstance(null), ProductFragment.TAG);
                break;
            case R.id.action_bottom_categories:
                textViewToolbarTitle.setText("Categories");
                showFragment(CategoriesFragment.getInstance(null), CategoriesFragment.TAG);
                break;
            case R.id.action_bottom_account:
                textViewToolbarTitle.setText("Account");
                showFragment(AccountFragment.getInstance(null), AccountFragment.TAG);
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
    public void onClickViewProducts() {
        bottomNavigationView.setSelectedItemId(R.id.action_bottom_products);
    }

    @Override
    public void onClickViewCustomers() {
        startActivity(CustomersActivity.getStartIntent(this));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onUpdateOrdersCount(int count) {
        createOrdersBadge(count);
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
                bottomNavigationView.setSelectedItemId(R.id.action_bottom_products);
                break;
            case BOOK:
                startActivity(BookOrderActivity.getIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;
            case SWITCH_STORE:
                startActivity(StoresActivity.getStartIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;
        }
        toggleDrawer();
    }

    private void checkForceDownload() {
        NetworkAdaper.getNetworkServices().forceDownload(new Callback<ResponseForceUpdate>() {
            @Override
            public void success(ResponseForceUpdate responseForceUpdate, Response response) {

                if (isDestroyed()) {
                    return;
                }

                if (responseForceUpdate != null && responseForceUpdate.getSuccess()) {
                    try {
                        ModelForceUpdate forceUpdate = responseForceUpdate.getData().get(0);
                        checkForceUpdate(forceUpdate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void checkForceUpdate(ModelForceUpdate forceUpdate) {

        String currentVersion;
        String playStoreVersion;
        if (forceUpdate != null) {
            currentVersion = BuildConfig.VERSION_NAME;
            playStoreVersion = forceUpdate.getAndroidAppVerison();

            if (!TextUtils.isEmpty(playStoreVersion)) {
                try {
                    double playVersion = Double.parseDouble(playStoreVersion);

                    double appVersion = Double.parseDouble(currentVersion);
                    Log.i("@@playversion", "" + playVersion);
                    Log.i("@@appVersion", "" + appVersion);

                    if (playVersion > appVersion) {
                        openDialogForVersion(forceUpdate);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void openDialogForVersion(ModelForceUpdate forceUpdate) {
        if (forceUpdate.getForceDownload() != null) {
            final DialogHandler dialogHandler = new DialogHandler(this);
            dialogHandler.setDialog("APPLICATION UPDATE", forceUpdate.getForceDownloadMessage());
            if (forceUpdate.getForceDownload().equalsIgnoreCase("1")) {
                dialogHandler.setCancelable(false);
                dialogHandler.setPostiveButton("Update", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPlayStoreLink();
                        dialogHandler.dismiss();
                    }

                });
            } else {
                dialogHandler.setNegativeButton("Cancel", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHandler.dismiss();
                    }
                });
                dialogHandler.setPostiveButton("Update", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPlayStoreLink();
                        dialogHandler.dismiss();
                    }
                });
            }
        }
    }

    private void openPlayStoreLink() {
        final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
        Log.i("@@openPlayStoreLink", "" + appPackageName);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void callLogOutApi() {
        ProgressDialogUtil.showProgressDialog(this);

        Map<String, String> param = new HashMap<String, String>();
        param.put("login_id", AppPreference.getInstance().getUserMobile());
        param.put("type", "phone");

        NetworkAdaper.getNetworkServices().logout(param, new Callback<LoginModel>() {
            @Override
            public void success(LoginModel loginModel, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                AppPreference.getInstance().clearAll();
                NetworkAdaper.setupRetrofitClient(NetworkAdaper.setBaseUrl(""));
                startActivity(SplashActivity.getIntent(DashboardActivity.this));
                AnimUtil.slideFromLeftAnim(DashboardActivity.this);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }
}