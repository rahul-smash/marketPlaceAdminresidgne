package com.signity.shopkeeperapp.dashboard.Products;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GetProductData> mData = new ArrayList<>();
    private ProductAdapterListener listener;
    private int totalOrders;
    private boolean showLoading = true;

    public ProductsAdapter(Context context) {
        this.context = context;
    }

    public void setListener(ProductAdapterListener listener) {
        this.listener = listener;
    }

    public List<GetProductData> getmData() {
        return mData;
    }

    public void setmData(List<GetProductData> mData, int totalOrders, boolean showLoading) {
        this.showLoading = showLoading;
        this.totalOrders = totalOrders;
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.mData.clear();
        notifyDataSetChanged();
    }

    public void addData(List<GetProductData> mData, int totalOrders) {
        this.showLoading = true;
        this.totalOrders = totalOrders;
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.itemview_products, parent, false);
            return new MyViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_loading, parent, false);
        return new ViewHolderLoading(view);
    }

    @Override
    public int getItemCount() {

        if (showLoading) {
            if (mData.isEmpty()) {
                return 1;
            }

            if (mData.size() < totalOrders) {
                return mData.size() + 1;
            }
        }

        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mData.size()) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).bind(position);
        }
    }

    private void popmenu(ImageView view, final String id, final int position) {
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_product_items, null, false);
        final PopupWindow popupWindowOverView = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowOverView.setOutsideTouchable(true);
        popupWindowOverView.setBackgroundDrawable(new ColorDrawable());
        popupWindowOverView.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindowOverView.dismiss();
                    return true;
                }
                return false;
            }

        });

        float den = context.getResources().getDisplayMetrics().density;
        int offsetY = (int) (den * 8);
        popupWindowOverView.showAsDropDown(view, 0, -offsetY);

        LinearLayout share = layout.findViewById(R.id.ll_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickShareProduct(position);
                }
                popupWindowOverView.dismiss();
            }
        });

        LinearLayout delete = layout.findViewById(R.id.ll_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickDeleteProduct(id, position);
                }
                popupWindowOverView.dismiss();
            }
        });
    }

    public void updateProductStatus(String id) {
        for (GetProductData productData : mData) {
            if (productData.getId().equals(id)) {
                productData.setStatus(productData.getStatus().equals("1") ? "0" : "1");
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mData.remove(position);
        totalOrders--;
        notifyItemRemoved(position);
    }

    public interface ProductAdapterListener {
        void onClickDeleteProduct(String id, int position);

        void onClickShareProduct(int position);

        void onClickSwitchProduct(String id, String status);

        void onClickProduct(String productId);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Switch switchProduct;
        ConstraintLayout constraintLayoutParent;
        ImageView imageViewProduct, imageSettings;
        TextView txtProductName, textViewStatus, txtSubCategoryName, txtPrice, txtVarient, txtWeight, txtLblQuantity, txtDiscountPrice;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageViewProduct = convertView.findViewById(R.id.iv_product_image);
            imageSettings = convertView.findViewById(R.id.iv_more);
            txtProductName = convertView.findViewById(R.id.tv_product_name);
            textViewStatus = convertView.findViewById(R.id.tv_status);
            txtSubCategoryName = convertView.findViewById(R.id.tv_subcategory_name);
            txtVarient = convertView.findViewById(R.id.tv_product_variant);
            txtWeight = convertView.findViewById(R.id.tv_product_quantity);
            txtLblQuantity = convertView.findViewById(R.id.txtLblQuantity);
            txtDiscountPrice = convertView.findViewById(R.id.tv_product_price_final);
            txtPrice = convertView.findViewById(R.id.tv_product_price);
            switchProduct = convertView.findViewById(R.id.switch_product);
            constraintLayoutParent = convertView.findViewById(R.id.const_parent);
        }

        public void bind(int position) {
            final GetProductData productData = mData.get(position);

            String productImage = productData.getImage10080();
            if (!TextUtils.isEmpty(productImage)) {
                Picasso.with(context)
                        .load(productImage)
                        .error(R.drawable.addimageicon)
                        .placeholder(R.drawable.addimageicon)
                        .into(imageViewProduct);
            }

            txtProductName.setText(productData.getTitle());
            txtSubCategoryName.setText(productData.getCategoryName());

            List<Variant> varientDataList = productData.getVariants();
            Variant variantData = varientDataList.get(0);

            switchProduct.setChecked(productData.getStatus().equals("1"));
            switchProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickSwitchProduct(productData.getId(), (productData.getStatus().equals("1") ? "0" : "1"));
                    }
                }
            });

            double discountPrice = Double.parseDouble(TextUtils.isEmpty(variantData.getPrice()) ? "0" : variantData.getPrice());
            double mrpPrice = Double.parseDouble(TextUtils.isEmpty(variantData.getMrpPrice()) ? "0" : variantData.getMrpPrice());

            textViewStatus.setText(productData.getStatus().equals("1") ? "Enabled" : "Disabled");
            txtVarient.setText(String.format(Locale.getDefault(), "Variants - %d", varientDataList.size()));
            txtWeight.setText(String.format("Qty - %s%s", variantData.getWeight(), variantData.getUnitType()));
            txtDiscountPrice.setText(Util.getPriceWithCurrency(discountPrice, AppPreference.getInstance().getCurrency()));
            txtPrice.setText(Util.getPriceWithCurrency(mrpPrice, AppPreference.getInstance().getCurrency()));
            txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            imageSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popmenu(imageSettings, productData.getId(), getAdapterPosition());
                }
            });

            txtPrice.setVisibility(discountPrice == mrpPrice ? View.GONE : View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickProduct(productData.getId());
                    }
                }
            });

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) constraintLayoutParent.getLayoutParams();
            int margin = (int) Util.pxFromDp(context, 16);
            layoutParams.setMargins(margin, margin, margin, 0);

            if (position == getItemCount() - 1) {
                layoutParams.setMargins(margin, margin, margin, 5 * margin);
            }
            constraintLayoutParent.setLayoutParams(layoutParams);
        }
    }

    class ViewHolderLoading extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayoutParent;

        public ViewHolderLoading(final View convertView) {
            super(convertView);

            constraintLayoutParent = convertView.findViewById(R.id.const_parent);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) constraintLayoutParent.getLayoutParams();
            int margin = (int) Util.pxFromDp(context, 16);
            layoutParams.setMargins(margin, margin, margin, 5 * margin);
            constraintLayoutParent.setLayoutParams(layoutParams);
        }
    }
}

