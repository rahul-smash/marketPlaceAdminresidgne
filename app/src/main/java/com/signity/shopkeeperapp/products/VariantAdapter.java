package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.List;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {

    private Context context;
    private List<Variant> variantList = new ArrayList<>();

    public VariantAdapter(Context context) {
        this.context = context;
    }

    public void setVariantList(List<Variant> variantList) {
        this.variantList = variantList;
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
            Variant variant = variantList.get(positon);
            textViewVariantDetail.setText(String.format("Weight - %s %s", variant.getWeight(), variant.getUnitType()));
            textViewVariantPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(variant.getMrpPrice()), AppPreference.getInstance().getCurrency()));
            textViewVariantPriceFinal.setText(Util.getPriceWithCurrency(Double.parseDouble(variant.getPrice()), AppPreference.getInstance().getCurrency()));
            textViewVariantDiscount.setText(String.format("Discount - %s", variant.getDiscount()).concat("%"));
        }
    }
}
