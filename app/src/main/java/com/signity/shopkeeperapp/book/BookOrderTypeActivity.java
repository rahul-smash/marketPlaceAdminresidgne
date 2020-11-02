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

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.book.TypeFragment.DeliveryFragment;
import com.signity.shopkeeperapp.book.TypeFragment.DineInFragment;
import com.signity.shopkeeperapp.book.TypeFragment.PickUpFragment;
import com.signity.shopkeeperapp.util.AnimUtil;

public class BookOrderTypeActivity extends BaseActivity {
    private static final String TAG = "BookOrderTypeActivity";
    private Toolbar toolbar;

    public static Intent getIntent(Context context) {
        return new Intent(context, BookOrderTypeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order_type);
        initViews();
        setUpToolbar();
        openFragment(0);
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
                showFragment(DeliveryFragment.getInstance(null), DeliveryFragment.TAG);
                break;
            case 1:
                showFragment(PickUpFragment.getInstance(null), PickUpFragment.TAG);
                break;
            case 2:
                showFragment(DineInFragment.getInstance(null), DineInFragment.TAG);
                break;
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
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
}
