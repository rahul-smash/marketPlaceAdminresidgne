package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {

    private Context context;
    private List<Map<String, String>> variantList = new ArrayList<>();

    public VariantAdapter(Context context) {
        this.context = context;
    }

    public void setVariantList(List<Map<String, String>> variantList) {
        this.variantList = variantList;
        notifyDataSetChanged();
    }

    public void addVariantList(List<Map<String, String>> variantList) {
        this.variantList.addAll(variantList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VariantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_variant, parent, false);
        return new VariantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariantAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return variantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVariantDetail, textViewVariantPrice, textViewVariantPriceFinal, textViewVariantDiscount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVariantDetail = itemView.findViewById(R.id.tv_variant_details);
            textViewVariantPrice = itemView.findViewById(R.id.tv_variant_price);
            textViewVariantPriceFinal = itemView.findViewById(R.id.tv_variant_price_final);
            textViewVariantDiscount = itemView.findViewById(R.id.tv_variant_discount);
            textViewVariantPrice.setPaintFlags(textViewVariantPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public void bind(int positon) {
            Map<String, String> variant = variantList.get(positon);

            textViewVariantDetail.setText(String.format("Weight - %s %s", variant.get("weight"), variant.get("unit_type")));
            textViewVariantPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(TextUtils.isEmpty(variant.get("mrp_price")) ? "0" : variant.get("mrp_price")), AppPreference.getInstance().getCurrency()));
            textViewVariantPriceFinal.setText(Util.getPriceWithCurrency(Double.parseDouble(TextUtils.isEmpty(variant.get("price")) ? "0" : variant.get("price")), AppPreference.getInstance().getCurrency()));
            textViewVariantDiscount.setText(String.format("Discount - %s", TextUtils.isEmpty(variant.get("discount")) ? "0" : variant.get("discount")).concat("%"));
        }
    }
}
