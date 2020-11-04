package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class BookOrderCheckoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GetProductData> productData = new ArrayList<>();

    public BookOrderCheckoutAdapter(Context context) {
        this.context = context;
    }

    public void setProductData(List<GetProductData> productData) {
        this.productData = productData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_book_order_checkout, parent, false);
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
            final GetProductData getProductData = productData.get(position);

            textViewProductName.setText(getProductData.getTitle());

            List<Variant> varientDataList = getProductData.getVariants();
            Variant variantData = varientDataList.get(getProductData.getSelectedVariantIndex());

            textViewProductPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(variantData.getPrice()), AppPreference.getInstance().getCurrency()));

            textViewProductQuantity.setText(String.valueOf(getProductData.getCount()));
            double total = getProductData.getCount() * Double.parseDouble(variantData.getPrice());
            textViewProductTotalPrice.setText(Util.getPriceWithCurrency(total, AppPreference.getInstance().getCurrency()));

            textViewWeight.setText(String.format("Weight: %s %s", variantData.getWeight(), variantData.getUnitType()));
        }
    }
}
