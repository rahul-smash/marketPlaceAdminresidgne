package com.signity.shopkeeperapp.products;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Product.DynamicField;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VariantFragment extends Fragment {

    public static final String TAG = "VariantFragment";
    public static final String DYNAMIC_LIST = "DYNAMIC_LIST";
    private Map<String, String> variantMap = new HashMap<>();
    private List<DynamicField> dynamicFieldList = new ArrayList<>();
    private Map<String, Integer> dynamicFieldIdMap = new HashMap<>();

    private LinearLayout linearLayoutLeft, linearLayoutRight;

    public static VariantFragment getInstance(Bundle bundle) {
        VariantFragment fragment = new VariantFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public Map<String, String> getVariantMap() {
        return variantMap;
    }

    public void setVariantMap(Map<String, String> variantMap) {
        this.variantMap = variantMap;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_variant_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra();
    }

    private void getExtra() {
        if (getArguments() != null) {
            dynamicFieldList = getArguments().getParcelableArrayList(DYNAMIC_LIST);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpDynamicViews();
    }

    private void initView(View view) {
        linearLayoutLeft = view.findViewById(R.id.ll_left);
        linearLayoutRight = view.findViewById(R.id.ll_right);
    }

    private void setUpDynamicViews() {

        for (int i = 0; i < dynamicFieldList.size(); i++) {
            View dynamicView;
            final DynamicField dynamicField = dynamicFieldList.get(i);

            switch (dynamicField.getType()) {
                case "number":
                    dynamicView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_dynamic_field_number, null, false);
                    final TextInputLayout textInputLayout = dynamicView.findViewById(R.id.til_dynamic_field);
                    final TextInputEditText textInputEditText = dynamicView.findViewById(R.id.edt_dynamic_field_number);

                    String astr = dynamicField.getValidation().equalsIgnoreCase("true") ? "*" : "";
                    textInputLayout.setHint(dynamicField.getLabel().concat(astr));
                    textInputLayout.setHintEnabled(true);

                    textInputEditText.setTag(dynamicField.getVariantFieldName());

                    if (dynamicField.getVariantFieldName().equalsIgnoreCase("discount") && TextUtils.isEmpty(variantMap.get("discount"))) {
                        textInputEditText.setText("0");
                        variantMap.put(textInputEditText.getTag().toString(), "0");
                    }

                    textInputEditText.setText(variantMap.get(dynamicField.getVariantFieldName()));

                    textInputEditText.addTextChangedListener(new CustomTextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            if (TextUtils.isEmpty(s)) {
                                return;
                            }

                            if (dynamicField.getVariantFieldName().equalsIgnoreCase("mrp_price")) {

                                String mrp = s.toString();
                                String discount = variantMap.get("discount");

                                if (TextUtils.isEmpty(mrp) || TextUtils.isEmpty(discount)) {
                                    updatePriceField(-1);
                                    return;
                                }
                                calculatePrice(mrp, discount);

                            } else if (dynamicField.getVariantFieldName().equalsIgnoreCase("discount")) {
                                String discount = s.toString();
                                String mrp = variantMap.get("mrp_price");

                                if (TextUtils.isEmpty(mrp) || TextUtils.isEmpty(discount)) {
                                    updatePriceField(-1);
                                    return;
                                }
                                calculatePrice(mrp, discount);
                            } else if (dynamicField.getVariantFieldName().equalsIgnoreCase("custom_field2")) {

                                String stockValue = variantMap.get("custom_field1");

                                if (TextUtils.isEmpty(stockValue)) {
                                    Toast.makeText(getContext(), "Please add Stock", Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    int stock = Integer.parseInt(stockValue);
                                    int minStock = Integer.parseInt(s.toString());

                                    if (minStock >= stock) {
                                        Toast.makeText(getContext(), "Min stock value required", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }

                            variantMap.put(textInputEditText.getTag().toString(), textInputEditText.getText().toString().trim());
                        }
                    });

                    break;
                case "select":
                    dynamicView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_dynamic_field_dropdown, null, false);
                    final TextView textViewHeading = dynamicView.findViewById(R.id.tv_dynamic_field_heading);
                    final Spinner spinner = dynamicView.findViewById(R.id.spinner_dynamic_field);

                    final List<String> spinnerKey = new ArrayList<>();
                    final List<String> spinnerValue = new ArrayList<>();

                    final Map<String, String> valueMap = dynamicField.getValue();

                    if (valueMap == null || valueMap.isEmpty()) {
                        return;
                    }

                    spinner.setTag(dynamicField.getVariantFieldName());
                    String astrDropDown = dynamicField.getValidation().equalsIgnoreCase("true") ? "*" : "";
                    textViewHeading.setText(dynamicField.getLabel().concat(astrDropDown));

                    for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                        spinnerKey.add(entry.getKey());
                        spinnerValue.add(entry.getValue());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerValue);
                    spinner.setAdapter(arrayAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            variantMap.put(spinner.getTag().toString(), spinnerKey.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            variantMap.put(spinner.getTag().toString(), spinnerKey.get(0));
                        }
                    });
                    break;
                case "text":
                default:
                    dynamicView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_dynamic_field_text, null, false);

                    final TextInputLayout textInputLayout1 = dynamicView.findViewById(R.id.til_dynamic_field);
                    final TextInputEditText textInputEditText1 = dynamicView.findViewById(R.id.edt_dynamic_field_text);

                    String astr1 = dynamicField.getValidation().equalsIgnoreCase("true") ? "*" : "";
                    textInputLayout1.setHint(dynamicField.getLabel().concat(astr1));
                    textInputLayout1.setHintEnabled(true);

                    textInputEditText1.setTag(dynamicField.getVariantFieldName());
                    textInputEditText1.setText(variantMap.get(dynamicField.getVariantFieldName()));
                    textInputEditText1.addTextChangedListener(new CustomTextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            if (TextUtils.isEmpty(s)) {
                                return;
                            }

                            variantMap.put(textInputEditText1.getTag().toString(), textInputEditText1.getText().toString().trim());
                        }
                    });
            }

            dynamicFieldIdMap.put(dynamicField.getVariantFieldName(), i * 23);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i % 2 == 0) {
                layoutParams.setMargins(0, 0, (int) Util.dpFromPx(getContext(), 8), 0);
                dynamicView.setLayoutParams(layoutParams);
                dynamicView.setId(i * 23);
                linearLayoutLeft.addView(dynamicView);
            } else {
                layoutParams.setMargins((int) Util.dpFromPx(getContext(), 8), 0, 0, 0);
                dynamicView.setLayoutParams(layoutParams);
                dynamicView.setId(i * 23);
                linearLayoutRight.addView(dynamicView);
            }
        }

    }

    private void calculatePrice(String mrp, String discount) {
        double doubleMrp = Double.parseDouble(mrp);
        double doubleDiscount = Double.parseDouble(discount);

        double price = doubleMrp - (doubleMrp * doubleDiscount / 100);
        updatePriceField(price);
    }

    private void updatePriceField(double price) {
        Integer fieldId = dynamicFieldIdMap.get("price");
        if (fieldId != null) {
            ConstraintLayout constraintLayout = linearLayoutLeft.findViewById(fieldId);
            if (constraintLayout == null) {
                constraintLayout = linearLayoutRight.findViewById(fieldId);
            }

            TextInputEditText editText = (TextInputEditText) constraintLayout.findViewById(R.id.edt_dynamic_field_number);

            if (price >= 0) {
                editText.setText(String.format(Locale.getDefault(), "%.2f", price));
            } else {
                editText.setText("0");
            }
            variantMap.put("price", editText.getText().toString().trim());
        }
    }

    static abstract class CustomTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
