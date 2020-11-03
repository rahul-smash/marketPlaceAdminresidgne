package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private OrderCartListener listener;

    public BestSellerAdapter(Context context, OrderCartListener listener) {
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
        TextView textViewCount;
        LinearLayout linearLayoutAdd, linearLayoutMinus, linearLayoutCountMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.iv_product_image);
            textViewProductName = itemView.findViewById(R.id.tv_product_name);
            textViewProductPrice = itemView.findViewById(R.id.tv_product_price);
            textViewCount = itemView.findViewById(R.id.tv_count);
            linearLayoutAdd = itemView.findViewById(R.id.ll_add);
            linearLayoutMinus = itemView.findViewById(R.id.ll_minus);
            linearLayoutCountMinus = itemView.findViewById(R.id.ll_count_minus);
        }

        public void onBind(int position) {
            final GetProductData getProductData = productData.get(position);

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

            linearLayoutCountMinus.setVisibility(getProductData.isSelected() ? View.VISIBLE : View.GONE);
            textViewCount.setText(String.valueOf(getProductData.getCount()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = getProductData.getCount();

                    if (count >= 20) {
                        return;
                    }

                    getProductData.setCount(++count);
                    getProductData.setSelected(true);
                    notifyItemChanged(getAdapterPosition(), getProductData);

                    listener.onAddProduct(getProductData);
                }
            });

            linearLayoutMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = getProductData.getCount() - 1;
                    if (count < 1) {
                        getProductData.setSelected(false);
                        getProductData.setCount(0);
                    } else {
                        getProductData.setCount(count);
                    }
                    notifyItemChanged(getAdapterPosition(), getProductData);
                    listener.onRemoveProduct(getProductData);
                }
            });
        }
    }
}
