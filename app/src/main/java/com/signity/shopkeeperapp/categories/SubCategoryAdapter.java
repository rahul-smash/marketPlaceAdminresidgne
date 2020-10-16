package com.signity.shopkeeperapp.categories;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.category.SubCategoryModel;
import com.squareup.picasso.Picasso;

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

    public List<SubCategoryModel> getSubCategoryModels() {
        return subCategoryModels;
    }

    public interface SubCategoryAdapterListner {
        void onAddImage(int position);
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText textInputEditTextSubCategoryName;
        LinearLayout linearLayoutAddImage;
        ImageView imageViewSubCategoryImage;
        ImageView imageViewDeleteImage;

        public SubCategoryViewHolder(final View view) {
            super(view);
            linearLayoutAddImage = view.findViewById(R.id.ll_add_sub_category_image);
            imageViewSubCategoryImage = view.findViewById(R.id.iv_sub_category);
            textInputEditTextSubCategoryName = view.findViewById(R.id.edt_sub_category_name);
            imageViewDeleteImage = view.findViewById(R.id.iv_cancel);
        }

        public void bind(final int position) {

            final SubCategoryModel subCategory = subCategoryModels.get(position);

            try {
                String categoryImageUrl = subCategory.getSubCategoryImageUrl();
                if (!TextUtils.isEmpty(categoryImageUrl)) {
                    imageViewDeleteImage.setVisibility(View.VISIBLE);
                    imageViewSubCategoryImage.setVisibility(View.VISIBLE);
                    linearLayoutAddImage.setVisibility(View.INVISIBLE);
                    Picasso.with(context)
                            .load(categoryImageUrl)
                            .placeholder(ResourcesCompat.getDrawable(context.getResources(), R.drawable.addimageicon, null))
                            .into(imageViewSubCategoryImage);
                } else {
                    imageViewDeleteImage.setVisibility(View.GONE);
                    imageViewSubCategoryImage.setVisibility(View.GONE);
                    linearLayoutAddImage.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            textInputEditTextSubCategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String subCatName = textInputEditTextSubCategoryName.getText().toString().trim();
                        subCategory.setSubCategoryName(subCatName);
                    }
                }
            });

            textInputEditTextSubCategoryName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(s)) {
                        subCategory.setSubCategoryName(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            textInputEditTextSubCategoryName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        String subCatName = textInputEditTextSubCategoryName.getText().toString().trim();
                        subCategory.setSubCategoryName(subCatName);
                    }
                    return false;
                }
            });

            imageViewDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subCategory.setSubCategoryImageUrl(null);
                    subCategory.setSubCategoryImage(null);
                    notifyItemChanged(getAdapterPosition(), subCategory);
                }
            });

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
