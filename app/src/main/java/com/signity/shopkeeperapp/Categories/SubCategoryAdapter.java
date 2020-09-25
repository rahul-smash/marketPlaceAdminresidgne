package com.signity.shopkeeperapp.Categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.category.SubCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SubCategoryAdapter";
    private final Context context;
    private List<SubCategoryModel> subCategoryModels = new ArrayList<>();
    private SubCategoryAdapterListner listener;

    public SubCategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.itemview_add_sub_category_button, parent, false);
            return new AddMoreCategoryViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_add_sub_category, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof SubCategoryViewHolder) {
            ((SubCategoryViewHolder) holder).bind(position);
        } else {
            ((AddMoreCategoryViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subCategoryModels.add(new SubCategoryModel());
                    notifyItemInserted(getItemCount() - 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return subCategoryModels.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == subCategoryModels.size()) {
            return 1;
        }
        return 0;
    }

    public void setListener(SubCategoryAdapterListner listener) {
        this.listener = listener;
    }

    public interface SubCategoryAdapterListner {
        void onAddImage(int position);
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText textInputEditTextSubCategoryName;
        LinearLayout linearLayoutAddImage;

        public SubCategoryViewHolder(final View view) {
            super(view);
            linearLayoutAddImage = view.findViewById(R.id.ll_add_sub_category_image);
            textInputEditTextSubCategoryName = view.findViewById(R.id.edt_sub_category_name);
        }

        public void bind(int position) {


            linearLayoutAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAddImage(getAdapterPosition());
                    }
                }
            });
        }

    }

    class AddMoreCategoryViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;

        public AddMoreCategoryViewHolder(final View view) {
            super(view);
            cardView = view.findViewById(R.id.cv_add_sub_category);
        }

    }

}
