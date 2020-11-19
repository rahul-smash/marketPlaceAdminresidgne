package com.signity.shopkeeperapp.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.signity.shopkeeperapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavDrawerAdapter extends BaseAdapter {

    private Context context;
    private int selectedId;
    private NavigationListener listener;
    private List<NavigationItems> navigationItems = new ArrayList<>();

    public NavDrawerAdapter(Context context, NavigationListener listener) {
        this.context = context;
        this.listener = listener;
        navigationItems.addAll(Arrays.asList(NavigationItems.values()));
    }

    @Override
    public int getCount() {
        return navigationItems.size();
    }

    @Override
    public NavigationItems getItem(int position) {
        return navigationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final NavigationItems items = getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.itemview_nav_drawer, parent, false);

        TextView textViewNavTitle = convertView.findViewById(R.id.tv_nav_title);
        ImageView imageViewNav = convertView.findViewById(R.id.iv_nav_image);
        ConstraintLayout constraintLayoutNavItem = convertView.findViewById(R.id.const_nav_item);
        View view = convertView.findViewById(R.id.view_nav);

        textViewNavTitle.setText(items.getTitle());
        imageViewNav.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), items.getDrawableIcon(), null));

        boolean isSelected = position == selectedId;

        view.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        constraintLayoutNavItem.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), isSelected ? R.color.colorBackgroundGrey : android.R.color.transparent, null));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickNavigation(items);
                    selectedId = position;
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    public enum NavigationItems {
        DASHBOARD("Dashboard", R.drawable.dashboardicon),
        ORDERS("Orders", R.drawable.ordersicon),
        PRODUCTS("Products", R.drawable.producticon),
        CUSTOMERS("Customers", R.drawable.ordersicon),
        BOOK("Book Order", R.drawable.transactionicon),
        SWITCH_STORE("Switch Store", R.drawable.switchstoreicon);

        private String title;
        @DrawableRes
        private int drawableIcon;

        NavigationItems(String title, @DrawableRes int drawableIcon) {
            this.title = title;
            this.drawableIcon = drawableIcon;
        }

        public String getTitle() {
            return title;
        }

        public int getDrawableIcon() {
            return drawableIcon;
        }
    }

    public interface NavigationListener {
        void onClickNavigation(NavigationItems navigationItems);
    }
}
