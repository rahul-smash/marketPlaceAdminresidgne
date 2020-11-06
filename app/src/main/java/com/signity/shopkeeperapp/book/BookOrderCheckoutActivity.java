package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.AddAddressModel;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.model.orders.ProductModel;
import com.signity.shopkeeperapp.model.orders.checkout.DataResponse;
import com.signity.shopkeeperapp.model.orders.checkout.OrderCalculationResponse;
import com.signity.shopkeeperapp.model.orders.checkout.OrderDetailResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BookOrderCheckoutActivity extends BaseActivity {
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    public static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
    public static final String CUSTOMER_ADDRESS_ID = "CUSTOMER_ADDRESS_ID";
    public static final String ORDER_TYPE = "ORDER_TYPE";
    public static final String CHARGES = "CHARGES";
    private static final String TAG = "BookOrderCheckoutActivity";
    private Toolbar toolbar;
    private BookOrderCheckoutAdapter bookOrderCheckoutAdapter;
    private PaymentModeAdapter paymentModeAdapter;
    private RecyclerView recyclerViewOrders;
    private RecyclerView recyclerViewPaymentMode;
    private TextView textViewCount;
    private String userId;
    private ConstraintLayout constraintLayoutLoyalty, constraintLayoutCoupon;
    private TextView textViewTotal, textViewMrpDiscount, textViewCouponDiscount, textViewLoyaltyPoints, textViewDeliveryCharges, textViewTax, textViewFinalAmount, textViewCartSaving;
    private ConstraintLayout constraintLayoutProceed;
    private LinearLayout linearLayoutMain;
    private TextView textviewOrderItemsCount, textViewOrderTotal;
    private String orderType, customerAddress, customerAddressId, charges;
    private DataResponse dataResponse;
    private String paymentMode;

    public static Intent getIntent(Context context) {
        return new Intent(context, BookOrderCheckoutActivity.class);
    }

    public static Intent getIntent(Context context, Bundle bundle) {
        Intent intent = getIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order_checkout);
        getExtra();
        initViews();
        setUpToolbar();
        setUpAdapter();
        calculateAmount();
    }

    private void calculateAmount() {

        List<ProductModel> productModels = new ArrayList<>();
        for (GetProductData productData : OrderCart.getOrderCartMap().values()) {
            ProductModel model = new ProductModel();
            model.setProductId(productData.getId());
            model.setProductName(productData.getTitle());
            model.setIsTaxEnable(productData.getIsTaxEnable());
            model.setQuantity(productData.getCount());

            Variant selectedVariant = productData.getVariants().get(productData.getSelectedVariantIndex());
            model.setVariantId(selectedVariant.getId());
            model.setUnitType(selectedVariant.getUnitType());
            model.setMrpPrice(selectedVariant.getMrpPrice());
            model.setWeight(selectedVariant.getWeight());
            model.setDiscount(selectedVariant.getDiscount());
            model.setPrice(selectedVariant.getPrice());

            productModels.add(model);
        }

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(productModels, new TypeToken<List<ProductModel>>() {
        }.getType());
        JsonArray jsonArrayImage = element.getAsJsonArray();

        Map<String, Object> param = new HashMap<>();
        param.put("shipping", charges);
        param.put("discount", 0);
        param.put("tax", 0);
        param.put("fixed_discount_amount", 0);
        param.put("user_id", userId);
        param.put("order_detail", jsonArrayImage.toString());

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.orderNetworkServices(AppPreference.getInstance()
                .getStoreId())
                .calculateOrder(param, new Callback<OrderCalculationResponse>() {
                    @Override
                    public void success(OrderCalculationResponse calculationResponse, Response response) {

                        if (isDestroyed()) {
                            return;
                        }
                        ProgressDialogUtil.hideProgressDialog();
                        if (calculationResponse.isSuccess()) {
                            dataResponse = calculationResponse.getData();
                            populateData();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ProgressDialogUtil.hideProgressDialog();
                    }
                });
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString(CUSTOMER_ID);
            customerAddress = bundle.getString(CUSTOMER_ADDRESS);
            customerAddressId = bundle.getString(CUSTOMER_ADDRESS_ID);
            orderType = bundle.getString(ORDER_TYPE);
            charges = bundle.getString(CHARGES);
        }
    }

    private void populateData() {

        linearLayoutMain.setVisibility(View.VISIBLE);
        constraintLayoutProceed.setVisibility(View.VISIBLE);

        String item = OrderCart.getOrderCartMap().size() > 1 ? "Items" : "Item";
        textViewCount.setText(String.format("%s %s", OrderCart.getOrderCartMap().size(), item));
        textviewOrderItemsCount.setText(String.format("%s %s", OrderCart.getOrderCartMap().size(), item));

        List<GetProductData> list = new ArrayList<>();
        if (!OrderCart.isCartEmpty()) {
            list.addAll(OrderCart.getOrderCartMap().values());
        }
        bookOrderCheckoutAdapter.setProductData(list);

        try {
            textViewTotal.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getItemSubTotal()), AppPreference.getInstance().getCurrency()));
            textViewDeliveryCharges.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getShipping()), AppPreference.getInstance().getCurrency()));
            textViewTax.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getTax()), AppPreference.getInstance().getCurrency()));
            textViewCouponDiscount.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getDiscount()), AppPreference.getInstance().getCurrency()));
            textViewMrpDiscount.setText(Util.getPriceWithCurrency(0, AppPreference.getInstance().getCurrency()));
            textViewLoyaltyPoints.setText(Util.getPriceWithCurrency(0, AppPreference.getInstance().getCurrency()));
            textViewCartSaving.setText(String.format("Cart Savings: %s", Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getDiscount()), AppPreference.getInstance().getCurrency())));
            textViewFinalAmount.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getTotal()), AppPreference.getInstance().getCurrency()));
            textViewOrderTotal.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getTotal()), AppPreference.getInstance().getCurrency()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpAdapter() {
        bookOrderCheckoutAdapter = new BookOrderCheckoutAdapter(this);
        recyclerViewOrders.setAdapter(bookOrderCheckoutAdapter);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        paymentModeAdapter = new PaymentModeAdapter(this);
        paymentModeAdapter.setListener(new PaymentModeAdapter.PaymentModeListener() {
            @Override
            public void onPaymentMode(String mode) {
                paymentMode = mode;
            }
        });
        recyclerViewPaymentMode.setAdapter(paymentModeAdapter);
        recyclerViewPaymentMode.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewPaymentMode.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 4)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewOrders = findViewById(R.id.rv_orders_checkout);
        recyclerViewPaymentMode = findViewById(R.id.rv_payment_mode);
        textViewCount = findViewById(R.id.tv_product_count);
        constraintLayoutCoupon = findViewById(R.id.const_apply_coupon);
        constraintLayoutLoyalty = findViewById(R.id.const_apply_loyalty);

        textViewTotal = findViewById(R.id.tv_total_price);
        textViewMrpDiscount = findViewById(R.id.tv_mpr_discount);
        textViewCouponDiscount = findViewById(R.id.tv_coupon_discount);
        textViewLoyaltyPoints = findViewById(R.id.tv_loyalty_points);
        textViewDeliveryCharges = findViewById(R.id.tv_delivery_amount);
        textViewTax = findViewById(R.id.tv_tax);
        textViewFinalAmount = findViewById(R.id.tv_payable_amount);
        textViewCartSaving = findViewById(R.id.tv_cart_saving);
        linearLayoutMain = findViewById(R.id.ll_main);
        constraintLayoutProceed = findViewById(R.id.const_proceed);

        textviewOrderItemsCount = findViewById(R.id.tv_order_items);
        textViewOrderTotal = findViewById(R.id.tv_order_total);

        constraintLayoutCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CouponsActivity.getStartIntent(BookOrderCheckoutActivity.this));
                AnimUtil.slideFromRightAnim(BookOrderCheckoutActivity.this);
            }
        });

        constraintLayoutLoyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoyaltyPointsActivity.getStartIntent(BookOrderCheckoutActivity.this));
                AnimUtil.slideFromRightAnim(BookOrderCheckoutActivity.this);
            }
        });

        constraintLayoutProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(paymentMode)) {
                    Toast.makeText(BookOrderCheckoutActivity.this, "Select payment mode", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (orderType.equals("Delivery")) {
                    placeOrder();
                } else {
                    placeOrderPickDine();
                }
            }
        });
    }

    private void placeOrder() {

        if (dataResponse == null) {
            return;
        }

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(dataResponse.getOrderDetail(), new TypeToken<List<OrderDetailResponse>>() {
        }.getType());
        JsonArray jsonArrayImage = element.getAsJsonArray();

        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Map<String, Object> param = new HashMap<>();
        param.put("user_id", userId);
        param.put("device_id", deviceId);
        param.put("platform", 2);
        param.put("payment_method", paymentMode.equalsIgnoreCase("Cash") ? "cod" : "online");
        param.put("user_address_id", customerAddressId);
        param.put("total", dataResponse.getItemSubTotal());
        param.put("user_address", customerAddress);
        param.put("shipping_charges", dataResponse.getShipping());
        param.put("discount", dataResponse.getDiscount());
        param.put("cart_saving", dataResponse.getDiscount());
        param.put("checkout", dataResponse.getTotal());
        param.put("wallet_refund", dataResponse.getWalletRefund());
        param.put("coupon_code", "");
        param.put("tax", dataResponse.getTax());
        if (dataResponse.getTaxDetail() != null && dataResponse.getTaxDetail().size() > 0) {
            param.put("tax_rate", dataResponse.getTaxDetail().get(0).getRate());
        }
        param.put("store_tax_rate_detail", "");
        param.put("calculated_tax_detail", "");
        param.put("store_fixed_tax_detail", "");
        param.put("delivery_time_slot", "");
        param.put("orders", jsonArrayImage.toString());

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.orderNetworkServices(AppPreference.getInstance().getStoreId())
                .placeOrder(param, new Callback<AddAddressModel>() {
                    @Override
                    public void success(AddAddressModel s, Response response) {

                        if (isDestroyed()) {
                            return;
                        }
                        ProgressDialogUtil.hideProgressDialog();
                        handleResponse(s);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (isDestroyed()) {
                            return;
                        }
                        ProgressDialogUtil.hideProgressDialog();

                    }
                });
    }

    private void placeOrderPickDine() {

        if (dataResponse == null) {
            return;
        }

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(dataResponse.getOrderDetail(), new TypeToken<List<OrderDetailResponse>>() {
        }.getType());
        JsonArray jsonArrayImage = element.getAsJsonArray();

        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Map<String, Object> param = new HashMap<>();
        param.put("user_id", userId);
        param.put("device_id", deviceId);
        param.put("platform", 2);
        param.put("payment_method", paymentMode.equalsIgnoreCase("Cash") ? "cod" : "online");
        param.put("user_address_id", TextUtils.isEmpty(customerAddressId) ? "1" : customerAddressId);
        param.put("total", dataResponse.getItemSubTotal());
        param.put("user_address", customerAddress);
        param.put("shipping_charges", dataResponse.getShipping());
        param.put("discount", dataResponse.getDiscount());
        param.put("cart_saving", dataResponse.getDiscount());
        param.put("checkout", dataResponse.getTotal());
        param.put("wallet_refund", dataResponse.getWalletRefund());
        param.put("coupon_code", "");
        param.put("tax", dataResponse.getTax());
        if (dataResponse.getTaxDetail() != null && dataResponse.getTaxDetail().size() > 0) {
            param.put("tax_rate", dataResponse.getTaxDetail().get(0).getRate());
        }
        param.put("store_tax_rate_detail", "");
        param.put("calculated_tax_detail", "");
        param.put("store_fixed_tax_detail", "");
        param.put("delivery_time_slot", "");
        param.put("orders", jsonArrayImage.toString());
        param.put("order_facility", orderType.equalsIgnoreCase("DineIn") ? "Dining" : orderType);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.orderNetworkServices(AppPreference.getInstance().getStoreId())
                .placeOrderPickDine(param, new Callback<AddAddressModel>() {
                    @Override
                    public void success(AddAddressModel s, Response response) {

                        if (isDestroyed()) {
                            return;
                        }

                        ProgressDialogUtil.hideProgressDialog();

                        handleResponse(s);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (isDestroyed()) {
                            return;
                        }
                        ProgressDialogUtil.hideProgressDialog();
                    }
                });
    }

    private void handleResponse(AddAddressModel s) {
        if (s.isSuccess()) {
            OrderCart.clearOrderCartMap();
            finishAffinity();
            AnimUtil.slideFromLeftAnim(BookOrderCheckoutActivity.this);
        } else {
            Toast.makeText(BookOrderCheckoutActivity.this, s.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                AnimUtil.slideFromLeftAnim(BookOrderCheckoutActivity.this);
            }
        });
    }
}
