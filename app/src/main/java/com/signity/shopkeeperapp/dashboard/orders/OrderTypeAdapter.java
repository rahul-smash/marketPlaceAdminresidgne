package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderTypeAdapter extends RecyclerView.Adapter<OrderTypeAdapter.ViewHolder> {

    private static final String TAG = "OrderTypeAdapter";
    private Context context;
    private List<OrderType> orderTypes = new ArrayList<>();
    private OrderTypeListener listener;
    private int selectedType;

    public OrderTypeAdapter(Context context) {
        this.context = context;
        orderTypes.addAll(Arrays.asList(OrderType.values()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_order_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return orderTypes.size();
    }

    public void setListener(OrderTypeListener listener) {
        this.listener = listener;
    }

    public enum OrderType {
        ALL, PENDING, ACCEPTED, SHIPPED, DELIVERED
    }

    public interface OrderTypeListener {
        void onClickOrderType(OrderType orderType);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ViewHolder(final View convertView) {
            super(convertView);
            chip = convertView.findViewById(R.id.chip);
        }

        public void bind(int position) {

            final OrderType orderType = orderTypes.get(position);

            chip.setText(orderType.name());
            chip.setChecked(selectedType == position);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickOrderType(orderType);
                    }
                    selectedType = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) chip.getLayoutParams();
            if (position == 0) {
                layoutParams.setMargins((int) Util.pxFromDp(context, 16), 0, 0, 0);
            } else if (position == orderTypes.size() - 1) {
                layoutParams.setMargins((int) Util.pxFromDp(context, 8), 0, (int) Util.pxFromDp(context, 16), 0);
            } else {
                layoutParams.setMargins((int) Util.pxFromDp(context, 8), 0, 0, 0);
            }
            chip.setLayoutParams(layoutParams);
        }
    }
}
