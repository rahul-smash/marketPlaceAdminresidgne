package com.signity.shopkeeperapp.dashboard.Products;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.categories.CategoriesAdapter;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.SelectedVariant;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {


    // Define listener member variable
    private static ProductsAdapter.OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, GetProductData productData);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(ProductsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    private List<GetProductData> mData;
    private LayoutInflater mInflater;
    ProductsAdapter.MyViewHolder holder;

    Context context;

    public ProductsAdapter(Context context, List<GetProductData> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_view_product, parent, false);
        holder = new ProductsAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        Log.i("_____CategorySize", "" + mData.size());
        return mData.size();
    }


    @Override
    public void onBindViewHolder(final ProductsAdapter.MyViewHolder holder, final int position) {
        Log.i("@@ProductData", "-------" + mData.size());
        final GetProductData getProductData = mData.get(position);
        String subProductImage = mData.get(position).getImage();
        Log.i("-----------------", "" + subProductImage);
        try {
            if (subProductImage != null && !subProductImage.isEmpty()) {

                Picasso.with(context).load(subProductImage).centerInside()
                        .error(R.mipmap.ic_launcher).placeholder(R.drawable.ic_launcher).into(holder.imageCategory);
            } else {
                holder.imageCategory.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String categoryIds = mData.get(position).getCategoryIds().toString();

        holder.txtProductName.setText(mData.get(position).getTitle());
    /*    List<Variant> varientData = mData.get(position).getVariants();
        Log.i("@@@____", "" + varientData.toString());*/
       /* Variant variantData = varientData.get(position);


        holder.txtVarient.setText(variantData.getMrpPrice().toString());
        holder.txtWeight.setText(variantData.getWeight().toString());
        holder.txtDiscountPrice.setText(variantData.getDiscount().toString());*/
        holder.imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(holder.imageSettings, position, getProductData);
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageCategory, imageSettings;
        TextView txtProductName, txtProductType, txtPriority, txtVarient, txtWeight, txtLblQuantity, txtDiscountPrice;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageSettings = convertView.findViewById(R.id.imageSettings);
            txtProductName = convertView.findViewById(R.id.txtProductName);
            txtProductType = convertView.findViewById(R.id.txtProductType);
            txtPriority = convertView.findViewById(R.id.txtPriority);
            txtVarient = convertView.findViewById(R.id.txtVarient);
            txtWeight = convertView.findViewById(R.id.txtWeight);
            txtLblQuantity = convertView.findViewById(R.id.txtLblQuantity);
            txtDiscountPrice = convertView.findViewById(R.id.txtDiscountPrice);

        }

    }


}

