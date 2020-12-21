package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GetProductData> productData = new ArrayList<>();
    private OnProductSelection listener;

    public ProductGalleryAdapter(Context context, OnProductSelection listener) {
        this.context = context;
        this.listener = listener;
    }

    public void addProductData(List<GetProductData> productData) {
        this.productData.addAll(productData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_product_gallery, parent, false);
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

    public void clearData() {
        this.productData.clear();
        notifyDataSetChanged();
    }

    public interface OnProductSelection {
        void onProductSelected(GetProductData getProductData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProduct;
        TextView textViewProductName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.iv_product_image);
            textViewProductName = itemView.findViewById(R.id.tv_product_name);
        }

        public void onBind(int position) {
            final GetProductData getProductData = productData.get(position);

            if (!TextUtils.isEmpty(getProductData.getImage300200())) {
                Picasso.with(context)
                        .load(getProductData.getImage300200())
                        .placeholder(context.getResources().getDrawable(R.drawable.addimageicon))
                        .into(imageViewProduct);
            }
            textViewProductName.setText(getProductData.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onProductSelected(getProductData);
                    }
                }
            });
        }

    }
}
