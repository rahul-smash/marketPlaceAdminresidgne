package com.signity.shopkeeperapp.dashboard.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.dashboard.DataResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeContentAdapter extends RecyclerView.Adapter<HomeContentAdapter.ViewHolder> {

    private Context context;
    private HomeContentAdapterListener listener;
    private List<HomeItems> homeItemsList = new ArrayList<>();
    private DataResponse data = new DataResponse();

    public HomeContentAdapter(Context context) {
        this.context = context;
        homeItemsList.addAll(Arrays.asList(HomeItems.values()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_home_content, parent, false);
        return new HomeContentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeContentAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return homeItemsList.size();
    }

    public void setListener(HomeContentAdapterListener listener) {
        this.listener = listener;
    }

    public void setUpData(DataResponse data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public enum HomeItems {

        ORDERS("Orders"), REVENUE("Revenue"),
        ALL_CUSTOMERS("All Customers"), TOTAL_PRODUCT("Total Products");

        private String title;

        HomeItems(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public interface HomeContentAdapterListener {
        void onClickItem(HomeItems homeItems);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDetail, textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDetail = itemView.findViewById(R.id.tv_detail);
            textViewTitle = itemView.findViewById(R.id.tv_title);
        }

        public void bind(int positon) {

            final HomeItems homeItems = homeItemsList.get(positon);

            textViewTitle.setText(homeItems.getTitle());

            switch (homeItems) {
                case ORDERS:
                    String orderCount = String.format(Locale.getDefault(), "%d", data.getTotalOrders());
                    textViewDetail.setText(orderCount);
                    break;
                case REVENUE:
                    textViewDetail.setText(data.getOutstanding());
                    break;
                case ALL_CUSTOMERS:
                    String totalCustomers = String.format(Locale.getDefault(), "%d", data.getCustomers());
                    textViewDetail.setText(totalCustomers);
                    break;
                case TOTAL_PRODUCT:
                    String totalProducts = String.format(Locale.getDefault(), "%d", data.getProducts());
                    textViewDetail.setText(totalProducts);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickItem(homeItems);
                    }
                }
            });
        }
    }
}


