package com.signity.shopkeeperapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.account.AccountFragment;
import com.signity.shopkeeperapp.dashboard.categories.CategoriesFragment;
import com.signity.shopkeeperapp.dashboard.home.HomeFragment;
import com.signity.shopkeeperapp.dashboard.orders.OrdersFragment;
import com.signity.shopkeeperapp.dashboard.products.ProductsFragment;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView toolbarTitle;
   public static  ImageView imgFilter;
    private boolean doubleBackToExitPressedOnce;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();
        setUpToolbar();
        setUpDrawerToggle();
        setUpBottomNavigation();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
       imgFilter=(ImageView)findViewById(R.id.imgFilter);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbarTitle = findViewById(R.id.tv_toolbar_dashboard);
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.action_bottom_orders);
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.colorBadge));
        badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.colorTextDark));
        badgeDrawable.setNumber(20);
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.action_bottom_home:
                toolbarTitle.setText("Home");
                imgFilter.setVisibility(View.GONE);
                showFragment(HomeFragment.getInstance(null), HomeFragment.TAG);
                break;
            case R.id.action_bottom_orders:
                toolbarTitle.setText("Orders");
                imgFilter.setVisibility(View.VISIBLE);

                showFragment(OrdersFragment.getInstance(null), OrdersFragment.TAG);
                break;
            case R.id.action_bottom_products:
                toolbarTitle.setText("Products");
                imgFilter.setVisibility(View.GONE);


                showFragment(ProductsFragment.getInstance(null), ProductsFragment.TAG);
                break;
            case R.id.action_bottom_categories:
                toolbarTitle.setText("Categories");
                imgFilter.setVisibility(View.GONE);


                showFragment(CategoriesFragment.getInstance(null), CategoriesFragment.TAG);
                break;
            case R.id.action_bottom_account:
                toolbarTitle.setText("Account");

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
}
