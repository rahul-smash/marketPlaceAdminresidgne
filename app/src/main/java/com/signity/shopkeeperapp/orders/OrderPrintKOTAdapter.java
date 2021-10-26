package com.signity.shopkeeperapp.orders;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class OrderPrintKOTAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ItemListModel> productData = new ArrayList<>();

    public OrderPrintKOTAdapter(Context context) {
        this.context = context;
    }

    public void setProductData(List<ItemListModel> productData) {
        this.productData = productData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_order_receipt_kot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProductName, textViewProductQuantity, textViewWeight, textViewComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.tv_product_name);
            textViewProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
            textViewWeight = itemView.findViewById(R.id.tv_product_weight);
            textViewComment = itemView.findViewById(R.id.tv_product_comment);
        }

        public void onBind(int position) {
            final ItemListModel getProductData = productData.get(position);

            textViewProductName.setText(Util.toTitleCase(getProductData.getName()));
            textViewProductQuantity.setText(String.valueOf(getProductData.getQuantity()));
            textViewComment.setText(String.valueOf(getProductData.getComment()));
            textViewComment.setVisibility(TextUtils.isEmpty(getProductData.getComment()) ? View.GONE : View.VISIBLE);
            boolean isWeightUnitFound = false;
            if (!TextUtils.isEmpty(getProductData.getWeight().trim()) && getProductData.getWeight().trim().length() > 0)
                isWeightUnitFound = true;

            if (TextUtils.isEmpty(getProductData.getUnitType().trim()) && getProductData.getUnitType().trim().length() > 0)
                isWeightUnitFound = true;

            if (isWeightUnitFound) {
                textViewWeight.setVisibility(View.VISIBLE);
                textViewWeight.setText(String.format("Weight: %s %s", getProductData.getWeight(), getProductData.getUnitType()));
            } else {
                textViewWeight.setVisibility(View.GONE);
            }
        }
    }
}
