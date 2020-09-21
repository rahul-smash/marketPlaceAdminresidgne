package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;

import java.util.List;

public class OrderDetaislAdpater extends RecyclerView.Adapter<OrderDetaislAdpater.MyViewHolder> {

    private static final String TAG = OrderDetaislAdpater.class.getSimpleName();

    // Define listener member variable
    private static OrderDetaislAdpater.OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, OrdersListModel order);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OrderDetaislAdpater.OnItemClickListener listener) {
        this.listener = listener;
    }


    private final LayoutInflater mInflater;


    private Context context;

    /*Data */
    private List<OrdersListModel> listOrder;


    public OrderDetaislAdpater(Context context, List<OrdersListModel> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateListItem(List<OrdersListModel> listOrder) {
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }

    @Override
    public OrderDetaislAdpater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.itemview_order_detail, parent, false);
        OrderDetaislAdpater.MyViewHolder holder = new OrderDetaislAdpater.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrderDetaislAdpater.MyViewHolder holder, final int position) {
        final OrdersListModel order = listOrder.get(position);



       /* holder.itemView_Parent.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    @Override
    public int getItemCount() {
        Log.i("@@OrderSize___", "" + listOrder.size());
        return listOrder.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(final View convertView) {
            super(convertView);

        }

    }
}
