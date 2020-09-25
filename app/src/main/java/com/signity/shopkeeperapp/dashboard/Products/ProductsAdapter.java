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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {


    // Define listener member variable
    private static ProductsAdapter.OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(ProductsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    private List<GetCategoryData> mData;
    private LayoutInflater mInflater;
    ProductsAdapter.MyViewHolder holder;

    Context context;

    public ProductsAdapter(Context context, List<GetCategoryData> data) {
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
    public void onBindViewHolder(ProductsAdapter.MyViewHolder holder, int position) {
        Log.i("@@CatregoriesData", "-------" + mData.size());

        //  holder.txtPriority.setText("Priority :" +mData.get(position).get );
        holder.imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageCategory, imageSettings;
        TextView txtCategoriesName, txtSubcategoryTotal, txtPriority;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageSettings = convertView.findViewById(R.id.imageSettings);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(convertView, getLayoutPosition());
                }
            });
        }

    }


}

