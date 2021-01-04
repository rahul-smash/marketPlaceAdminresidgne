package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    public void clearData() {
        this.productData.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProduct;
        TextView textViewProductName, textViewProductPrice, textViewProductVariantData;
        TextView textViewCount;
        LinearLayout linearLayoutAdd, linearLayoutMinus, linearLayoutCountMinus, linearLayoutOutStock;
        Spinner spinnerOrders;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.iv_product_image);
            textViewProductName = itemView.findViewById(R.id.tv_product_name);
            textViewProductPrice = itemView.findViewById(R.id.tv_product_price);
            textViewCount = itemView.findViewById(R.id.tv_count);
            linearLayoutAdd = itemView.findViewById(R.id.ll_add);
            linearLayoutMinus = itemView.findViewById(R.id.ll_minus);
            linearLayoutCountMinus = itemView.findViewById(R.id.ll_count_minus);
            linearLayoutOutStock = itemView.findViewById(R.id.ll_out_stock);
            spinnerOrders = itemView.findViewById(R.id.spinner_orders);
            textViewProductVariantData = itemView.findViewById(R.id.tv_product_detail);
        }

        public void onBind(int position) {
            final GetProductData getProductData = productData.get(position);

            if (!TextUtils.isEmpty(getProductData.getImage10080())) {
                Picasso.with(context)
                        .load(getProductData.getImage10080())
                        .placeholder(context.getResources().getDrawable(R.drawable.addimageicon))
                        .into(imageViewProduct);
            }

            List<String> variantList = new ArrayList<>();
            if (getProductData.getVariants() != null) {
                for (Variant variant : getProductData.getVariants()) {
                    variantList.add(String.format("%s %s", variant.getWeight(), variant.getUnitType()));
                }
            }

            if (getProductData.getVariants() != null) {
                spinnerOrders.setVisibility(getProductData.getVariants().size() > 1 ? View.VISIBLE : View.GONE);
            }

            final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, variantList);
            spinnerOrders.setAdapter(stringArrayAdapter);
            spinnerOrders.setSelection(getProductData.getSelectedVariantIndex());
            spinnerOrders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (getProductData.getSelectedVariantIndex() != position) {
                        getProductData.setSelectedVariantIndex(position);
                        getProductData.setCount(0);
                        getProductData.setSelected(false);
                        notifyItemChanged(getAdapterPosition(), getProductData);
                        listener.onRemoveProduct(getProductData);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            textViewProductName.setText(getProductData.getTitle());

            List<Variant> varientDataList = getProductData.getVariants();
            Variant variantData = varientDataList.get(getProductData.getSelectedVariantIndex());
            boolean showOutOfStock = false;
            if (!TextUtils.isEmpty(variantData.getSellingChooser()) && !variantData.getSellingChooser().equalsIgnoreCase("continue_selling")) {
                if (!TextUtils.isEmpty(variantData.getStock()) && !TextUtils.isEmpty(variantData.getMinStock())) {
                    try {
                        showOutOfStock = isOutOfStock(variantData.getStock(), variantData.getMinStock());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            textViewProductVariantData.setText(String.format("%s %s", variantData.getWeight(), variantData.getUnitType()));
            linearLayoutOutStock.setVisibility(showOutOfStock ? View.VISIBLE : View.GONE);
            textViewProductPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(!TextUtils.isEmpty(variantData.getPrice()) ? variantData.getPrice() : "0"), AppPreference.getInstance().getCurrency()));

            linearLayoutCountMinus.setVisibility(getProductData.isSelected() ? View.VISIBLE : View.GONE);
            textViewCount.setText(String.valueOf(getProductData.getCount()));

            final boolean finalShowOutOfStock = showOutOfStock;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = getProductData.getCount();

                    if (finalShowOutOfStock) {
                        Toast.makeText(context, "Out Of Stock", Toast.LENGTH_SHORT).show();
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

                    if (finalShowOutOfStock) {
                        Toast.makeText(context, "Out Of Stock", Toast.LENGTH_SHORT).show();
                        return;
                    }

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

        private boolean isOutOfStock(String stock, String minStock) throws NumberFormatException {
            int stockValue = Integer.parseInt(stock);
            int minStockValue = Integer.parseInt(minStock);

            if (stockValue <= 0) {
                return true;
            }

            if (stockValue < minStockValue) {
                return true;
            }

            return false;
        }
    }
}
