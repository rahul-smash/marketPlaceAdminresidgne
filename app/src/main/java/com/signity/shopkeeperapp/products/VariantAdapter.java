package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {

    private Context context;

    public VariantAdapter(Context context) {
        this.context = context;
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
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVariantDetail, textViewVariantPrice, textViewVariantPriceFinal, textViewVariantDiscount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVariantDetail = itemView.findViewById(R.id.tv_variant_details);
            textViewVariantPrice = itemView.findViewById(R.id.tv_variant_price);
            textViewVariantPriceFinal = itemView.findViewById(R.id.tv_variant_price_final);
            textViewVariantDiscount = itemView.findViewById(R.id.tv_variant_discount);
        }

        public void bind(int positon) {

        }
    }
}
