package com.signity.shopkeeperapp.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.signity.shopkeeperapp.model.orders.offers.ApplyCouponDTO;
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
    public static final String LOYALTY = "LOYALTY";
    public static final String DELIVERY_SLOT = "DELIVERY_SLOT";
    private static final String TAG = "BookOrderCheckoutActivity";
    private static final int REQUEST_COUPON = 1122;
    private static final int REQUEST_LOYALTY = 3344;
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
    private List<ProductModel> productModels = new ArrayList<>();
    private String coupon = "";
    private String discount = "0";
    private TextView textViewApplyLoyalty, textViewApplyCoupon, textViewLoyaltyPoint;
    private ImageView imageViewLoyalty, imageViewCoupon;
    private String loyalty = "0";
    private String point = "";
    private int showLoyalty = 0;
    private String deliverySlot = "";
    private double cartSaving;

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

        productModels.clear();
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
        param.put("fixed_discount_amount", discount);
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
            showLoyalty = bundle.getInt(LOYALTY);
            deliverySlot = bundle.getString(DELIVERY_SLOT);
        }
    }

    private void populateData() {

        linearLayoutMain.setVisibility(View.VISIBLE);
        constraintLayoutProceed.setVisibility(View.VISIBLE);

        String item = OrderCart.getOrderCartMap().size() > 1 ? "Items" : "Item";
        textViewCount.setText(String.format("%s %s", OrderCart.getOrderCartMap().size(), item));
        textviewOrderItemsCount.setText(String.format("%s %s", OrderCart.getOrderCartMap().size(), item));

        double mrpDiscount = 0;
        try {
            for (OrderDetailResponse response : dataResponse.getOrderDetail()) {
                double mrp = Double.parseDouble(response.getMrpPrice());
                double price = Double.parseDouble(response.getPrice());
                mrpDiscount += (mrp - price) * response.getQuantity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<GetProductData> list = new ArrayList<>();
        if (!OrderCart.isCartEmpty()) {
            list.addAll(OrderCart.getOrderCartMap().values());
        }
        bookOrderCheckoutAdapter.setProductData(list);

        cartSaving = mrpDiscount;
        if (!TextUtils.isEmpty(dataResponse.getDiscount())) {
            try {
                cartSaving += Double.parseDouble(dataResponse.getDiscount());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        try {
            textViewTotal.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getItemSubTotal()), AppPreference.getInstance().getCurrency()));
            textViewDeliveryCharges.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getShipping()), AppPreference.getInstance().getCurrency()));
            textViewTax.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getTax()), AppPreference.getInstance().getCurrency()));
            textViewCouponDiscount.setText(Util.getPriceWithCurrency(Double.parseDouble(loyalty.equalsIgnoreCase("0") ? dataResponse.getDiscount() : "0"), AppPreference.getInstance().getCurrency()));
            textViewMrpDiscount.setText(Util.getPriceWithCurrency(mrpDiscount, AppPreference.getInstance().getCurrency()));
            textViewLoyaltyPoints.setText(Util.getPriceWithCurrency(Double.parseDouble(loyalty), AppPreference.getInstance().getCurrency()));
            textViewCartSaving.setText(String.format("Cart Savings: %s", Util.getPriceWithCurrency(cartSaving, AppPreference.getInstance().getCurrency())));
            textViewFinalAmount.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getTotal()), AppPreference.getInstance().getCurrency()));
            textViewOrderTotal.setText(Util.getPriceWithCurrency(Double.parseDouble(dataResponse.getTotal()), AppPreference.getInstance().getCurrency()));
            if (!TextUtils.isEmpty(point)) {
                textViewLoyaltyPoint.setText(String.format("(%s Points)", point));
            } else {
                textViewLoyaltyPoint.setText("");
            }
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
        textViewApplyLoyalty = findViewById(R.id.tv_apply_loyalty);
        textViewApplyCoupon = findViewById(R.id.tv_apply_coupon);
        imageViewLoyalty = findViewById(R.id.iv_apply_loyalty);
        imageViewCoupon = findViewById(R.id.iv_apply_coupon);
        textViewLoyaltyPoint = findViewById(R.id.tv_loyalty_point);

        constraintLayoutLoyalty.setTag(0);
        constraintLayoutCoupon.setTag(0);

        constraintLayoutLoyalty.setVisibility(showLoyalty > 0 ? View.VISIBLE : View.GONE);

        constraintLayoutCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) constraintLayoutCoupon.getTag();
                int tagLoyalty = (int) constraintLayoutLoyalty.getTag();

                if (tagLoyalty == 1) {
                    Toast.makeText(BookOrderCheckoutActivity.this, "Please remove existing discount first", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tag == 0) {
                    startActivityForResult(CouponsActivity.getStartIntent(BookOrderCheckoutActivity.this), REQUEST_COUPON);
                    AnimUtil.slideFromRightAnim(BookOrderCheckoutActivity.this);
                } else {
                    couponLayoutVisibility(false);
                    coupon = "";
                    discount = "0";
                    calculateAmount();
                }
            }
        });

        constraintLayoutLoyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) constraintLayoutLoyalty.getTag();
                int tagCoupon = (int) constraintLayoutCoupon.getTag();

                if (tagCoupon == 1) {
                    Toast.makeText(BookOrderCheckoutActivity.this, "Please remove existing discount first", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tag == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(LoyaltyPointsActivity.USER_ID, userId);
                    startActivityForResult(LoyaltyPointsActivity.getStartIntent(BookOrderCheckoutActivity.this, bundle), REQUEST_LOYALTY);
                    AnimUtil.slideFromRightAnim(BookOrderCheckoutActivity.this);
                } else {
                    loyaltyLayoutVisibility(false);
                    loyalty = "0";
                    coupon = "";
                    discount = "0";
                    point = "";
                    calculateAmount();
                }
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
        param.put("platform", "android");
        param.put("online_method", paymentMode.equalsIgnoreCase("Cash") ? "" : paymentMode);
        param.put("payment_method", paymentMode.equalsIgnoreCase("Cash") ? "cod" : "online");
        param.put("user_address_id", customerAddressId);
        param.put("total", dataResponse.getTotal());
        param.put("user_address", customerAddress);
        param.put("shipping_charges", dataResponse.getShipping());
        param.put("discount", dataResponse.getDiscount());
        param.put("cart_saving", cartSaving);
        param.put("checkout", dataResponse.getItemSubTotal());
        param.put("wallet_refund", dataResponse.getWalletRefund());
        param.put("coupon_code", coupon);
        param.put("tax", dataResponse.getTax());
        param.put("delivery_time_slot", deliverySlot);
        if (dataResponse.getTaxDetail() != null && dataResponse.getTaxDetail().size() > 0) {
            param.put("tax_rate", dataResponse.getTaxDetail().get(0).getRate());
        }
        param.put("store_tax_rate_detail", "");
        param.put("calculated_tax_detail", "");
        param.put("store_fixed_tax_detail", "");
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
        param.put("platform", "android");
        param.put("online_method", paymentMode.equalsIgnoreCase("Cash") ? "" : paymentMode);
        param.put("payment_method", paymentMode.equalsIgnoreCase("Cash") ? "cod" : "online");
        param.put("user_address_id", TextUtils.isEmpty(customerAddressId) ? "1" : customerAddressId);
        param.put("total", dataResponse.getTotal());
        param.put("user_address", customerAddress);
        param.put("shipping_charges", dataResponse.getShipping());
        param.put("discount", dataResponse.getDiscount());
        param.put("cart_saving", cartSaving);
        param.put("checkout", dataResponse.getItemSubTotal());
        param.put("wallet_refund", dataResponse.getWalletRefund());
        param.put("coupon_code", coupon);
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

    private void applyCoupon(String couponCode) {
        if (productModels.isEmpty()) {
            return;
        }

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(productModels, new TypeToken<List<ProductModel>>() {
        }.getType());
        JsonArray jsonArrayImage = element.getAsJsonArray();

        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Map<String, Object> param = new HashMap<>();
        param.put("platform", "android");
        param.put("payment_method", 3);
        param.put("device_token", AppPreference.getInstance().getDeviceToken());
        param.put("device_id", deviceId);
        param.put("user_id", userId);
        param.put("coupon_code", couponCode);
        param.put("orders", jsonArrayImage.toString());

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.orderNetworkServices(AppPreference.getInstance().getStoreId())
                .applyCoupon(param, new Callback<ApplyCouponDTO>() {
                    @Override
                    public void success(ApplyCouponDTO applyCouponDTO, Response response) {

                        if (isDestroyed()) {
                            return;
                        }
                        ProgressDialogUtil.hideProgressDialog();
                        if (applyCouponDTO.isSuccess()) {
                            coupon = applyCouponDTO.getData().getCouponCode();
                            discount = applyCouponDTO.getDiscountAmount();
                            calculateAmount();
                            couponLayoutVisibility(true);
                        } else {
                            Toast.makeText(BookOrderCheckoutActivity.this, applyCouponDTO.getMessage(), Toast.LENGTH_SHORT).show();
                        }

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

    private void couponLayoutVisibility(boolean value) {
        constraintLayoutCoupon.setTag(value ? 1 : 0);
        textViewApplyCoupon.setText(value ? coupon.concat(" Applied!") : "Apply Coupon");
        imageViewCoupon.setImageDrawable(getResources().getDrawable(value ? R.drawable.popupcancelicon : R.drawable.backicon));
    }

    private void loyaltyLayoutVisibility(boolean value) {
        constraintLayoutLoyalty.setTag(value ? 1 : 0);
        textViewApplyLoyalty.setText(value ? coupon.concat(" Applied!") : "Apply Loyalty Points");
        imageViewLoyalty.setImageDrawable(getResources().getDrawable(value ? R.drawable.popupcancelicon : R.drawable.backicon));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_COUPON:
                    if (data != null) {
                        String coupon = data.getStringExtra(CouponsActivity.COUPON);
                        applyCoupon(coupon);
                    }
                    break;
                case REQUEST_LOYALTY:
                    if (data != null) {
                        coupon = data.getStringExtra(LoyaltyPointsActivity.LOYALTY);
                        discount = data.getStringExtra(LoyaltyPointsActivity.DISCOUNT);
                        loyalty = data.getStringExtra(LoyaltyPointsActivity.DISCOUNT);
                        point = data.getStringExtra(LoyaltyPointsActivity.POINT);

                        try {
                            if (!TextUtils.isEmpty(point)) {
                                if (showLoyalty < Integer.parseInt(point)) {
                                    Toast.makeText(this, "Not enough loyalty points", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        calculateAmount();
                        loyaltyLayoutVisibility(true);
                    }
                    break;
            }
        }
    }
}
