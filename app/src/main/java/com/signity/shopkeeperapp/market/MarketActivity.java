package com.signity.shopkeeperapp.market;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;

public class MarketActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        initView();
        setUpToolbar();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CreativeFragment.MARKET_MODE, Constant.MarketMode.CREATIVE);
        showMarketFragment(CreativeFragment.getInstance(bundle), CreativeFragment.TAG);
    }

    private void initView() {
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
                AnimUtil.slideFromLeftAnim(MarketActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(this);
    }

    private void showMarketFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment, tag);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }
}
