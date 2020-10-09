package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeOrdersAdapter extends RecyclerView.Adapter<HomeOrdersAdapter.ViewHolder> {

    private static final String TAG = "HomeOrdersAdapter";
    private Context context;
    private List<OrdersListModel> ordersListModels = new ArrayList<>();
    private HomeOrdersAdapter.OrderType orderTypeFilter = HomeOrdersAdapter.OrderType.ALL;
    private OrdersListener listener;
    private boolean showImages;

    public HomeOrdersAdapter(Context context, boolean showImages) {
        this.context = context;
        this.showImages = showImages;
    }

    public void setListener(OrdersListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        switch (viewType) {
            case 1:
                viewHolder = new ViewHolderAccepted(LayoutInflater.from(context).inflate(R.layout.itemview_orders_accepted, parent, false));
                break;
            case 4:
                viewHolder = new ViewHolderShipped(LayoutInflater.from(context).inflate(R.layout.itemview_orders_shipped, parent, false));
                break;
            case 5:
                viewHolder = new ViewHolderDelivered(LayoutInflater.from(context).inflate(R.layout.itemview_orders_delivered, parent, false));
                break;
            case 2:
                viewHolder = new ViewHolderRejected(LayoutInflater.from(context).inflate(R.layout.itemview_orders_rejected, parent, false));
                break;
            case 0:
            default:
                viewHolder = new ViewHolderPending(LayoutInflater.from(context).inflate(R.layout.itemview_orders_pending, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickOrder(holder.getAdapterPosition());
                }
            }
        });
    }

    public void setOrderTypeFilter(OrderType orderTypeFilter) {
        if (this.orderTypeFilter != orderTypeFilter) {
            ordersListModels.clear();
        }
        this.orderTypeFilter = orderTypeFilter;
    }

    public void addOrdersListModels(List<OrdersListModel> ordersListModels) {
        this.ordersListModels.addAll(ordersListModels);
        notifyDataSetChanged();
    }

    public List<OrdersListModel> getOrdersListModels() {
        return ordersListModels;
    }

    public void setOrdersListModels(List<OrdersListModel> ordersListModels) {
        this.ordersListModels = ordersListModels;
        notifyDataSetChanged();
    }

    public void clearOrdersList() {
        this.ordersListModels.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        OrdersListModel model = ordersListModels.get(position);
        return Integer.parseInt(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return ordersListModels.size();
    }

    public void removeItem(int position) {
        ordersListModels.remove(position);
        notifyItemRemoved(position);
    }

    public enum OrderType {
        ALL(9, "all"), PENDING(0, "pending"), ACCEPTED(1, "active"), SHIPPED(4, "shipped"), DELIVERED(5, "delivered"), REJECTED(2, "rejected");

        private int statusId;
        private String slug;

        OrderType(int statusId, String slug) {
            this.statusId = statusId;
            this.slug = slug;
        }

        public String getSlug() {
            return slug;
        }

        public int getStatusId() {
            return statusId;
        }
    }

    public interface OrdersListener {
        void onClickOrder(int position);

        void onRejectOrder(int position);

        void onAcceptOrder(int position);

        void onShipOrder(int position);

        void onDeliverOrder(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewName, textViewPrice, textViewOrderId, textViewDateTime, textViewItemsCount, textViewPaymentType;

        public ViewHolder(final View convertView) {
            super(convertView);
            textViewItemsCount = convertView.findViewById(R.id.tv_items_count);
            textViewPrice = convertView.findViewById(R.id.tv_order_price);
            imageViewProduct = convertView.findViewById(R.id.iv_product);
            textViewOrderId = convertView.findViewById(R.id.tv_order_id);
            textViewName = convertView.findViewById(R.id.tv_store_name);
            textViewDateTime = convertView.findViewById(R.id.tv_order_date_time);
            textViewPaymentType = convertView.findViewById(R.id.tv_order_type);
        }

        public void bind(int position) {

            OrdersListModel ordersModel = ordersListModels.get(position);

            imageViewProduct.setVisibility(showImages ? View.VISIBLE : View.GONE);
            textViewName.setText(ordersModel.getCustomerName());
            textViewOrderId.setText(String.format("Order #%s", ordersModel.getOrderId()));

            String itemText = ordersModel.getItems().size() > 1 ? "items" : "item";
            textViewItemsCount.setText(String.format("%s %s", ordersModel.getItems().size(), itemText));
            textViewPrice.setText(String.format(Locale.getDefault(), "%s", Util.getPriceWithCurrency(ordersModel.getTotal(), AppPreference.getInstance().getCurrency())));
            textViewDateTime.setText(ordersModel.getTime());
            textViewPaymentType.setText(ordersModel.getPaymentMethod().toUpperCase());
        }
    }

    class ViewHolderPending extends ViewHolder {
        Chip chipAccept;
        Chip chipReject;

        public ViewHolderPending(final View convertView) {
            super(convertView);
            chipAccept = convertView.findViewById(R.id.chip_accept);
            chipReject = convertView.findViewById(R.id.chip_reject);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            chipAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAcceptOrder(getAdapterPosition());
                    }
                }
            });
            chipReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRejectOrder(getAdapterPosition());
                    }
                }
            });
        }
    }

    class ViewHolderAccepted extends ViewHolder {
        Chip chipShip;
        Chip chipReject;

        public ViewHolderAccepted(final View convertView) {
            super(convertView);
            chipShip = convertView.findViewById(R.id.chip_ship);
            chipReject = convertView.findViewById(R.id.chip_reject);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            chipShip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onShipOrder(getAdapterPosition());
                    }
                }
            });
            chipReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRejectOrder(getAdapterPosition());
                    }
                }
            });
        }
    }

    class ViewHolderShipped extends ViewHolder {
        Chip chipDeliver;
        Chip chipReject;

        public ViewHolderShipped(final View convertView) {
            super(convertView);
            chipDeliver = convertView.findViewById(R.id.chip_deliver);
            chipReject = convertView.findViewById(R.id.chip_reject);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            chipDeliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeliverOrder(getAdapterPosition());
                    }
                }
            });
            chipReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRejectOrder(getAdapterPosition());
                    }
                }
            });
        }
    }

    class ViewHolderDelivered extends ViewHolder {

        public ViewHolderDelivered(final View convertView) {
            super(convertView);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
        }
    }

    class ViewHolderRejected extends ViewHolder {

        public ViewHolderRejected(final View convertView) {
            super(convertView);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
        }
    }
}
