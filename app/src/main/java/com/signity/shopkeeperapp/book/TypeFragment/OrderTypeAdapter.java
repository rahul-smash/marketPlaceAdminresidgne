package com.signity.shopkeeperapp.book.TypeFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        DELIVERY("Delivery", R.drawable.deliverybg), PICKUP("Pick Up", R.drawable.pickupbg);
//        DINEIN("Dine In", R.drawable.dineinbg);

        private String title;
        private int icon;

        OrderType(String title, int deliverybg) {
            this.title = title;
            this.icon = deliverybg;
        }

        public int getIcon() {
            return icon;
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
        LinearLayout imageView;
        LinearLayout materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_order_type);
            imageView = itemView.findViewById(R.id.iv_check);
            materialCardView = itemView.findViewById(R.id.ll_order_type);
        }

        public void onBind(int position) {

            textView.setText(orderTypes.get(position).getTitle());
            imageView.setVisibility(selectedId == position ? View.VISIBLE : View.GONE);
            materialCardView.setBackground(context.getResources().getDrawable(orderTypes.get(position).getIcon()));

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
