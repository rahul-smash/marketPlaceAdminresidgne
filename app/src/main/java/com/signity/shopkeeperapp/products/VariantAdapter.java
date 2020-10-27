package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private VariantListener listener;

    public VariantAdapter(Context context) {
        this.context = context;
    }

    public void setVariantList(List<Map<String, String>> variantList) {
        this.variantList = variantList;
        notifyDataSetChanged();
    }

    public void setListener(VariantListener listener) {
        this.listener = listener;
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

    public interface VariantListener {
        void onClickVariant(Map<String, String> variant);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVariantDetail, textViewVariantPrice, textViewVariantPriceFinal, textViewVariantDiscount;
        ImageView imageViewDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVariantDetail = itemView.findViewById(R.id.tv_variant_details);
            imageViewDelete = itemView.findViewById(R.id.iv_more);
            textViewVariantPrice = itemView.findViewById(R.id.tv_variant_price);
            textViewVariantPriceFinal = itemView.findViewById(R.id.tv_variant_price_final);
            textViewVariantDiscount = itemView.findViewById(R.id.tv_variant_discount);
            textViewVariantPrice.setPaintFlags(textViewVariantPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public void bind(int positon) {
            final Map<String, String> variant = variantList.get(positon);

            String weight = TextUtils.isEmpty(variant.get("weight")) ? "" : variant.get("weight");
            String unit = TextUtils.isEmpty(variant.get("unit_type")) ? "" : variant.get("unit_type");
            textViewVariantDetail.setText(String.format("Weight - %s %s", weight, unit));
            textViewVariantPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(TextUtils.isEmpty(variant.get("mrp_price")) ? "0" : variant.get("mrp_price")), AppPreference.getInstance().getCurrency()));
            textViewVariantPriceFinal.setText(Util.getPriceWithCurrency(Double.parseDouble(TextUtils.isEmpty(variant.get("price")) ? "0" : variant.get("price")), AppPreference.getInstance().getCurrency()));
            textViewVariantDiscount.setText(String.format("Discount - %s", TextUtils.isEmpty(variant.get("discount")) ? "0" : variant.get("discount")).concat("%"));

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    variantList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickVariant(variantList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
