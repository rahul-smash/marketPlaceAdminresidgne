package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.DynamicField;

import java.util.ArrayList;
import java.util.List;

public class DynamicFieldAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DynamicField> dynamicFieldList = new ArrayList<>();

    public DynamicFieldAdapter(Context context) {
        this.context = context;
    }

    public void setDynamicFieldList(List<DynamicField> dynamicFieldList) {
        this.dynamicFieldList = dynamicFieldList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (FieldType.values()[viewType]) {
            case NUMBER:
                viewHolder = new ViewHolderNumber(LayoutInflater.from(context).inflate(R.layout.itemview_dynamic_field_number, parent, false));
                break;
            case SELECT:
                viewHolder = new ViewHolderSelect(LayoutInflater.from(context).inflate(R.layout.itemview_dynamic_field_dropdown, parent, false));
                break;
            case TEXT:
            default:
                viewHolder = new ViewHolderText(LayoutInflater.from(context).inflate(R.layout.itemview_dynamic_field_text, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderText) {
            ((ViewHolderText) holder).bind(position);
        }

        if (holder instanceof ViewHolderNumber) {
            ((ViewHolderNumber) holder).bind(position);
        }

        if (holder instanceof ViewHolderSelect) {
            ((ViewHolderSelect) holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FieldType enumField = FieldType.TEXT;
        try {
            String fieldType = dynamicFieldList.get(position).getType();
            enumField = FieldType.valueOf(fieldType.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return enumField.ordinal();
    }

    @Override
    public int getItemCount() {
        return dynamicFieldList.size();
    }

    public enum FieldType {
        TEXT, NUMBER, SELECT
    }

    public class ViewHolderText extends RecyclerView.ViewHolder {

        TextInputLayout textInputLayout;
        TextInputEditText textInputEditText;

        public ViewHolderText(@NonNull View itemView) {
            super(itemView);
            textInputLayout = itemView.findViewById(R.id.til_dynamic_field);
            textInputEditText = itemView.findViewById(R.id.edt_dynamic_field_text);
        }

        public void bind(int positon) {

            DynamicField dynamicField = dynamicFieldList.get(positon);

            textInputLayout.setHint(dynamicField.getLabel());
            textInputLayout.setHintEnabled(true);
            textInputEditText.setTag(dynamicField.getVariantFieldName());
        }
    }

    public class ViewHolderNumber extends RecyclerView.ViewHolder {

        TextInputLayout textInputLayout;
        TextInputEditText textInputEditText;

        public ViewHolderNumber(@NonNull View itemView) {
            super(itemView);
            textInputLayout = itemView.findViewById(R.id.til_dynamic_field);
            textInputEditText = itemView.findViewById(R.id.edt_dynamic_field_number);
        }

        public void bind(int positon) {

            DynamicField dynamicField = dynamicFieldList.get(positon);
            textInputLayout.setHint(dynamicField.getLabel());
            textInputLayout.setHintEnabled(true);

        }
    }

    public class ViewHolderSelect extends RecyclerView.ViewHolder {

        TextView textViewHeading;
        Spinner spinner;

        public ViewHolderSelect(@NonNull View itemView) {
            super(itemView);
            textViewHeading = itemView.findViewById(R.id.tv_dynamic_field_heading);
            spinner = itemView.findViewById(R.id.spinner_dynamic_field);
        }

        public void bind(int positon) {
        }
    }

}
