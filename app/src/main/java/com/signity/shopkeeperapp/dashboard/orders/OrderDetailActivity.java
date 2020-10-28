package com.signity.shopkeeperapp.dashboard.orders;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.OrderItemResponseModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.notifications.NotificationDialog;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
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

import static android.content.Intent.ACTION_DIAL;

public class OrderDetailActivity extends BaseActivity implements OrderDetailsAdpater.OrderDetailListener, HomeOrdersAdapter.OrdersListener {

    public static final String ORDER_OBJECT = "OrderObject";
    private TextView textViewTotalPrice, textViewPayableAmount, textViewCartSavings, textViewAddress, textViewDateTime, textViewDeliveryTimeSlot, textViewNote;
    private RecyclerView recyclerView1;
    private OrderDetailsAdpater orderDetailsAdpater;
    private OrdersListModel ordersListModel;
    private Toolbar toolbar;
    private TextView textViewDeliveryCharges;
    private TextView textViewCouponDiscount;
    private TextView textViewMrpDiscount;
    private String orderId;
    private RecyclerView recyclerViewOrders;
    private HomeOrdersAdapter ordersAdapter;
    private TextView textViewCustomerName;
    private TextView textViewCustomerNumber;
    private LinearLayout linearLayoutDeliveryDetail;
    private ConstraintLayout constraintLayoutPaymentDetail;
    private TextView textViewCouponCode;
    private LinearLayout linearLayoutDiscountCoupon;
    private LinearLayout linearLayoutNote;
    private TextView textViewDeliveryDateSlot;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OrderDetailActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity_new);
        getExtra();
        initView();
        setUpToolbar();
        setUpAdapter();
        getOrderDetail();
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ordersListModel = (OrdersListModel) bundle.getSerializable(ORDER_OBJECT);
            orderId = ordersListModel.getOrderId();
        }
    }

    private void getOrderDetail() {
        ProgressDialogUtil.showProgressDialog(this);
        Map<String, String> param = new HashMap<String, String>();
        param.put("order_id", orderId);

        NetworkAdaper.getNetworkServices().getOrderDetail(param, new Callback<StoreOrdersReponse>() {
            @Override
            public void success(StoreOrdersReponse getValues, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (getValues.isSuccess()) {
                    if (getValues.getData() != null && getValues.getData().getOrders() != null) {
                        if (getValues.getData().getOrders().size() > 0) {
                            ordersListModel = getValues.getData().getOrders().get(0);
                            setOrderDetails();
                            linearLayoutDeliveryDetail.setVisibility(ordersListModel.getOrderFacility().equalsIgnoreCase("pickup") ? View.GONE : View.VISIBLE);
                            constraintLayoutPaymentDetail.setVisibility(View.VISIBLE);
                            boolean val = ordersListModel.getStatus().equals("0");
                            orderDetailsAdpater.setItemListModels(ordersListModel.getItems(), val);

                            ordersListModel.setPageNumber(1);

                            List<OrdersListModel> ordersListModels = new ArrayList<>();
                            ordersListModels.add(ordersListModel);

                            Map<Integer, List<OrdersListModel>> newMapData = new HashMap<>();
                            newMapData.put(1, ordersListModels);

                            ordersAdapter.setPageOrdersMap(newMapData);
                        }
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(OrderDetailActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
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
                onBackPressed();
                AnimUtil.slideFromLeftAnim(OrderDetailActivity.this);
            }
        });
    }

    private void setUpAdapter() {
        orderDetailsAdpater = new OrderDetailsAdpater(this);
        orderDetailsAdpater.setListener(this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(orderDetailsAdpater);

        ordersAdapter = new HomeOrdersAdapter(this);
        ordersAdapter.setListener(this);
        recyclerViewOrders.setAdapter(ordersAdapter);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setOrderDetails() {

        textViewCustomerName.setText(ordersListModel.getCustomerName());
        textViewAddress.setText(ordersListModel.getAddress());
        textViewCustomerNumber.setText(ordersListModel.getPhone());
        textViewDateTime.setText(ordersListModel.getTime());

        if (!TextUtils.isEmpty(ordersListModel.getNote())) {
            linearLayoutNote.setVisibility(View.VISIBLE);
            textViewNote.setText(ordersListModel.getNote());
        }

        if (!TextUtils.isEmpty(ordersListModel.getDeliveryTimeSlot())) {
            try {
                String[] timeSlot = ordersListModel.getDeliveryTimeSlot().split(" ", 2);
                textViewDeliveryDateSlot.setText(Util.getDeliverySlotDate(timeSlot[0]));
                textViewDeliveryTimeSlot.setText(timeSlot[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (ordersListModel.getDiscount() != 0) {
            linearLayoutDiscountCoupon.setVisibility(View.VISIBLE);
            textViewCouponCode.setText(String.format("Coupon Applied(%s)", ordersListModel.getCouponCode().toUpperCase()));
        }
        textViewTotalPrice.setText(Util.getPriceWithCurrency(ordersListModel.getCheckout(), AppPreference.getInstance().getCurrency()));
        textViewMrpDiscount.setText(Util.getPriceWithCurrency(0, AppPreference.getInstance().getCurrency()));
        textViewCouponDiscount.setText(Util.getPriceWithCurrency(ordersListModel.getDiscount(), AppPreference.getInstance().getCurrency()));
        textViewDeliveryCharges.setText(Util.getPriceWithCurrency(ordersListModel.getShippingCharges(), AppPreference.getInstance().getCurrency()));
        textViewPayableAmount.setText(Util.getPriceWithCurrency(ordersListModel.getTotal(), AppPreference.getInstance().getCurrency()));
        textViewCartSavings.setText(String.format("Cart Savings: %s", Util.getPriceWithCurrency(ordersListModel.getDiscount(), AppPreference.getInstance().getCurrency())));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView1 = findViewById(R.id.recyclerView);
        textViewTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        textViewPayableAmount = (TextView) findViewById(R.id.tv_payable_amount);
        textViewCartSavings = (TextView) findViewById(R.id.tv_cart_saving);
        textViewAddress = (TextView) findViewById(R.id.tv_address);
        textViewCustomerName = (TextView) findViewById(R.id.tv_customer_name);
        textViewCustomerNumber = (TextView) findViewById(R.id.tv_customer_number);
        textViewDateTime = (TextView) findViewById(R.id.tv_order_date_time);
        textViewDeliveryTimeSlot = (TextView) findViewById(R.id.tv_delivery_time_slot);
        textViewDeliveryDateSlot = (TextView) findViewById(R.id.tv_delivery_date_slot);
        textViewNote = (TextView) findViewById(R.id.tv_note);
        linearLayoutNote = findViewById(R.id.ll_note);
        textViewDeliveryCharges = (TextView) findViewById(R.id.tv_delivery_amount);
        textViewCouponDiscount = (TextView) findViewById(R.id.tv_coupon_discount);
        textViewMrpDiscount = (TextView) findViewById(R.id.tv_mpr_discount);
        recyclerViewOrders = findViewById(R.id.rv_orders);
        linearLayoutDeliveryDetail = findViewById(R.id.ll_delivery_detail);
        constraintLayoutPaymentDetail = findViewById(R.id.const_payment_details);
        textViewCouponCode = findViewById(R.id.tv_coupon_code);
        linearLayoutDiscountCoupon = findViewById(R.id.ll_discount_coupon);

        findViewById(R.id.ll_guide_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
    }

    private void openMap() {

        String lat = ordersListModel.getDestinationuser_lat();
        String lon = ordersListModel.getDestinationuser_lng();

        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) {
            String map = "http://maps.google.co.in/maps?q=" + ordersListModel.getAddress();
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + lat + "," + lon));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void callAlert() {
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(OrderDetailActivity.this);
        adb.setTitle("Call " + ordersListModel.getPhone() + " ?");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                actionCall();
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void actionCall() {
        try {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ordersListModel.getPhone()));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(OrderDetailActivity.this);
            } else {
                Toast.makeText(getApplicationContext(), "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callOrderItemStatus(String itemID, String status) {

        ProgressDialogUtil.showProgressDialog(OrderDetailActivity.this);
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", ordersListModel.getUserId());
        param.put("order_id", ordersListModel.getOrderId());
        param.put("item_ids", itemID);
        param.put("item_status", status);

        NetworkAdaper.getNetworkServices().setOrderItemStatus(param, new Callback<OrderItemResponseModel>() {
            @Override
            public void success(OrderItemResponseModel orderItemResponseModel, Response response) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (orderItemResponseModel.getSuccess()) {
                    OrdersListModel ordersListModelTemp = orderItemResponseModel.getOrdersListModel();
                    if (ordersListModelTemp != null) {
                        ordersListModel = ordersListModelTemp;
                        setOrderDetails();
                        boolean val = ordersListModel.getStatus().equals("0");
                        orderDetailsAdpater.setItemListModels(ordersListModel.getItems(), val);

                        ordersListModel.setPageNumber(1);

                        List<OrdersListModel> ordersListModels = new ArrayList<>();
                        ordersListModels.add(ordersListModel);

                        Map<Integer, List<OrdersListModel>> newMapData = new HashMap<>();
                        newMapData.put(1, ordersListModels);

                        ordersAdapter.setPageOrdersMap(newMapData);
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_orders_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChangeStatus(String itemId, String status) {
        callOrderItemStatus(itemId, status);
    }

    @Override
    public void onClickOrder(int position, int pageNumber) {

    }

    @Override
    public void onRejectOrder(final int position, final int pageNumber) {
        RejectOrderDialog rejectOrderDialog = RejectOrderDialog.getInstance(null);
        rejectOrderDialog.setListener(new RejectOrderDialog.DialogListener() {
            @Override
            public void onSubmit(String message) {
                OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
                updateOrderStatus(HomeOrdersAdapter.OrderType.REJECTED, order.getOrderId(), pageNumber,message);
            }
        });
        rejectOrderDialog.show(getSupportFragmentManager(), NotificationDialog.TAG);
    }

    @Override
    public void onAcceptOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ACCEPTED, order.getOrderId(), position,"");
    }

    @Override
    public void onShipOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.SHIPPED, order.getOrderId(), position,"");
    }

    @Override
    public void onDeliverOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.DELIVERED, order.getOrderId(), position,"");
    }

    @Override
    public void onWhatsappMessage(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        String phone = order.getPhone();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        openWhatsapp(phone);
    }

    private void openWhatsapp(String phone) {
        try {
            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled) {

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                String data = String.format("%s%s@s.whatsapp.net", AppPreference.getInstance().getPhoneCode(), phone);
                sendIntent.putExtra("jid", data);//phone number without "+" prefix
                startActivity(sendIntent);
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean appInstalled = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    @Override
    public void onCallCustomer(int position) {
        if (TextUtils.isEmpty(ordersListModel.getPhone())) {
            DialogUtils.showAlertDialog(getApplicationContext(), Constant.APP_TITLE, "Sorry! phone number is not available.");
        } else {
            callAlert();
        }
    }

    private void updateOrderStatus(HomeOrdersAdapter.OrderType orderStatus, String orderId, final int position,String message) {
        ProgressDialogUtil.showProgressDialog(this);
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", AppPreference.getInstance().getUserId());
        param.put("order_status", String.valueOf(orderStatus.getStatusId()));
        param.put("order_ids", orderId);
        param.put("message", message);

        NetworkAdaper.getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (getValues.getSuccess()) {
                    getOrderDetail();
                } else {
                    Toast.makeText(OrderDetailActivity.this, getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(OrderDetailActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
