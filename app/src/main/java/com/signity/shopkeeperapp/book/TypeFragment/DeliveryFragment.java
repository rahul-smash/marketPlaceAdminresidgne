package com.signity.shopkeeperapp.book.TypeFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.book.BookOrderCheckoutActivity;
import com.signity.shopkeeperapp.model.customers.detail.CustomerAddressResponse;
import com.signity.shopkeeperapp.model.orders.CustomerData;
import com.signity.shopkeeperapp.model.orders.Data;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeliveryFragment extends Fragment {

    public static final String TAG = "DeliveryFragment";
    private TextInputEditText editTextMobileNumber, editTextName, editTextLoyaltyPoints, editTextEmail;
    private TextView textViewCustomerName, textViewCustomerNumber, textViewCustomerAddress, textViewAddressType;
    private TextInputEditText editTextScheduleDate, editTextScheduleTime, editTextDescription;
    private ImageView imageViewSearch;
    private LinearLayout linearLayoutAddDelivery, linearLayoutDeliveryAddress, linearLayoutNext;
    private String mobile;
    private int yearInt, dayInt, monthInt, hourOfDayInt, minuteInt;

    public static DeliveryFragment getInstance(Bundle bundle) {
        DeliveryFragment fragment = new DeliveryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getExtra();
        checkNumber();
    }

    private void getExtra() {
        if (getArguments() != null) {
            mobile = getArguments().getString("NUMBER");
            editTextMobileNumber.setText(mobile);
        }
    }

    private void initView(View view) {
        editTextMobileNumber = view.findViewById(R.id.edt_delivery_mobile);
        editTextName = view.findViewById(R.id.edt_delivery_name);
        editTextLoyaltyPoints = view.findViewById(R.id.edt_delivery_loyalty_points);
        editTextEmail = view.findViewById(R.id.edt_delivery_email);
        editTextScheduleDate = view.findViewById(R.id.edt_delivery_date);
        editTextScheduleTime = view.findViewById(R.id.edt_delivery_time);
        editTextDescription = view.findViewById(R.id.edt_delivery_description);
        imageViewSearch = view.findViewById(R.id.iv_delivery_search);
        linearLayoutDeliveryAddress = view.findViewById(R.id.ll_delivery);
        linearLayoutAddDelivery = view.findViewById(R.id.ll_add_address);
        linearLayoutNext = view.findViewById(R.id.ll_next);

        textViewCustomerName = view.findViewById(R.id.tv_customer_name);
        textViewCustomerNumber = view.findViewById(R.id.tv_customer_number);
        textViewCustomerAddress = view.findViewById(R.id.tv_customer_address);
        textViewAddressType = view.findViewById(R.id.tv_address_type);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validMobile()) {
                    checkNumber();
                }
            }
        });

        editTextMobileNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    if (validMobile()) {
                        checkNumber();
                    }
                    return true;
                }
                return false;
            }
        });

        linearLayoutAddDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - Open dialog to add address
            }
        });

        editTextScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearInt = year;
                        monthInt = month;
                        dayInt = dayOfMonth;

                        String date = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, month + 1, year);
                        editTextScheduleDate.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                calendar.add(Calendar.DATE, 180);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        editTextScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hourOfDayInt = hourOfDay;
                        minuteInt = minute;
                        if (getTime())
                            editTextScheduleTime.setText(Util.getTimeFrom(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 30, false);

                timePickerDialog.show();
            }
        });

        linearLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderCheckout();
            }
        });
    }

    private boolean getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearInt, monthInt, dayInt, hourOfDayInt, minuteInt, 0);
        calendar.setTimeZone(TimeZone.getDefault());
        long startTime = calendar.getTimeInMillis();

        Date date = new Date();
        int diff = Math.round((startTime - date.getTime()) / 60000);

        if (diff < 25) {
            Toast.makeText(getContext(), "Delivery time invalid", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void orderCheckout() {

        mobile = editTextMobileNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String loyalty = editTextLoyaltyPoints.getText().toString().trim();
        String date = editTextScheduleDate.getText().toString().trim();
        String time = editTextScheduleTime.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String deliveryAddress = textViewCustomerAddress.getText().toString().trim();

        if (!validMobile()) {
            return;
        }

        if (TextUtils.isEmpty(name)) {
            editTextName.requestFocus();
            Toast.makeText(getContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(deliveryAddress)) {
            Toast.makeText(getContext(), "Address can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(getContext(), "Delivery Date can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "Deliver Time can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(BookOrderCheckoutActivity.getIntent(getContext()));
        AnimUtil.slideFromRightAnim(getActivity());
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkNumber() {

        if (TextUtils.isEmpty(mobile)) {
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);

        ProgressDialogUtil.showProgressDialog(getContext());
        NetworkAdaper.getNetworkServices().checkNumber(param, new Callback<CustomerData>() {
            @Override
            public void success(CustomerData customerData, Response response) {

                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (customerData.isSuccess()) {
                    populateFields(customerData.getData());
                } else {
                    Toast.makeText(getContext(), customerData.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void populateFields(Data data) {

        if (data == null) {
            return;
        }

        editTextName.setText(data.getFullName());
        editTextEmail.setText(data.getEmail());
        editTextLoyaltyPoints.setText(String.valueOf(data.getLoyalityPoints()));
        if (data.getCustomerAddress() != null && !data.getCustomerAddress().isEmpty()) {
            linearLayoutAddDelivery.setVisibility(View.GONE);
            linearLayoutDeliveryAddress.setVisibility(View.VISIBLE);

            CustomerAddressResponse response = data.getCustomerAddress().get(0);

            textViewCustomerName.setText(response.getFirstName());
            textViewCustomerNumber.setText(response.getMobile());
            textViewCustomerAddress.setText(String.format("%s, %s", response.getAddress(), response.getCity()));
            textViewAddressType.setText("Home");
        }

    }

    private boolean validMobile() {

        mobile = editTextMobileNumber.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "Mobile Number can't be empty", Toast.LENGTH_SHORT).show();
            editTextMobileNumber.requestFocus();
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery_type, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
