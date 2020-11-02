package com.signity.shopkeeperapp.book;

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
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BestSellerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GetProductData> productData = new ArrayList<>();

    public BestSellerAdapter(Context context) {
        this.context = context;
    }

    public void addProductData(List<GetProductData> productData) {
        this.productData.addAll(productData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_book_order, parent, false);
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

        ImageView imageViewProduct;
        TextView textViewProductName, textViewProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.iv_product_image);
        }

        public void onBind(int position) {
            GetProductData getProductData = productData.get(position);

            if (!TextUtils.isEmpty(getProductData.getImage10080())) {
                Picasso.with(context)
                        .load(getProductData.getImage10080())
                        .placeholder(context.getResources().getDrawable(R.drawable.addimageicon))
                        .into(imageViewProduct);
            }

            textViewProductName.setText(getProductData.getTitle());

            List<Variant> varientDataList = getProductData.getVariants();
            Variant variantData = varientDataList.get(0);

            textViewProductPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(variantData.getPrice()), AppPreference.getInstance().getCurrency()));
        }
    }
}
