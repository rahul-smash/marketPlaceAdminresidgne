package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Categories.SubCategory;

import java.util.List;

public class SubCategoryDialogAdapter extends RecyclerView.Adapter<SubCategoryDialogAdapter.ViewHolder> {

    private Context context;
    private List<SubCategory> subCategoryList;
    private SubCategoryDialogListener listener;

    public SubCategoryDialogAdapter(Context context, List<SubCategory> subCategoryList) {
        this.context = context;
        this.subCategoryList = subCategoryList;
    }

    public void setSubCategoryList(List<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public void setListener(SubCategoryDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoryDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_category_dialog, parent, false);
        return new SubCategoryDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryDialogAdapter.ViewHolder holder, int position) {
        final SubCategory subCategory = subCategoryList.get(position);
        holder.textViewCategoryName.setText(subCategory.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectSubCategory(subCategory.getId(), subCategory.getTitle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public interface SubCategoryDialogListener {
        void onSelectSubCategory(String subCategoryId, String subCategoryName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategoryName;
        View viewSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.tv_category_name);
            viewSelected = itemView.findViewById(R.id.view_selected);
        }
    }
}
