package com.signity.shopkeeperapp.dashboard.categories;

import android.content.Context;
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
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    private CategoriesListener listener;
    private Context context;
    private List<SubCategory> categoryDataList = new ArrayList<>();

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    public void setListener(CategoriesListener listener) {
        this.listener = listener;
    }

    public void setCategoryDataList(List<SubCategory> categoryDataList) {
        this.categoryDataList.addAll(categoryDataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.categoryDataList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_categories, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesAdapter.MyViewHolder holder, final int position) {
        holder.bind(position);
    }

    private void showOverViewPopMenu(ImageView imageViewMore, final int pos) {
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_delete, null, false);
        final PopupWindow popupWindowOverView = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowOverView.setOutsideTouchable(true);
        popupWindowOverView.setBackgroundDrawable(new ColorDrawable());
        popupWindowOverView.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindowOverView.dismiss();
                    return true;
                }
                return false;
            }

        });

        popupWindowOverView.showAsDropDown(imageViewMore, 0, 0);
        LinearLayout delete = layout.findViewById(R.id.ll_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickDeleteCategory(categoryDataList.get(pos).getId(), pos);
                }
                popupWindowOverView.dismiss();
            }
        });
    }

    public void removeItem(int position) {
        categoryDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateCategoryStatus(String id) {
        for (SubCategory subCategory : categoryDataList) {
            if (subCategory.getId().equals(id)) {
                subCategory.setStatus(subCategory.getStatus().equals("1") ? "0" : "1");
                break;
            }
        }
        notifyDataSetChanged();
    }

    public interface CategoriesListener {
        void onClickDeleteCategory(String subCategoryId, int position);

        void onClickSwitchProduct(String id, String status);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewCategory, imageViewMore;
        TextView textViewCategoryName, textViewSubCategoryName, textViewPriority, textViewStatus;
        Switch switchCategory;
        ConstraintLayout constraintLayoutParent;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageViewMore = convertView.findViewById(R.id.iv_more);
            textViewPriority = convertView.findViewById(R.id.tv_priority);
            imageViewCategory = convertView.findViewById(R.id.iv_category_image);
            textViewCategoryName = convertView.findViewById(R.id.tv_category_name);
            textViewSubCategoryName = convertView.findViewById(R.id.tv_subcategory_name);
            textViewStatus = convertView.findViewById(R.id.tv_status);
            switchCategory = convertView.findViewById(R.id.switch_catergory);
            constraintLayoutParent = convertView.findViewById(R.id.const_parent);
        }

        public void bind(final int position) {
            final SubCategory subCategory = categoryDataList.get(position);
            String subCategoryImage = subCategory.getImage10080();
            if (!TextUtils.isEmpty(subCategoryImage)) {
                Picasso.with(context)
                        .load(subCategoryImage)
                        .error(R.drawable.addimageicon)
                        .placeholder(R.drawable.addimageicon)
                        .into(imageViewCategory);
            }

            textViewCategoryName.setText(subCategory.getCategoryName());
            textViewSubCategoryName.setText(subCategory.getTitle());
            textViewStatus.setText(subCategory.getStatus().equals("1") ? "Enabled" : "Disabled");
            textViewPriority.setText(String.format("Priority :%s", subCategory.getVersion()));

            imageViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOverViewPopMenu(imageViewMore, position);
                }
            });

            switchCategory.setChecked(subCategory.getStatus().equals("1"));

            switchCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClickSwitchProduct(subCategory.getId(), (subCategory.getStatus().equals("1") ? "0" : "1"));
                    }
                }
            });

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) constraintLayoutParent.getLayoutParams();
            int margin = (int) Util.pxFromDp(context, 16);
            layoutParams.setMargins(margin, margin, margin, 0);

            if (position == categoryDataList.size() - 1) {
                layoutParams.setMargins(margin, margin, margin, 5 * margin);
            }
            constraintLayoutParent.setLayoutParams(layoutParams);
        }
    }
}