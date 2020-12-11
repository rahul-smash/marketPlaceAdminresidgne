package com.signity.shopkeeperapp.dashboard.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.book.CategoriesFragment;
import com.signity.shopkeeperapp.dashboard.Products.ProductFragment;
import com.signity.shopkeeperapp.dashboard.category.CategoryFragment;

public class CatalogFragment extends Fragment {

    public static final String TAG = "CatalogFragment";
    private TabLayout tabLayout;
    private FragmentType selectedTab = FragmentType.PRODUCTS;

    public static CatalogFragment getInstance(Bundle bundle) {
        CatalogFragment fragment = new CatalogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catelog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpTab();
        openFragment(selectedTab);
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tab_catalog);
    }

    private void setUpTab() {
        tabLayout.addTab(tabLayout.newTab().setText("Products"));
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
        switch (fragmentType) {
            case PRODUCTS:
                showFragment(ProductFragment.getInstance(null), ProductFragment.TAG);
                break;
            case CATEGORIES:
                showFragment(CategoryFragment.getInstance(null), CategoryFragment.TAG);
                break;
        }
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            fragmentTransaction.replace(R.id.fl_catalog, fragment, tag);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }
    }

    public enum FragmentType {
        PRODUCTS, CATEGORIES
    }
}
