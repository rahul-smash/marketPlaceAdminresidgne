package com.signity.shopkeeperapp.dashboard.Products;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.CategoryStatus.CategoryStatus;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.model.productStatus.ProductStatus;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    String switcToggle;

    // Define listener member variable
    private ProductsAdapter.OnItemClickListener listener;
    private Context context;
    private List<GetProductData> mData = new ArrayList<>();

    public ProductsAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(ProductsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setmData(List<GetProductData> mData) {
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_products, parent, false);
        return new ProductsAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.MyViewHolder holder, final int position) {
        Log.i("@@ProductData", "-------" + mData.size());
        final GetProductData getProductData = mData.get(position);
        String subProductImage = mData.get(position).getImage10080();
        try {
            if (subProductImage != null && !subProductImage.isEmpty()) {

                Picasso.with(context).load(subProductImage)
                        .error(R.mipmap.ic_launcher).placeholder(R.drawable.ic_launcher).into(holder.imageCategory);
            } else {
                holder.imageCategory.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String categoryIds = mData.get(position).getCategoryIds();

        holder.txtProductName.setText(mData.get(position).getTitle());
        List<Variant> varientDataList = mData.get(position).getVariants();
        Log.i("@@@____", "" + varientDataList.toString());
        Variant variantData = varientDataList.get(0);

        holder.switchProduct.setChecked(getProductData.getStatus().equals("1"));

        holder.switchProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //     Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
                    switcToggle = "1";
                    setProductStatus(mData.get(position).getId(), String.valueOf(switcToggle));

                } else {
                    //      Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();

                    switcToggle = "0";
                    setProductStatus(mData.get(position).getId(), String.valueOf(switcToggle));

                }
            }
        });

        holder.txtVarient.setText(String.format(Locale.getDefault(), "Variants - %d", varientDataList.size()));
        holder.txtWeight.setText(String.format("Qty - %s%s", variantData.getWeight(), variantData.getUnitType()));
        holder.txtDiscountPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(variantData.getPrice()), AppPreference.getInstance().getCurrency()));
        holder.txtPrice.setText(Util.getPriceWithCurrency(Double.parseDouble(variantData.getMrpPrice()), AppPreference.getInstance().getCurrency()));
        holder.txtPrice.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
//                popmenu(v);
            }
        });
    }

    private void popmenu(View v) {
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_product_items, null, false);
        final PopupWindow popupWindowOverView = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowOverView.setOutsideTouchable(true);
        popupWindowOverView.setBackgroundDrawable(new ColorDrawable());
        popupWindowOverView.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) // here I want to close the pw when clicking outside it but at all this is just an example of how it works and you can implement the onTouch() or the onKey() you want
                {
                    popupWindowOverView.dismiss();
                    return true;
                }
                return false;
            }

        });

        float den = context.getResources().getDisplayMetrics().density;
        int offsetY = (int) (den * 2);
        popupWindowOverView.showAsDropDown(v, 0, 0);
        LinearLayout popups = layout.findViewById(R.id.popups);
        popups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Pending work Delete!", Toast.LENGTH_SHORT).show();

            }
        });
    }


    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, GetProductData productData);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Switch switchProduct;
        ImageView imageCategory, imageSettings;
        TextView txtProductName, txtProductType, txtPrice, txtVarient, txtWeight, txtLblQuantity, txtDiscountPrice;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageCategory = convertView.findViewById(R.id.iv_product_image);
            imageSettings = convertView.findViewById(R.id.iv_more);
            txtProductName = convertView.findViewById(R.id.tv_product_name);
            txtProductType = convertView.findViewById(R.id.tv_subcategory_name);
            txtVarient = convertView.findViewById(R.id.tv_product_variant);
            txtWeight = convertView.findViewById(R.id.tv_product_quantity);
            txtLblQuantity = convertView.findViewById(R.id.txtLblQuantity);
            txtDiscountPrice = convertView.findViewById(R.id.tv_product_price_final);
            txtPrice = convertView.findViewById(R.id.tv_product_price);
            switchProduct = convertView.findViewById(R.id.switch_product);
        }

    }
    //SetProductStatusAPI
    public void setProductStatus(String subProductId, String status) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String, Object> param = new HashMap<>();
        param.put("product_id", subProductId);
        param.put("product_status", status);

        NetworkAdaper.getNetworkServices().setProductStatus(param, new Callback<ProductStatus>() {
            @Override
            public void success(ProductStatus productStatus, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (productStatus.getSuccess()) {

                } else {
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

}

