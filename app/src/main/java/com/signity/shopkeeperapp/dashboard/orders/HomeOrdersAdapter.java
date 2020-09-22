package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeOrdersAdapter extends RecyclerView.Adapter<HomeOrdersAdapter.ViewHolder> {

    private static final String TAG = "DashboardOrdersAdapter";
    private Context context;
    private List<OrdersListModel> ordersListModels = new ArrayList<>();
    private Constant.OrderStatus orderStatus = Constant.OrderStatus.ALL;

    public HomeOrdersAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    public void setOrdersListModels(List<OrdersListModel> ordersListModels, Constant.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.ordersListModels = ordersListModels;
        notifyDataSetChanged();
    }

    public void clearOrdersList() {
        this.ordersListModels.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ordersListModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewName, textViewStatus, textViewPrice, textViewOrderId, textViewDateTime, textViewItemsCount;
        ConstraintLayout itemView_Parent;
        LinearLayout linearLayoutAccept, linearLayoutShipped, linearLayoutDelivered, linearLayoutStatus, getLinearLayoutStatusDelivered;

        public ViewHolder(final View convertView) {
            super(convertView);
            textViewItemsCount = (TextView) convertView.findViewById(R.id.tv_items_count);
            textViewPrice = (TextView) convertView.findViewById(R.id.tv_order_price);
            imageViewProduct = (ImageView) convertView.findViewById(R.id.iv_product);
            textViewOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
            textViewName = (TextView) convertView.findViewById(R.id.tv_store_name);
            textViewDateTime = (TextView) convertView.findViewById(R.id.tv_order_date_time);
            textViewStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
            itemView_Parent = (ConstraintLayout) convertView.findViewById(R.id.itemView_Parent);
            linearLayoutAccept = (LinearLayout) convertView.findViewById(R.id.ll_accept);
            linearLayoutShipped = (LinearLayout) convertView.findViewById(R.id.ll_shipped);
            linearLayoutDelivered = (LinearLayout) convertView.findViewById(R.id.ll_delivered);
            linearLayoutStatus = (LinearLayout) convertView.findViewById(R.id.ll_status);
            getLinearLayoutStatusDelivered = (LinearLayout) convertView.findViewById(R.id.ll_status_delivered);
        }

        public void bind(int position) {

            OrdersListModel ordersModel = ordersListModels.get(position);

            textViewName.setText(ordersModel.getCustomerName());
            textViewOrderId.setText(String.format("Order- %s", ordersModel.getOrderId()));

            String itemText = ordersModel.getItems().size() > 1 ? "items" : "item";
            textViewItemsCount.setText(String.format("%s %s", ordersModel.getItems().size(), itemText));
            textViewPrice.setText(String.format(Locale.getDefault(), "%s", Util.getPriceWithCurrency(ordersModel.getTotal(), AppPreference.getInstance().getCurrency())));
            textViewDateTime.setText(ordersModel.getTime());

            switch (orderStatus) {
                case ALL:
                    break;
                case PENDING:
                    linearLayoutAccept.setVisibility(View.VISIBLE);
                    linearLayoutDelivered.setVisibility(View.GONE);
                    linearLayoutShipped.setVisibility(View.GONE);
                    break;
                case ACCEPTED:
                    linearLayoutAccept.setVisibility(View.GONE);
                    linearLayoutDelivered.setVisibility(View.GONE);
                    linearLayoutShipped.setVisibility(View.VISIBLE);
                    break;
                case SHIPPED:
                    linearLayoutAccept.setVisibility(View.GONE);
                    linearLayoutDelivered.setVisibility(View.VISIBLE);
                    linearLayoutShipped.setVisibility(View.GONE);
                    break;
                case DELIVERD:
                    linearLayoutStatus.setVisibility(View.GONE);
                    getLinearLayoutStatusDelivered.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
