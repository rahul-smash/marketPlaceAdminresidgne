package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.DynamicField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DynamicFieldAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<DynamicField> dynamicFieldList = new ArrayList<>();
    private Map<String, String> fieldMap = new HashMap<>();

    public DynamicFieldAdapter(Context context) {
        this.context = context;
    }

    public void setDynamicFieldList(List<DynamicField> dynamicFieldList) {
        this.dynamicFieldList = dynamicFieldList;
        notifyDataSetChanged();
    }

    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
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

            final DynamicField dynamicField = dynamicFieldList.get(positon);

            String astr = dynamicField.getValidation().equalsIgnoreCase("true") ? "*" : "";
            textInputLayout.setHint(dynamicField.getLabel().concat(astr));
            textInputLayout.setHintEnabled(true);
            textInputEditText.setTag(dynamicField.getVariantFieldName());

            textInputEditText.setText(fieldMap.get(dynamicField.getVariantFieldName()));

            textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        if (dynamicField.getValidation().equalsIgnoreCase("true")) {
                            if (TextUtils.isEmpty(textInputEditText.getText().toString().trim())) {
//                                textInputEditText.setError("Empty");
                                return;
                            }
                        }
                    }

                    fieldMap.put(textInputEditText.getTag().toString(), textInputEditText.getText().toString().trim());
                }
            });

            textInputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s)) {
                        return;
                    }
                    fieldMap.put(textInputEditText.getTag().toString(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
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

            final DynamicField dynamicField = dynamicFieldList.get(positon);

            String astr = dynamicField.getValidation().equalsIgnoreCase("true") ? "*" : "";
            textInputLayout.setHint(dynamicField.getLabel().concat(astr));
            textInputLayout.setHintEnabled(true);
            textInputEditText.setTag(dynamicField.getVariantFieldName());

            if (dynamicField.getVariantFieldName().equalsIgnoreCase("discount") && TextUtils.isEmpty(fieldMap.get("discount"))) {
                textInputEditText.setText("0");
                fieldMap.put(textInputEditText.getTag().toString(), "0");
            }

            textInputEditText.setText(fieldMap.get(dynamicField.getVariantFieldName()));

            textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (hasFocus) {
                        if (textInputEditText.getTag().toString().equalsIgnoreCase("price")) {

                            String mrp = fieldMap.get("mrp_price");
                            String discount = fieldMap.get("discount");

                            if (TextUtils.isEmpty(mrp) || TextUtils.isEmpty(discount)) {
                                return;
                            }

                            double doubleMrp = Double.parseDouble(mrp);
                            double doubleDiscount = Double.parseDouble(discount);

                            double price = doubleMrp - (doubleMrp * doubleDiscount / 100);
                            textInputEditText.setText(String.format(Locale.getDefault(), "%.2f", price));
                        }
                    }

                    if (!hasFocus) {
                        String value = textInputEditText.getText().toString().trim();
                        if (TextUtils.isEmpty(value)) {
                            return;
                        }

                        try {
                            double dValue = Double.parseDouble(value);
                            if (dValue < 0) {
                                Toast.makeText(context, "Invalid " + dynamicField.getLabel(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Invalid " + dynamicField.getLabel(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    fieldMap.put(textInputEditText.getTag().toString(), textInputEditText.getText().toString().trim());
                }
            });

            textInputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s)) {
                        return;
                    }

                    if (textInputEditText.getTag().toString().equalsIgnoreCase("custom_field2")) {

                        String stockValue = fieldMap.get("custom_field1");

                        if (TextUtils.isEmpty(stockValue)) {
                            Toast.makeText(context, "Please add Stock", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int stock = Integer.parseInt(stockValue);
                        int minStock = Integer.parseInt(s.toString());

                        if (minStock >= stock) {
                            Toast.makeText(context, "Min stock value required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    fieldMap.put(textInputEditText.getTag().toString(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
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

            final List<String> spinnerKey = new ArrayList<>();
            final List<String> spinnerValue = new ArrayList<>();

            DynamicField dynamicField = dynamicFieldList.get(positon);

            final Map<String, String> valueMap = dynamicField.getValue();

            if (valueMap == null || valueMap.isEmpty()) {
                return;
            }

            spinner.setTag(dynamicField.getVariantFieldName());
            String astr = dynamicField.getValidation().equalsIgnoreCase("true") ? "*" : "";
            textViewHeading.setText(dynamicField.getLabel().concat(astr));

            for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                spinnerKey.add(entry.getKey());
                spinnerValue.add(entry.getValue());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, spinnerValue);
            spinner.setAdapter(arrayAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fieldMap.put(spinner.getTag().toString(), spinnerKey.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

}
