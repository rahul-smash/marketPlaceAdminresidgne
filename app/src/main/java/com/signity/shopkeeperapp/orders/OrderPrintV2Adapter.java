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
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class OrderPrintV2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ItemListModel> productData = new ArrayList<>();

    public OrderPrintV2Adapter(Context context) {
        this.context = context;
    }

    public void setProductData(List<ItemListModel> productData) {
        this.productData = productData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_receipt_order_product, parent, false);
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

        TextView textViewProductName, textViewProductPrice, textViewProductTotalPrice, textViewProductQuantity, textViewWeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.tv_product_name);
            textViewProductPrice = itemView.findViewById(R.id.tv_product_price);
            textViewProductTotalPrice = itemView.findViewById(R.id.tv_product_total);
            textViewProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
            textViewWeight = itemView.findViewById(R.id.tv_product_weight);
        }

        public void onBind(int position) {
            final ItemListModel getProductData = productData.get(position);

            textViewProductName.setText(Util.toTitleCase(getProductData.getName()));

            String productPrice = Util.getPriceWithCurrency(getProductData.getPrice(), AppPreference.getInstance().getCurrency());
            textViewProductPrice.setText(String.format("%s", productPrice));

            textViewProductQuantity.setText(String.valueOf(getProductData.getQuantity()));
            double total = Integer.parseInt(getProductData.getQuantity()) * getProductData.getPrice();
            textViewProductTotalPrice.setText(Util.getPriceWithCurrency(total, AppPreference.getInstance().getCurrency()));
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

        public String capitalize(String s) {
            if (s.length() == 0) return s;
            return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }

    }
}
