package com.signity.shopkeeperapp.onboarding.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.signity.shopkeeperapp.onboarding.OnBoardingScreen;

public class OnBoardingPagerAdapter extends FragmentPagerAdapter {

    public OnBoardingPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(OnBoardingScreen.SCREEN_ID, pos);
        return OnBoardingScreen.getInstance(bundle);
    }

    @Override
    public int getCount() {
        return OnBoardingScreen.OnBoarding.values().length;
    }
}

