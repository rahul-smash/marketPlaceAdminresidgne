package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.Variant;
import com.signity.shopkeeperapp.model.orders.ProductModel;
import com.signity.shopkeeperapp.model.orders.checkout.DataResponse;
import com.signity.shopkeeperapp.model.orders.checkout.OrderCalculationResponse;
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
        param.put("shipping", 0);
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
                            populateData(calculationResponse.getData());
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
        }
    }

    private void populateData(DataResponse data) {

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

        textViewTotal.setText(Util.getPriceWithCurrency(Double.parseDouble(data.getItemSubTotal()), AppPreference.getInstance().getCurrency()));
        textViewDeliveryCharges.setText(Util.getPriceWithCurrency(Double.parseDouble(data.getShipping()), AppPreference.getInstance().getCurrency()));
        textViewTax.setText(Util.getPriceWithCurrency(Double.parseDouble(data.getTax()), AppPreference.getInstance().getCurrency()));
        textViewCouponDiscount.setText(Util.getPriceWithCurrency(Double.parseDouble(data.getDiscount()), AppPreference.getInstance().getCurrency()));
        textViewMrpDiscount.setText(Util.getPriceWithCurrency(0, AppPreference.getInstance().getCurrency()));
        textViewLoyaltyPoints.setText(Util.getPriceWithCurrency(0, AppPreference.getInstance().getCurrency()));
        textViewCartSaving.setText(String.format("Cart Savings: %s", Util.getPriceWithCurrency(Double.parseDouble(data.getDiscount()), AppPreference.getInstance().getCurrency())));
        textViewFinalAmount.setText(Util.getPriceWithCurrency(Double.parseDouble(data.getTotal()), AppPreference.getInstance().getCurrency()));
        textViewOrderTotal.setText(Util.getPriceWithCurrency(Double.parseDouble(data.getTotal()), AppPreference.getInstance().getCurrency()));
    }

    private void setUpAdapter() {
        bookOrderCheckoutAdapter = new BookOrderCheckoutAdapter(this);
        recyclerViewOrders.setAdapter(bookOrderCheckoutAdapter);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        paymentModeAdapter = new PaymentModeAdapter(this);
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
