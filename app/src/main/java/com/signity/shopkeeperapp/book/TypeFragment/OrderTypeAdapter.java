package com.signity.shopkeeperapp.book.TypeFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.signity.shopkeeperapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<OrderType> orderTypes = new ArrayList<>();
    private int selectedId = OrderType.DELIVERY.ordinal();
    private OrderTypeListener listener;

    public OrderTypeAdapter(Context context, OrderTypeListener listener) {
        this.context = context;
        this.listener = listener;
        this.orderTypes.addAll(Arrays.asList(OrderType.values()));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_order_type_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return orderTypes.size();
    }

    public enum OrderType {
        DELIVERY("Delivery"), PICKUP("Pick Up"), DINEIN("Dine In");

        private String title;

        OrderType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public interface OrderTypeListener {
        void onSelectOrderType(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_order_type);
            imageView = itemView.findViewById(R.id.iv_check);
            materialCardView = itemView.findViewById(R.id.card_order_type);
        }

        public void onBind(int position) {

            textView.setText(orderTypes.get(position).getTitle());
            imageView.setVisibility(selectedId == position ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelectOrderType(getAdapterPosition());
                    selectedId = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

}
