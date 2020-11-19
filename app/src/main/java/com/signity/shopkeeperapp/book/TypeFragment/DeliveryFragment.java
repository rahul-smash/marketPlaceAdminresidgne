package com.signity.shopkeeperapp.book.TypeFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.book.BookOrderCheckoutActivity;
import com.signity.shopkeeperapp.model.customers.addCustomer.AddCustomerResponse;
import com.signity.shopkeeperapp.model.customers.addCustomer.DataResponse;
import com.signity.shopkeeperapp.model.customers.detail.CustomerAddressResponse;
import com.signity.shopkeeperapp.model.orders.CustomerData;
import com.signity.shopkeeperapp.model.orders.Data;
import com.signity.shopkeeperapp.model.orders.delivery.DateTimeCollectionDTO;
import com.signity.shopkeeperapp.model.orders.delivery.DeliverySlotDTO;
import com.signity.shopkeeperapp.model.orders.delivery.TimeslotDTO;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DeliveryFragment extends Fragment {

    public static final String TAG = "DeliveryFragment";
    private static final int ADDRESS_CODE = 101;
    private TextInputEditText editTextMobileNumber, editTextName, editTextLoyaltyPoints, editTextEmail;
    private TextView textViewCustomerAddress, textViewCustomerState, textViewCustomerPincode, textViewAddressType, txtChangeAddress;
    private TextInputEditText editTextScheduleDate, editTextScheduleTime, editTextDescription;
    private ImageView imageViewSearch;
    private LinearLayout linearLayoutAddDelivery, linearLayoutDeliveryAddress, linearLayoutNext;
    private String mobile;
    private String customerFirstName, customerNumber, customerID, customerAddressId, addressCharges, minAmount;
    private int yearInt, dayInt, monthInt, hourOfDayInt, minuteInt;
    private String customerAddress, customerAreaId, customerAreaName, customerCity, customerState, customerZipcode, areaId;
    private boolean isNotAllow;
    private double total;
    private RecyclerView recyclerViewDate, recyclerViewTime;
    private DeliveryDateAdapter deliveryDateAdapter;
    private DeliveryTimeAdapter deliveryTimeAdapter;
    private int selectedTimeSlot, selectedDateSlot;
    private String deliverySlot = "";

    public static DeliveryFragment getInstance(Bundle bundle) {
        DeliveryFragment fragment = new DeliveryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpAdapter();
    }

    private void setUpAdapter() {
        deliveryDateAdapter = new DeliveryDateAdapter();
        recyclerViewDate.setAdapter(deliveryDateAdapter);
        recyclerViewDate.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        deliveryTimeAdapter = new DeliveryTimeAdapter();
        recyclerViewTime.setAdapter(deliveryTimeAdapter);
        recyclerViewTime.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getExtra();
        checkNumber();
        getDeliverSlots();
    }

    private void getExtra() {
        if (getArguments() != null) {
            mobile = getArguments().getString("NUMBER");
            total = getArguments().getDouble("TOTAL");
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

        textViewCustomerAddress = view.findViewById(R.id.tv_customer_address);
        textViewCustomerState = view.findViewById(R.id.tv_customer_state_area);
        textViewCustomerPincode = view.findViewById(R.id.tv_customer_pincode);
        textViewAddressType = view.findViewById(R.id.tv_address_type);
        txtChangeAddress = view.findViewById(R.id.tv_change_address);

        recyclerViewDate = view.findViewById(R.id.rv_delivery_date);
        recyclerViewTime = view.findViewById(R.id.rv_delivery_time_slot);

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

        txtChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                intent.putExtra("address", customerAddress);
                intent.putExtra("area_name", customerAreaName);
                intent.putExtra("area_id", customerAreaId);
                intent.putExtra("city", customerCity);
                intent.putExtra("state", customerState);
                intent.putExtra("zipcode", customerZipcode);
                intent.putExtra("customer_id", customerID);
                intent.putExtra("customer_first_name", customerFirstName);
                intent.putExtra("customer_mobile", customerNumber);
                intent.putExtra("customer_address_id", customerAddressId);
                startActivityForResult(intent, ADDRESS_CODE);
            }
        });

        linearLayoutAddDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumberForAddAddress(editTextMobileNumber.getText().toString());

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
                }, calendar.get(Calendar.HOUR_OF_DAY) + 1, calendar.get(Calendar.MINUTE), false);

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

        String mobile = editTextMobileNumber.getText().toString().trim();
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

        String datee = deliveryDateAdapter.getDate();
        String timee = deliveryTimeAdapter.getTime();

        if (TextUtils.isEmpty(datee)) {
            Toast.makeText(getContext(), "Select delivery slot date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(timee)) {
            Toast.makeText(getContext(), "Select delivery slot time", Toast.LENGTH_SHORT).show();
            return;
        }

        deliverySlot = String.format("%s, %s", Util.getDeliverySlotDate1(datee), timee);

        getCustomerId(mobile);
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
                    populateEmptyData();
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

    private void checkNumberForAddAddress(String mobile) {

        if (TextUtils.isEmpty(mobile)) {
            for_empty_field();
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

                    if (customerData.getData().getCustomerAddress() == null || customerData.getData().getCustomerAddress().isEmpty()) {

                        String name = "";
                        if (!TextUtils.isEmpty(customerData.getData().getFullName())) {
                            name = customerData.getData().getFullName();
                        } else if (!TextUtils.isEmpty(editTextName.getText().toString())) {
                            name = editTextName.getText().toString();
                        } else {
                            Toast.makeText(getContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                        intent.putExtra("customer_id", customerData.getData().getId());
                        intent.putExtra("customer_first_name", name);
                        intent.putExtra("customer_mobile", customerData.getData().getPhone());
                        intent.putExtra("customer_address_id", "");
                        startActivityForResult(intent, ADDRESS_CODE);
                        return;
                    }

                    CustomerAddressResponse customerResponse = customerData.getData().getCustomerAddress().get(0);

                    String name = "";
                    if (!TextUtils.isEmpty(customerResponse.getFirstName())) {
                        name = customerResponse.getFirstName();
                    } else if (!TextUtils.isEmpty(editTextName.getText().toString())) {
                        name = editTextName.getText().toString();
                    } else {
                        Toast.makeText(getContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                    intent.putExtra("customer_id", customerResponse.getUserId());
                    intent.putExtra("customer_first_name", name);
                    intent.putExtra("customer_mobile", customerResponse.getMobile());
                    intent.putExtra("customer_address_id", customerResponse.getId());
                    startActivityForResult(intent, ADDRESS_CODE);

                } else {
                    for_empty_field();
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

    private void populateEmptyData() {

        linearLayoutAddDelivery.setVisibility(View.VISIBLE);
        linearLayoutDeliveryAddress.setVisibility(View.GONE);

        editTextName.setText("");
        editTextEmail.setText("");
        editTextLoyaltyPoints.setText("");

        textViewCustomerAddress.setText("");
        textViewCustomerState.setText("");
        textViewCustomerPincode.setText("");
    }

    private void populateFields(Data data) {

        if (data == null) {
            return;
        }

        getAddressData(data);

        editTextName.setText(data.getFullName());
        editTextEmail.setText(data.getEmail());
        editTextLoyaltyPoints.setText(String.valueOf(data.getLoyalityPoints()));
        if (data.getCustomerAddress() != null && !data.getCustomerAddress().isEmpty()) {
            linearLayoutAddDelivery.setVisibility(View.GONE);
            linearLayoutDeliveryAddress.setVisibility(View.VISIBLE);

            CustomerAddressResponse response = data.getCustomerAddress().get(0);

            textViewCustomerState.setText(String.format("%s, %s", response.getCity(), response.getState()));
            textViewCustomerAddress.setText(String.format("%s, %s", response.getAddress(), response.getAreaName()));
            textViewCustomerPincode.setText(response.getZipcode());

            if (TextUtils.isEmpty(response.getCity()) && TextUtils.isEmpty(response.getState())) {
                textViewCustomerState.setVisibility(View.GONE);
            } else {
                textViewCustomerState.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(response.getZipcode())) {
                textViewCustomerPincode.setVisibility(View.GONE);
            } else {
                textViewCustomerPincode.setVisibility(View.VISIBLE);
            }
        }

    }

    public void getAddressData(Data data) {
        if (data.getCustomerAddress() != null && !data.getCustomerAddress().isEmpty()) {

            CustomerAddressResponse response = data.getCustomerAddress().get(0);
            customerAddress = response.getAddress();
            customerAreaName = response.getAreaName();
            customerAreaId = response.getAreaId();
            customerCity = response.getCity();
            customerState = response.getState();
            customerZipcode = response.getZipcode();

            customerFirstName = response.getFirstName();
            customerNumber = response.getMobile();
            customerID = response.getUserId();
            customerAddressId = response.getId();
            addressCharges = response.getAreaCharges();
            isNotAllow = response.isNotAllow();
            minAmount = response.getMinAmount();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADDRESS_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                String result=data.getStringExtra("result");
                Log.e("Return result", data.getExtras().toString());
                try {
                    Bundle bundle = data.getExtras();
                    customerAddress = bundle.getString("address");
                    customerAreaName = bundle.getString("area_name");
                    customerAreaId = bundle.getString("area_id");
                    customerCity = bundle.getString("city");
                    customerState = bundle.getString("state");
                    customerZipcode = bundle.getString("zipcode");
                    addressCharges = bundle.getString("charges");
                    isNotAllow = bundle.getBoolean("isNotAllow");
                    minAmount = bundle.getString("minAmount");

                    if (customerAddress != null && !customerAddress.isEmpty()) {
                        linearLayoutAddDelivery.setVisibility(View.GONE);
                        linearLayoutDeliveryAddress.setVisibility(View.VISIBLE);

                        textViewCustomerState.setText(String.format("%s, %s", customerCity, customerState));
                        textViewCustomerAddress.setText(String.format("%s, %s", customerAddress, customerAreaName));
                        textViewCustomerPincode.setText(customerZipcode);

                        if (TextUtils.isEmpty(customerCity) && TextUtils.isEmpty(customerState)) {
                            textViewCustomerState.setVisibility(View.GONE);
                        } else {
                            textViewCustomerState.setVisibility(View.VISIBLE);
                        }

                        if (TextUtils.isEmpty(customerZipcode)) {
                            textViewCustomerPincode.setVisibility(View.GONE);
                        } else {
                            textViewCustomerPincode.setVisibility(View.VISIBLE);
                        }
                    }

                    Log.e("Return result", customerAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Return result", e.getMessage());
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    private void getCustomerId(String mobile) {

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
                    if (customerData.getData() != null) {
                        openCheckout(customerData.getData());
                    }
                } else {
                    addCustomer();
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

    private void addCustomer() {

        String mobile = editTextMobileNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getContext(), "Mobile Number can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(customerAddress)) {
            Toast.makeText(getContext(), "Area can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);
        param.put("name", name);
        param.put("email", email);
        param.put("address", customerAddress);
        param.put("area_id", customerAreaId);
        param.put("area_name", customerAreaName);
        param.put("city", customerCity);
        param.put("state", customerState);
        param.put("zipcode", customerZipcode);

        ProgressDialogUtil.showProgressDialog(getContext());
        NetworkAdaper.getNetworkServices().addCustomer(param, new Callback<AddCustomerResponse>() {
            @Override
            public void success(AddCustomerResponse addCategoryResponse, Response response) {

                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (addCategoryResponse.isSuccess()) {
                    if (addCategoryResponse.getData() != null) {
                        openCheckout(addCategoryResponse.getData());
                    }
                } else {
                    Toast.makeText(getActivity(), addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void openCheckout(Data data) {

        try {
            if (!TextUtils.isEmpty(addressCharges)) {
                double min = Double.parseDouble(minAmount);
                if (total >= min) {
                    addressCharges = "0";
                }
                if (total < min && isNotAllow) {
                    Toast.makeText(getContext(), "Minimum order amount required " + minAmount, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ID, data.getId());

        if (data.getCustomerAddress() != null) {
            bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ADDRESS, data.getCustomerAddress().get(0).getAddress());
            bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ADDRESS_ID, data.getCustomerAddress().get(0).getId());
        }
        bundle.putString(BookOrderCheckoutActivity.ORDER_TYPE, "Delivery");
        bundle.putString(BookOrderCheckoutActivity.CHARGES, addressCharges);
        bundle.putInt(BookOrderCheckoutActivity.LOYALTY, data.getLoyalityPoints());
        bundle.putString(BookOrderCheckoutActivity.DELIVERY_SLOT, deliverySlot);
        bundle.putString(BookOrderCheckoutActivity.ORDER_COMMENT, editTextDescription.getText().toString());
        startActivity(BookOrderCheckoutActivity.getIntent(getContext(), bundle));
        AnimUtil.slideFromRightAnim(getActivity());
    }

    private void openCheckout(DataResponse data) {

        try {
            if (!TextUtils.isEmpty(addressCharges)) {
                double min = Double.parseDouble(minAmount);
                if (total >= min) {
                    addressCharges = "0";
                }
                if (total < min && isNotAllow) {
                    Toast.makeText(getContext(), "Minimum order amount required " + minAmount, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ID, data.getStoreUser().getUserId());
        bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ADDRESS, data.getAddress().getUserAddress().getAddress());
        bundle.putString(BookOrderCheckoutActivity.CUSTOMER_ADDRESS_ID, data.getAddress().getUserAddress().getId());
        bundle.putString(BookOrderCheckoutActivity.CHARGES, addressCharges);
        bundle.putString(BookOrderCheckoutActivity.ORDER_TYPE, "Delivery");
        bundle.putString(BookOrderCheckoutActivity.DELIVERY_SLOT, deliverySlot);
        bundle.putString(BookOrderCheckoutActivity.ORDER_COMMENT, editTextDescription.getText().toString());
        String loyalty = editTextLoyaltyPoints.getText().toString();
        if (TextUtils.isEmpty(loyalty)) {
            loyalty = "0";
        }
        bundle.putInt(BookOrderCheckoutActivity.LOYALTY, Integer.parseInt(loyalty));
        startActivity(BookOrderCheckoutActivity.getIntent(getContext(), bundle));
        AnimUtil.slideFromRightAnim(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery_type, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (textViewCustomerAddress != null) {
            if (!TextUtils.isEmpty(textViewCustomerAddress.getText().toString())) {
                linearLayoutAddDelivery.setVisibility(View.GONE);
                linearLayoutDeliveryAddress.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setDateSelection(int position) {
        List<TimeslotDTO> time = deliveryDateAdapter.getDateList().get(position).getTimeslot();
        deliveryTimeAdapter.setTimeList(time);
        deliveryTimeAdapter.setSelectedIndex(0);
    }

    public void for_empty_field() {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        intent.putExtra("customer_id", "");
        intent.putExtra("customer_first_name", "");
        intent.putExtra("customer_mobile", "");
        intent.putExtra("customer_address_id", "");
        startActivityForResult(intent, ADDRESS_CODE);
    }

    private void getDeliverSlots() {
        NetworkAdaper.orderNetworkServices(AppPreference.getInstance().getStoreId())
                .getDeliverySlots(new Callback<DeliverySlotDTO>() {
                    @Override
                    public void success(DeliverySlotDTO slotDTO, Response response) {

                        if (slotDTO.isSuccess()) {
                            boolean isSlotSelected = false;

                            if (slotDTO.getData() != null && slotDTO.getData().getDateTimeCollection() != null) {
                                for (int i = 0; i < slotDTO.getData().getDateTimeCollection().size(); i++) {
                                    List<TimeslotDTO> timeslotDTO = slotDTO.getData().getDateTimeCollection().get(i).getTimeslot();
                                    for (int j = 0; j < timeslotDTO.size(); j++) {
                                        if (timeslotDTO.get(j).isIsEnable()) {
                                            selectedTimeSlot = j;
                                            isSlotSelected = true;
                                            break;
                                        }
                                    }
                                    if (isSlotSelected) {
                                        selectedDateSlot = i;
                                        break;
                                    }
                                }

                                deliveryDateAdapter.setDateList(slotDTO.getData().getDateTimeCollection());
                                deliveryDateAdapter.setSelectedIndex(selectedDateSlot);

                                deliveryTimeAdapter.setTimeList(slotDTO.getData().getDateTimeCollection().get(selectedDateSlot).getTimeslot());
                                deliveryTimeAdapter.setSelectedIndex(selectedTimeSlot);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    class DeliveryDateAdapter extends RecyclerView.Adapter<DeliveryDateAdapter.ViewHolder> {

        private List<DateTimeCollectionDTO> dateList = new ArrayList<>();
        private int selectedIndex = 0;

        public List<DateTimeCollectionDTO> getDateList() {
            return dateList;
        }

        public void setDateList(List<DateTimeCollectionDTO> dateList) {
            this.dateList = dateList;
            notifyDataSetChanged();
        }

        public String getDate() {
            if (dateList.isEmpty()) {
                return null;
            }

            return dateList.get(selectedIndex).getLabel();
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DeliveryDateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.itemview_delivery_date, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DeliveryDateAdapter.ViewHolder holder, int position) {

            String lable = dateList.get(position).getLabel();
            String date = lable.substring(0, lable.lastIndexOf(" "));

            holder.textViewDate.setText(date);
            holder.textViewDate.setTextColor(getContext().getResources().getColor(position == selectedIndex ? R.color.colorAccept : R.color.colorTextDark));
            holder.linearLayoutDate.setBackgroundColor(getContext().getResources().getColor(position == selectedIndex ? R.color.colorBackground : R.color.colorWhite));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedIndex = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    setDateSelection(selectedIndex);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dateList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewDate;
            LinearLayout linearLayoutDate;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewDate = itemView.findViewById(R.id.tv_delivery_date_time);
                linearLayoutDate = itemView.findViewById(R.id.ll_date_time);
            }
        }
    }

    class DeliveryTimeAdapter extends RecyclerView.Adapter<DeliveryTimeAdapter.ViewHolder> {

        private List<TimeslotDTO> timeList = new ArrayList<>();
        private int selectedIndex = 0;

        public void setTimeList(List<TimeslotDTO> timeList) {
            this.timeList = timeList;
            notifyDataSetChanged();
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
            notifyDataSetChanged();
        }

        public String getTime() {

            if (timeList.isEmpty()) {
                return null;
            }

            TimeslotDTO dto = timeList.get(selectedIndex);
            if (dto.isIsEnable()) {
                return dto.getValue();
            } else {
                return null;
            }
        }

        @NonNull
        @Override
        public DeliveryTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.itemview_delivery_date, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DeliveryTimeAdapter.ViewHolder holder, int position) {

            final TimeslotDTO timeslotDTO = timeList.get(position);
            holder.textViewTime.setText(timeslotDTO.getLabel());
            holder.textViewTime.setTextColor(getContext().getResources().getColor(position == selectedIndex ? R.color.colorAccept : R.color.colorTextDark));

            if (!timeslotDTO.isIsEnable()) {
                holder.textViewTime.setText(String.format("%s(%s)", timeslotDTO.getLabel(), timeslotDTO.getInnerText()));
                holder.textViewTime.setTextColor(getContext().getResources().getColor(R.color.colorTextGrey));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (timeslotDTO.isIsEnable()) {
                        selectedIndex = holder.getAdapterPosition();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), timeslotDTO.getInnerText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return timeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTime = itemView.findViewById(R.id.tv_delivery_date_time);
            }
        }
    }

}
