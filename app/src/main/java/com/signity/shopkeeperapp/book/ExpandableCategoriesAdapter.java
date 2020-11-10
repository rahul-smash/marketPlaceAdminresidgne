package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public int selectedId = -1;
    private Context context;
    private List<SubCategory> categoryDataList = new ArrayList<>();
    private ExpandableCategoriesListener listener;
    private Map<Integer, List<GetProductData>> productMap = new HashMap<>();

    public ExpandableCategoriesAdapter(Context context) {
        this.context = context;
    }

    public void setListener(ExpandableCategoriesListener listener) {
        this.listener = listener;
    }

    public void setCategoryDataList(List<SubCategory> categoryDataList) {
        this.categoryDataList = categoryDataList;
        notifyDataSetChanged();
    }

    public void setProductDataList(List<GetProductData> productDataList) {
        this.productMap.put(selectedId, productDataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_expandable_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        ((ViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }

    public interface ExpandableCategoriesListener {
        void onClickCategory(String subCategoryId, String categoryId);

        void onAddProduct(GetProductData getProductData);

        void onRemoveProduct(GetProductData getProductData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OrderCartListener {

        TextView textView;
        ImageView imageViewArrow;
        RecyclerView recyclerView;
        ConstraintLayout constraintLayoutCategories;
        ContentLoadingProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_category_name);
            imageViewArrow = itemView.findViewById(R.id.iv_arrow);
            recyclerView = itemView.findViewById(R.id.rv_itemview_categories);
            constraintLayoutCategories = itemView.findViewById(R.id.const_categories);
            progressBar = itemView.findViewById(R.id.expand_progress);
        }

        public void onBind(final int position) {
            final SubCategory categoryData = categoryDataList.get(position);
            textView.setText(categoryData.getTitle());

            final BestSellerAdapter bestSellerAdapter = new BestSellerAdapter(context, this);
            recyclerView.setAdapter(bestSellerAdapter);
            if (productMap.get(position) != null) {
                List<GetProductData> productData = productMap.get(position);
                bestSellerAdapter.addProductData(productData);
            }

            progressBar.hide();
            imageViewArrow.setImageDrawable(context.getResources().getDrawable(categoryData.isOpen() ? R.drawable.collapsearrow2 : R.drawable.collapsearrow1));

            recyclerView.setVisibility(categoryData.isOpen() ? View.VISIBLE : View.GONE);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedId = getAdapterPosition();
                    categoryData.setOpen(!categoryData.isOpen());
                    if (productMap.get(position) == null) {
                        if (listener != null) {
                            progressBar.show();
                            listener.onClickCategory(categoryData.getId(), categoryData.getCategoryId());
                        }
                    } else {
                        notifyDataSetChanged();
                    }
                }
            });

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) constraintLayoutCategories.getLayoutParams();
            int margin = (int) Util.pxFromDp(context, 8);
            layoutParams.setMargins(margin, margin, margin, 0);

            if (position == categoryDataList.size() - 1) {
                layoutParams.setMargins(margin, margin, margin, 8 * margin);
            }
            constraintLayoutCategories.setLayoutParams(layoutParams);
        }

        @Override
        public void onAddProduct(GetProductData getProductData) {
            if (listener != null) {
                listener.onAddProduct(getProductData);
            }
        }

        @Override
        public void onRemoveProduct(GetProductData getProductData) {
            if (listener != null) {
                listener.onRemoveProduct(getProductData);
            }
        }
    }
}
