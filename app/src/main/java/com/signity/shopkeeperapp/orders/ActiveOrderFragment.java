package com.signity.shopkeeperapp.orders;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.Constant;

/**
 * Created by Rajinder on 29/9/15.
 */
public class ActiveOrderFragment extends Fragment {

    TextView header;
    public static boolean api_refreshed = false;
    public ViewPager viewPager;
    PagerAdapter adapter;


    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                ActiveOrderFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_order, container, false);
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Approved"));
        tabLayout.addTab(tabLayout.newTab().setText("Processing"));
        tabLayout.addTab(tabLayout.newTab().setText("Shipping"));
        tabLayout.addTab(tabLayout.newTab().setText("Delivered"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(4);
        adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (api_refreshed) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            adapter.notifyDataSetChanged();
                            api_refreshed = false;

                        }
                    }, 1000);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }

    public void onStatusChange(int num) {
        viewPager.setCurrentItem(num);

    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {


            Bundle arg = new Bundle();
            arg.putInt("position", position);
            ActiveOrderFragmentItem tab1 = new ActiveOrderFragmentItem();
            tab1.setArguments(arg);
            switch (position) {
                case 0:
                    arg.putString("type", Constant.TYPE_APPROVE);
                    tab1.setArguments(arg);
                    try {
                        tab1.listOrderSelected.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    arg.putString("type", Constant.TYPE_PROCESSING);
                    tab1.setArguments(arg);
                    try {
                        tab1.listOrderSelected.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    arg.putString("type", Constant.TYPE_SHIPPING);
                    tab1.setArguments(arg);
                    try {
                        tab1.listOrderSelected.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    arg.putString("type", Constant.TYPE_DELIVERED);
                    tab1.setArguments(arg);
                    try {
                        tab1.listOrderSelected.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    return null;
            }
            return tab1;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }


}
