package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;

import java.util.List;

public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.ViewHolder> {

    private Context context;
    private List<GetCategoryData> categoryDataList;
    private CategoryDialogListener listener;

    public CategoryDialogAdapter(Context context, List<GetCategoryData> categoryDataList) {
        this.context = context;
        this.categoryDataList = categoryDataList;
    }

    public void setListener(CategoryDialogListener listener) {
        this.listener = listener;
    }

    public void setCategoryDataList(List<GetCategoryData> categoryDataList) {
        this.categoryDataList = categoryDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_category_dialog, parent, false);
        return new CategoryDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryDialogAdapter.ViewHolder holder, int position) {
        final GetCategoryData getCategoryData = categoryDataList.get(position);
        holder.textViewCategoryName.setText(getCategoryData.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectCategory(getCategoryData.getId(), getCategoryData.getTitle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }

    public interface CategoryDialogListener {
        void onSelectCategory(String categoryId, String categoryName);
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
