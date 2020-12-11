package com.signity.shopkeeperapp.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.classes.PageIndicator;
import com.signity.shopkeeperapp.login.MobileLoginActivity;
import com.signity.shopkeeperapp.onboarding.adapter.OnBoardingPagerAdapter;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Util;

/**
 * OnBoardingActivity
 *
 * @blame Android Team
 */
public class OnBoardingActivity extends BaseActivity {
    private static final String TAG = OnBoardingActivity.class.getSimpleName();

    private ViewPager viewPager;
    private PageIndicator pageIndicator;
    private int pagePosition;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OnBoardingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        setUpStatusBar();
        initViews();
        setUp();
    }

    private void initViews() {
        pageIndicator = findViewById(R.id.indicator_on_boarding);
        viewPager = findViewById(R.id.vp_on_boarding);

        findViewById(R.id.tv_skip_on_boarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMobileLogin();
            }
        });
    }

    private void openMobileLogin() {
        startActivity(MobileLoginActivity.getStartIntent(this));
        runAnimation();
    }

    private void runAnimation() {
        AnimUtil.slideFromRightAnim(this);
        finish();
    }

    private void setUpStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = 1024;
        decorView.setSystemUiVisibility(uiOptions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
    }

    protected void setUp() {
        setUpViewPager();
        setUpPageIndicator();
    }

    private void setUpPageIndicator() {
        pageIndicator.setActiveDot(pagePosition);
        pageIndicator.setDotSpacing((int) Util.pxFromDp(this, 2));
        pageIndicator.setTotalNoOfDots(OnBoardingScreen.OnBoarding.values().length);
    }

    private void setUpViewPager() {
        OnBoardingPagerAdapter onBoardingAdapter = new OnBoardingPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(onBoardingAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagePosition = position;
                pageIndicator.setActiveDot(pagePosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageIndicator.setActiveDot(pagePosition);
    }
}