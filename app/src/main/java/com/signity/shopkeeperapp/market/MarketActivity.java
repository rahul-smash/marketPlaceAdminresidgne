package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    public static final String MARKET_MODE = "MARKET_MODE";
    private Toolbar toolbar;
    private Constant.MarketMode marketMode = Constant.MarketMode.CREATIVE;
    private TextView textViewToolbar;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MarketActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MarketActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        getExtra();
        initView();
        setUpToolbar();
        openFragment();
    }

    private void openFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CreativeFragment.MARKET_MODE, marketMode);
        showMarketFragment(CreativeFragment.getInstance(bundle), CreativeFragment.TAG);
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            marketMode = (Constant.MarketMode) bundle.getSerializable(MARKET_MODE);
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        textViewToolbar = findViewById(R.id.tv_toolbar);

        textViewToolbar.setText(marketMode == Constant.MarketMode.CREATIVE ? "Premium Creatives" : "Custom Frames");
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
