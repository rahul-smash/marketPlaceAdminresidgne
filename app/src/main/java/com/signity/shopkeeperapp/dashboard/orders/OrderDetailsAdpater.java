package com.signity.shopkeeperapp.dashboard.orders;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailsAdpater extends RecyclerView.Adapter<OrderDetailsAdpater.MyViewHolder> {

    private static final String TAG = "OrderDetaislAdpater";
    private boolean canChange;
    private OrderDetailListener listener;
    private Context context;
    private List<ItemListModel> itemListModels = new ArrayList<>();

    public OrderDetailsAdpater(Context context) {
        this.context = context;
    }

    public void setItemListModels(List<ItemListModel> itemListModels, boolean canChange) {
        this.canChange = canChange;
        this.itemListModels = itemListModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailsAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_order_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailsAdpater.MyViewHolder holder, final int position) {
        final ItemListModel itemListModel = itemListModels.get(position);

        holder.textViewItemName.setText(itemListModel.getName());
        holder.textViewWeight.setText(String.format("Weight: %s %s", itemListModel.getWeight(), itemListModel.getUnitType()));
        holder.textViewQuantity.setText(itemListModel.getQuantity());
        holder.textViewProductPrice.setText(String.format(Locale.getDefault(), "x %.2f", itemListModel.getPrice()));
        double total = Integer.parseInt(itemListModel.getQuantity()) * itemListModel.getPrice();
        holder.textViewTotal.setText(Util.getPriceWithCurrency(total, AppPreference.getInstance().getCurrency()));
        holder.textViewStatus.setText(itemListModel.getStatus().equals("2") ? "Reject" : "Accept");
        holder.switchItem.setChecked(!itemListModel.getStatus().equals("2"));
        holder.switchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChangeStatus(itemListModel.getItemId(), itemListModel.getStatus().equals("2") ? "1" : "2");
                }
            }
        });
        if (!TextUtils.isEmpty(itemListModel.getComment())) {
            holder.linearLayoutItemComment.setVisibility(View.VISIBLE);
            holder.textViewItemComment.setText(String.format("Comment: %s", itemListModel.getComment()));
        }

        // restrict user to cancel the item
        holder.switchItem.setEnabled(false);

        if (!TextUtils.isEmpty(itemListModel.getImageSmall())) {
            Picasso.with(context)
                    .load(itemListModel.getImageSmall())
                    .error(R.drawable.addimageicon)
                    .placeholder(ResourcesCompat.getDrawable(context.getResources(), R.drawable.addimageicon, null))
                    .into(holder.imageViewItem);
        } else {
            holder.imageViewItem.setImageResource(R.drawable.addimageicon);
        }
    }

    @Override
    public int getItemCount() {
        return itemListModels.size();
    }

    public void setListener(OrderDetailListener listener) {
        this.listener = listener;
    }

    public interface OrderDetailListener {
        void onChangeStatus(String itemId, String status);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutItemComment;
        TextView textViewItemComment;
        TextView textViewItemName, textViewQuantity, textViewWeight, textViewTotal, textViewStatus, textViewProductPrice;
        ImageView imageViewItem;
        Switch switchItem;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageViewItem = convertView.findViewById(R.id.iv_product_image);
            textViewItemName = convertView.findViewById(R.id.tv_product_name);
            textViewItemComment = convertView.findViewById(R.id.tv_item_comment);
            linearLayoutItemComment = convertView.findViewById(R.id.ll_item_comment);
            textViewQuantity = convertView.findViewById(R.id.tv_product_quantity);
            textViewWeight = convertView.findViewById(R.id.tv_product_weight);
            textViewTotal = convertView.findViewById(R.id.tv_product_total);
            textViewProductPrice = convertView.findViewById(R.id.tv_product_price);
            textViewStatus = convertView.findViewById(R.id.tv_product_status);
            switchItem = convertView.findViewById(R.id.switch_product);
        }
    }
}
