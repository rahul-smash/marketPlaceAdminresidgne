package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.util.Util;

import java.util.List;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private static final String TAG = OrdersAdapter.class.getSimpleName();

    // Define listener member variable
    private static OrdersAdapter.OnItemClickListener listener;
    private final LayoutInflater mInflater;
    private Context context;
    /*Data */
    private List<OrdersListModel> listOrder;


    public OrdersAdapter(Context context, List<OrdersListModel> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
        this.mInflater = LayoutInflater.from(context);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OrdersAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateListItem(List<OrdersListModel> listOrder) {
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }

    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.itemview_orders_pending, parent, false);
        OrdersAdapter.MyViewHolder holder = new OrdersAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrdersAdapter.MyViewHolder holder, final int position) {
        final OrdersListModel order = listOrder.get(position);
        //holder.txtOrderId.setText(order.getOrderId());//changed by sachinClient call
        Log.i("@@ChangedOrderID", order.getDisplay_order_id());


        holder.txt_Price.setText(Util.getCurrency(context) + Util.getDoubleValue(order.getTotal()));
        holder.txt_Name.setText(order.getCustomerName());
        holder.txtorderId.setText("Order- " + order.getDisplay_order_id());
        holder.txtDate.setText(order.getTime());
        if (order.getStatus().equalsIgnoreCase("1")) {
            holder.txtStatus.setText("Processing");
            //  holder.txtStatus.setBackgroundResource(R.drawable.shape_button_processing);
        } else if (order.getStatus().equalsIgnoreCase("4")) {
            holder.txtStatus.setText("Shipping");
            //holder.txtStatus.setBackgroundResource(R.drawable.shape_button_shipping);
        } else if (order.getStatus().equalsIgnoreCase("5")) {
            Log.i("@@DeleiverdOrder__", "order.getStatus()");
            holder.txtStatus.setText("Delivered");
            //holder.txtStatus.setBackgroundResource(R.drawable.shape_button_delivered);
        } else if (order.getStatus().equalsIgnoreCase("0")) {
            holder.txtStatus.setText("Due Orders");
            //holder.txtStatus.setBackgroundResource(R.drawable.shape_button_due_orders);
        } else if (order.getStatus().equalsIgnoreCase("2")) {
            holder.txtStatus.setText("Rejected");
            //holder.txtStatus.setBackgroundResource(R.drawable.shape_button_rejected);
        } else if (order.getStatus().equalsIgnoreCase("6")) {
            holder.txtStatus.setText("Cancelled");
            //holder.txtStatus.setBackgroundResource(R.drawable.shape_button_rejected);
        }
        holder.txt_items.setText(String.valueOf(order.getItems().size() + " Items "));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(holder.imgArrow, position, order);
            }
        });

        holder.imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(holder.imgArrow, position, order);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i("@@OrderSize___", "" + listOrder.size());
        return listOrder.size();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, OrdersListModel order);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgArrow;
        TextView txt_Name, txtStatus, txt_Price, txtorderId, txtDate, txt_items;

        public MyViewHolder(final View convertView) {
            super(convertView);
            txt_items = (TextView) convertView.findViewById(R.id.tv_items_count);
            txt_Price = (TextView) convertView.findViewById(R.id.tv_order_price);
            imgProduct = (ImageView) convertView.findViewById(R.id.iv_product);
            txtorderId = (TextView) convertView.findViewById(R.id.tv_order_id);
            txt_Name = (TextView) convertView.findViewById(R.id.tv_store_name);
            txtDate = (TextView) convertView.findViewById(R.id.tv_order_date_time);
            txtStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
            imgArrow = (ImageView) convertView.findViewById(R.id.iv_next_arrow);
        }

    }
}
