package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.orderTracker.OrderTrackerBroadcast;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeOrdersAdapter extends RecyclerView.Adapter<HomeOrdersAdapter.ViewHolder> {

    private static final String TAG = "HomeOrdersAdapter";
    private Context context;
    private OrdersListener listener;
    private int totalOrders;
    private boolean showLoading = true;
    private Map<Integer, List<OrdersListModel>> pageOrdersMap = new HashMap<>();
    private List<OrdersListModel> ordersListModels = new ArrayList<>();
    private boolean hideNameIcon;

    public HomeOrdersAdapter(Context context) {
        this.context = context;
    }

    public HomeOrdersAdapter(Context context, boolean hideNameIcon) {
        this.hideNameIcon = hideNameIcon;
        this.context = context;
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
            case 8:
                viewHolder = new ViewHolderReadyToPick(LayoutInflater.from(context).inflate(R.layout.itemview_orders_ready_to_pick, parent, false));
                break;
            case 7:
                viewHolder = new ViewHolderOnTheWay(LayoutInflater.from(context).inflate(R.layout.itemview_orders_on_the_way, parent, false));
                break;
            case 5:
                viewHolder = new ViewHolderDelivered(LayoutInflater.from(context).inflate(R.layout.itemview_orders_delivered, parent, false));
                break;
            case 2:
                viewHolder = new ViewHolderRejected(LayoutInflater.from(context).inflate(R.layout.itemview_orders_rejected, parent, false));
                break;
            case 6:
                viewHolder = new ViewHolderCanceled(LayoutInflater.from(context).inflate(R.layout.itemview_orders_rejected, parent, false));
                break;
            case 101:
                viewHolder = new ViewHolderLoading(LayoutInflater.from(context).inflate(R.layout.itemview_loading, parent, false));
                break;
            case 0:
            default:
                viewHolder = new ViewHolderPending(LayoutInflater.from(context).inflate(R.layout.itemview_orders_pending, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(position);
        if (holder instanceof ViewHolderLoading) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickOrder(holder.getAdapterPosition(), ordersListModels.get(position).getPageNumber());
                }
            }
        });
    }

    public void addUpdatePageWithOrders(int pageNumber, List<OrdersListModel> ordersListModels, int totalOrders) {
        this.showLoading = true;
        this.totalOrders = totalOrders;
        pageOrdersMap.put(pageNumber, ordersListModels);
        updateOrdersData();
        notifyDataSetChanged();
    }

    private void updateOrdersData() {
        this.ordersListModels = new ArrayList<>();
        for (List<OrdersListModel> ordersListModelsList : pageOrdersMap.values()) {
            this.ordersListModels.addAll(ordersListModelsList);
        }
    }

    public void setPageOrdersMap(Map<Integer, List<OrdersListModel>> pageOrdersMap) {
        this.showLoading = true;
        this.pageOrdersMap = pageOrdersMap;
        updateOrdersData();
        notifyDataSetChanged();
    }

    public List<OrdersListModel> getOrdersListModels() {
        return ordersListModels;
    }

    public void clearPageMap() {
        this.pageOrdersMap.clear();
        this.ordersListModels.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (position < ordersListModels.size()) {
                OrdersListModel model = ordersListModels.get(position);
                return Integer.parseInt(model.getStatus());
            }
        } catch (NumberFormatException e) {
            return 1;
        }
        return 101;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (showLoading) {

            if (pageOrdersMap.isEmpty()) {
                return 1;
            }

            if (ordersListModels.size() < totalOrders) {
                return ordersListModels.size() + 1;
            }
        }

        return ordersListModels.size();
    }

    public enum OrderType {
        ALL(9, "all"),
        PENDING(0, "pending"),
        ACCEPTED(1, "active"),
        SHIPPED(4, "shipped"),
        DELIVERED(5, "delivered"),
        REJECTED(2, "rejected"),
        CANCELED(6, "cancel"),
        READY_TO_BE_PICKED(8, "ready_to_be_picked"),
        ON_THE_WAY(7, "on_the_way");

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
        void onClickOrder(int position, int pageNumber);

        void onRejectOrder(int position, int pageNumber);

        void onAcceptOrder(int position, int pageNumber);

        void onShipOrder(int position, int pageNumber);

        void onReadyToBePicked(int position, int pageNumber);

        void onDeliverOrder(int position, int pageNumber);

        void onTheWayOrder(int position, int pageNumber);

        void onWhatsappMessage(int position);

        void onCallCustomer(int position);

        void onAssignRunner(String runnerId, int pageNumber, String orderId, String areaId);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewWhatsapp, imageViewPhoneCall;
        LinearLayout linearLayoutAssignRunner, linearLayoutAssigned;
        TextView textViewName, textViewOrderPlatform, textViewPrice, textViewOrderIdItemsTime, textViewRunnerName, textViewDeliveryType, textViewPaymentType;

        public ViewHolder(final View convertView) {
            super(convertView);
            textViewPrice = convertView.findViewById(R.id.tv_order_price);
            textViewOrderIdItemsTime = convertView.findViewById(R.id.tv_order_id_items_time);
            textViewName = convertView.findViewById(R.id.tv_store_name);
            textViewOrderPlatform = convertView.findViewById(R.id.tv_order_platform);
            textViewPaymentType = convertView.findViewById(R.id.tv_order_type);
            textViewDeliveryType = convertView.findViewById(R.id.tv_delivery_time_slot);
            imageViewWhatsapp = convertView.findViewById(R.id.iv_whatsapp);
            imageViewPhoneCall = convertView.findViewById(R.id.iv_phone_call);
            linearLayoutAssignRunner = convertView.findViewById(R.id.ll_assign_runner);
            linearLayoutAssigned = convertView.findViewById(R.id.ll_assigned);
            textViewRunnerName = convertView.findViewById(R.id.tv_runner_name);
        }

        public void bind(int position) {

            final OrdersListModel ordersModel = ordersListModels.get(position);

            String orderId = String.format("#%s", ordersModel.getDisplay_order_id());
            String itemText = ordersModel.getItems().size() > 1 ? "items" : "item";
            String item = String.format("(%s %s)", ordersModel.getItems().size(), itemText);

            textViewName.setVisibility(hideNameIcon ? View.GONE : View.VISIBLE);
            imageViewWhatsapp.setVisibility(hideNameIcon ? View.GONE : View.VISIBLE);
            imageViewPhoneCall.setVisibility(hideNameIcon ? View.GONE : View.VISIBLE);

            linearLayoutAssignRunner.setVisibility(View.GONE);
            linearLayoutAssigned.setVisibility(ordersModel.getRunnerId().equals("0") ? View.GONE : View.VISIBLE);
            linearLayoutAssigned.setClickable(false);

            if (!ordersModel.getOrderFacility().equalsIgnoreCase("delivery")) {
                linearLayoutAssigned.setVisibility(View.GONE);
                linearLayoutAssignRunner.setVisibility(View.GONE);
            }

            if (ordersModel.getRunnerDetail() != null) {
                textViewRunnerName.setText(ordersModel.getRunnerDetail().getFullName());
            } else {
                linearLayoutAssigned.setVisibility(View.GONE);
            }

            String platform = ordersModel.getPlatform();
            if (!TextUtils.isEmpty(platform)) {
                textViewOrderPlatform.setVisibility(View.VISIBLE);
                if (platform.equalsIgnoreCase("admin_android")) {
                    platform = "Admin App Order";
                } else if (platform.equalsIgnoreCase("web") || platform.equalsIgnoreCase("pwa")) {
                    platform = "Web Order";
                } else {
                    platform = "Customer App Order";
                }
            } else {
                textViewOrderPlatform.setVisibility(View.GONE);
            }

            textViewOrderPlatform.setText(platform);
            textViewOrderIdItemsTime.setText(String.format("%s %s | %s", orderId, item, ordersModel.getTime()));
            textViewName.setText(ordersModel.getCustomerName());
            textViewPrice.setText(String.format(Locale.getDefault(), "%s", Util.getPriceWithCurrency(ordersModel.getTotal(), AppPreference.getInstance().getCurrency())));
            textViewPaymentType.setText(ordersModel.getPaymentMethod().toUpperCase());
            textViewDeliveryType.setText(ordersModel.getOrderFacility());

            imageViewPhoneCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCallCustomer(getAdapterPosition());
                    }
                }
            });

            imageViewWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onWhatsappMessage(getAdapterPosition());
                    }
                }
            });

            linearLayoutAssignRunner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onAssignRunner(ordersModel.getRunnerId(), ordersModel.getPageNumber(), ordersModel.getOrderId(), ordersModel.getAreaId());
                    }
                }
            });

            linearLayoutAssigned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {

                        if (TextUtils.isEmpty(ordersModel.getRunnerAccepted())) {
                            return;
                        }

                        if (ordersModel.getRunnerAccepted().equals("0")) {
                            listener.onAssignRunner(ordersModel.getRunnerId(), ordersModel.getPageNumber(), ordersModel.getOrderId(), ordersModel.getAreaId());
                        } else {
                            Toast.makeText(context, "Runner has accepted order delivery!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
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
        public void bind(final int position) {
            super.bind(position);
            chipAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.sendBroadcast(new Intent(context, OrderTrackerBroadcast.class)
                            .setAction(OrderTrackerBroadcast.INTENT_ACTION_DISMISS_SOUND));
                    if (listener != null) {
                        listener.onAcceptOrder(getAdapterPosition(), ordersListModels.get(position).getPageNumber());
                    }
                }
            });
            chipReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   context.sendBroadcast(new Intent(context, OrderTrackerBroadcast.class)
                            .setAction(OrderTrackerBroadcast.INTENT_ACTION_DISMISS_SOUND));
                    if (listener != null) {
                        listener.onRejectOrder(getAdapterPosition(), ordersListModels.get(position).getPageNumber());
                    }
                }
            });
        }
    }

    class ViewHolderAccepted extends ViewHolder {
        Chip chipReady;
        Chip chipReject;

        public ViewHolderAccepted(final View convertView) {
            super(convertView);
            chipReady = convertView.findViewById(R.id.chip_ready_to_pick);
            chipReject = convertView.findViewById(R.id.chip_reject);
            chipReject.setVisibility(View.GONE);
        }

        @Override
        public void bind(final int position) {
            super.bind(position);
            chipReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onReadyToBePicked(getAdapterPosition(), ordersListModels.get(position).getPageNumber());
                    }
                }
            });
            chipReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRejectOrder(getAdapterPosition(), ordersListModels.get(position).getPageNumber());
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
        public void bind(final int position) {
            super.bind(position);
            chipDeliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeliverOrder(getAdapterPosition(), ordersListModels.get(position).getPageNumber());
                    }
                }
            });
            chipReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRejectOrder(getAdapterPosition(), ordersListModels.get(position).getPageNumber());
                    }
                }
            });
        }
    }

    class ViewHolderOnTheWay extends ViewHolder {
        View convertView;
        Chip chipDeliver;

        public ViewHolderOnTheWay(final View convertView) {
            super(convertView);
            this.convertView = convertView;
        }

        @Override
        public void bind(int position) {
            super.bind(position);
        }
    }

    class ViewHolderReadyToPick extends ViewHolder {
        View convertView;
        Chip chipDeliver;

        public ViewHolderReadyToPick(final View convertView) {
            super(convertView);
            this.convertView = convertView;
            chipDeliver = convertView.findViewById(R.id.chip_deliver);
        }

        @Override
        public void bind(final int position) {
            super.bind(position);

            if (!ordersListModels.get(position).getOrderFacility().equalsIgnoreCase("delivery")) {
                chipDeliver.setVisibility(View.VISIBLE);
            } else {
                chipDeliver.setVisibility(View.GONE);
            }

            chipDeliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeliverOrder(getAdapterPosition(), ordersListModels.get(position).getPageNumber());
                    }
                }
            });
        }
    }

    class ViewHolderDelivered extends ViewHolder {
        View convertView;

        public ViewHolderDelivered(final View convertView) {
            super(convertView);
            this.convertView = convertView;
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            convertView.findViewById(R.id.ll_assign_runner).setVisibility(View.GONE);
            convertView.findViewById(R.id.ll_assigned).setClickable(false);
        }
    }

    class ViewHolderRejected extends ViewHolder {
        View convertView;

        public ViewHolderRejected(final View convertView) {
            super(convertView);
            this.convertView = convertView;
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            convertView.findViewById(R.id.ll_assign_runner).setVisibility(View.GONE);
            convertView.findViewById(R.id.ll_assigned).setClickable(false);
        }
    }

    class ViewHolderCanceled extends ViewHolder {
        View convertView;

        public ViewHolderCanceled(final View convertView) {
            super(convertView);
            this.convertView = convertView;
            TextView textViewStatus = convertView.findViewById(R.id.tv_order_status);
            textViewStatus.setText("Cancelled");
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            convertView.findViewById(R.id.ll_assign_runner).setVisibility(View.GONE);
            convertView.findViewById(R.id.ll_assigned).setClickable(false);
        }
    }

    class ViewHolderLoading extends ViewHolder {

        public ViewHolderLoading(final View convertView) {
            super(convertView);
        }

        @Override
        public void bind(int position) {
        }
    }
}
