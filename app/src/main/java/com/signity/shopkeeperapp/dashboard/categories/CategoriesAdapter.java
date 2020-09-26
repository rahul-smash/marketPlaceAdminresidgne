package com.signity.shopkeeperapp.dashboard.categories;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.RvActiveOrderAdapter;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    private List<GetCategoryData> mData;
    private LayoutInflater mInflater;
    MyViewHolder holder;

    Context context;

    // Define listener member variable
    // Define listener member variable
    private static CategoriesAdapter.OnItemClickListener listener;

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(CategoriesAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, GetCategoryData categoryData);
    }





    public CategoriesAdapter(Context context, List<GetCategoryData> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_categories_items, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        Log.i("_____CategorySize", "" + mData.size());
        return mData.size();
    }


    @Override
    public void onBindViewHolder(final CategoriesAdapter.MyViewHolder holder, final int position) {

        Log.i("@@CatregoriesData", "-------" + mData.size());
        final GetCategoryData getCategoryData = mData.get(position);
        String subCategoryImage = mData.get(position).getImage();

        String txtCategoriesName = mData.get(position).getTitle();
        String subCategoryTotal = mData.get(position).getSubCategoryTotal().toString();
        try {
            if (subCategoryImage != null && !subCategoryImage.isEmpty()) {
                Picasso.with(context).load(subCategoryImage).resize(50, 50).centerInside()
                        .error(R.mipmap.ic_launcher).placeholder(R.drawable.ic_launcher).into(holder.imageCategory);
            } else {
                holder.imageCategory.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txtCategoriesName.setText(txtCategoriesName);

        holder.txtSubcategoryTotal.setText(subCategoryTotal);
        //  holder.txtPriority.setText("Priority :" +mData.get(position).get );
        holder.imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(holder.imageSettings, position, getCategoryData);
            }
        });
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(holder.parent, position, getCategoryData);
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageCategory, imageSettings;
        TextView txtCategoriesName, txtSubcategoryTotal, txtPriority;
        ConstraintLayout parent;

        public MyViewHolder(final View convertView) {
            super(convertView);
            parent = convertView.findViewById(R.id.parent);
            imageSettings = convertView.findViewById(R.id.imageSettings);
            txtPriority = convertView.findViewById(R.id.txtPriority);
            imageCategory = (ImageView) convertView.findViewById(R.id.imageCategory);
            txtCategoriesName = convertView.findViewById(R.id.txtCategoriesName);
            txtSubcategoryTotal = convertView.findViewById(R.id.txtSubcategoryTotal);

        }

    }


}

