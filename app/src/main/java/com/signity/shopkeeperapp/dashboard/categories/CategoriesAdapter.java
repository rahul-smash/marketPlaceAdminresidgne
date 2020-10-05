package com.signity.shopkeeperapp.dashboard.categories;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.CategoryStatus.CategoryStatus;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    String switcToggle;
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

        final SubCategory getCategoryData = categoryDataList.get(position);
        String subCategoryImage = getCategoryData.getImage10080();

        String txtCategoriesName = getCategoryData.getTitle();

        holder.textViewSubCategoryName.setText(txtCategoriesName);
        try {
            if (!TextUtils.isEmpty(subCategoryImage)) {
                Picasso.with(context)
                        .load(subCategoryImage)
                        .error(R.mipmap.ic_launcher).placeholder(R.drawable.ic_launcher).into(holder.imageViewCategory);
            } else {
                holder.imageViewCategory.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.textViewCategoryName.setText(getCategoryData.getCategoryName());

        holder.textViewPriority.setText(String.format("Priority :%s", getCategoryData.getVersion()));
        holder.imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverViewPopMenu(holder);
            }
        });
        holder.switchCategory.setChecked(
                (getCategoryData.getStatus().equals("1")));
        holder.switchCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //     Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
                    switcToggle = "1";
                    setCategoryStatus(getCategoryData.getId(), String.valueOf(switcToggle));

                } else {
                    //      Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();

                    switcToggle = "0";
                    setCategoryStatus(getCategoryData.getId(), String.valueOf(switcToggle));
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClickCategory(holder.getAdapterPosition());
            }
        });
        holder.imageViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onClickCategory(holder.getAdapterPosition());
            }
        });
    }

    private void showOverViewPopMenu(MyViewHolder holder) {
        Log.i("@@ViewMore", "--click");
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_delete, null, false);
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
        popupWindowOverView.showAsDropDown(holder.imageViewMore, 0, 0);
        LinearLayout popups = layout.findViewById(R.id.popups);
        popups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Pending work Delete!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public interface CategoriesListener {
        void onClickCategory(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewCategory, imageViewMore;
        TextView textViewCategoryName, textViewSubCategoryName, textViewPriority;
        Switch switchCategory;

        public MyViewHolder(final View convertView) {
            super(convertView);
            imageViewMore = convertView.findViewById(R.id.iv_more);
            textViewPriority = convertView.findViewById(R.id.tv_priority);
            imageViewCategory = convertView.findViewById(R.id.iv_category_image);
            textViewCategoryName = convertView.findViewById(R.id.tv_category_name);
            textViewSubCategoryName = convertView.findViewById(R.id.tv_subcategory_name);
            switchCategory = convertView.findViewById(R.id.switch_catergory);
        }
    }

    public void setCategoryStatus(String subCategoryId, String status) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String, Object> param = new HashMap<>();
        param.put("cat_id", subCategoryId);
        param.put("category_status", status);

        NetworkAdaper.getNetworkServices().setCategoryStatus(param, new Callback<CategoryStatus>() {
            @Override
            public void success(CategoryStatus categoryStatus, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (categoryStatus.getSuccess()) {

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