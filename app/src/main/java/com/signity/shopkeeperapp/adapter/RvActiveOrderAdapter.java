package com.signity.shopkeeperapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.util.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rajesh on 27/6/16.
 */
public class RvActiveOrderAdapter extends RecyclerView.Adapter<RvActiveOrderAdapter.MyViewHolder> {

    private static final String TAG = RvActiveOrderAdapter.class.getSimpleName();

    // Define listener member variable
    private static OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, OrdersListModel order);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private final LayoutInflater mInflater;


    private Context context;

    /*Data */
    private List<OrdersListModel> listOrder;


    public RvActiveOrderAdapter(Context context, List<OrdersListModel> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateListItem(List<OrdersListModel> listOrder) {
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_row_active_orders, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OrdersListModel order = listOrder.get(position);
        holder.txtOrderId.setText(order.getOrderId());
        if (order.getCustomerName().equalsIgnoreCase("") || order.getCustomerName().equals(null)) {
            holder.txtCustName.setText("Guest User");
        } else {
            holder.txtCustName.setText(order.getCustomerName());
        }
        Double totalPrice = 0.00;
        Double itemsPrice = 0.00;

//        for (int i = 0; i < order.getItems().size(); i++) {
//            if (order.getItems().get(i).getStatus().equalsIgnoreCase("1")) {
//                itemsPrice = itemsPrice + (order.getItems().get(i).getPrice() * Integer.parseInt(order.getItems().get(i).getQuantity()));
//            }
//        }
//        totalPrice = ((itemsPrice + order.getShippingCharges()) - order.getDiscount()) + order.getTax();
        holder.txtTotalAmount.setText(Util.getCurrency(context) + Util.getDoubleValue(order.getTotal()));
            String dateStr = order.getTime();
            DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm",Locale.US);
        Date date = null;
        try {
            date = df.parse(dateStr);
            Log.i("@@TimeZone",""+date);

            Log.i("@@TimeZone",""+TimeZone.getTimeZone("America/Adak"));
            df.setTimeZone(TimeZone.getTimeZone("America/Adak"));;
            String formattedDate = df.format(date);
            Log.i("@@Order_getTime", "" + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.i("@@Order_getTime", "" + order.getTime());
        holder.txtTime.setText(order.getTime());
        if (order.getStatus().equalsIgnoreCase("1")) {
            holder.status.setText("Processing");
            holder.status.setBackgroundResource(R.drawable.shape_button_processing);
        } else if (order.getStatus().equalsIgnoreCase("4")) {
            holder.status.setText("Shipping");
            holder.status.setBackgroundResource(R.drawable.shape_button_shipping);
        } else if (order.getStatus().equalsIgnoreCase("5")) {
            holder.status.setText("Delivered");
            holder.status.setBackgroundResource(R.drawable.shape_button_delivered);
        } else if (order.getStatus().equalsIgnoreCase("0")) {
            holder.status.setText("Due Orders");
            holder.status.setBackgroundResource(R.drawable.shape_button_due_orders);
        } else if (order.getStatus().equalsIgnoreCase("2")) {
            holder.status.setText("Rejected");
            holder.status.setBackgroundResource(R.drawable.shape_button_rejected);
        } else if (order.getStatus().equalsIgnoreCase("6")) {
            holder.status.setText("Cancelled");
            holder.status.setBackgroundResource(R.drawable.shape_button_rejected);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(holder.parent, position, order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderId;
        TextView txtCustName;
        TextView txtTotalAmount;
        TextView txtTime;
        TextView status;
        RelativeLayout parent;

        public MyViewHolder(final View convertView) {
            super(convertView);

            txtOrderId = (TextView) convertView.findViewById(R.id.valOdrId);
            txtCustName = (TextView) convertView.findViewById(R.id.valCustName);
            txtTotalAmount = (TextView) convertView.findViewById(R.id.valTotalAmt);
            txtTime = (TextView) convertView.findViewById(R.id.valTime);
            status = (TextView) convertView.findViewById(R.id.txtStausVal);
            parent = (RelativeLayout) convertView.findViewById(R.id.parent);


        }

    }
}
