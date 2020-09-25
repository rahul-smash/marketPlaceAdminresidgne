package com.signity.shopkeeperapp.dashboard.categories;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {


    // Define listener member variable
    private static OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private List<GetCategoryData> mData;
    private LayoutInflater mInflater;
    MyViewHolder holder;

    Context context;

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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("@@CatregoriesData", "-------" + mData.size());
        String subCategoryImage = mData.get(position).getImage();
        String txtCategoriesName = mData.get(position).getTitle();
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

    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageCategory;
        TextView txtCategoriesName;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageCategory = (ImageView) convertView.findViewById(R.id.imageCategory);
            txtCategoriesName = convertView.findViewById(R.id.txtCategoriesName);
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

