package com.signity.shopkeeperapp.customers;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.dashboard.orders.HomeOrdersAdapter;
import com.signity.shopkeeperapp.dashboard.orders.OrderDetailActivity;
import com.signity.shopkeeperapp.dashboard.orders.RejectOrderDialog;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.customers.detail.CustomerDetailResponse;
import com.signity.shopkeeperapp.model.customers.detail.DataResponse;
import com.signity.shopkeeperapp.model.runner.CommonResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.notifications.NotificationDialog;
import com.signity.shopkeeperapp.runner.ChooseRunnerDialog;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

public class CustomerDetailActivity extends BaseActivity implements HomeOrdersAdapter.OrdersListener {

    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    private static final String TAG = "CustomerDetailActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private HomeOrdersAdapter ordersAdapter;
    private String customerId;
    private LinearLayout linearLayoutMain;
    private TextView textViewCustomerName, textViewCustomerNumber, textViewCustomerEmail, textViewLoyaltyPoints, textViewActiveCount, textViewTotalCount, textViewAmountPaid, textViewDeliveryAddress, textViewCustomerCity;
    private DataResponse customerData;
    private ConstraintLayout constraintLayoutDelivery;
    private LinearLayout linearLayoutDelivery, linearLayoutGuide, linearLayoutArea, linearLayoutEmail;
    private ImageView imageViewWhatsapp, imageViewPhoneCall, imageViewMessage;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CustomerDetailActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CustomerDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        getExtra();
        initViews();
        setUpToolbar();
        setUpAdapter();
    }

    public void getCustomerDetail() {
        Map<String, Object> param = new HashMap<>();
        param.put("user_id", customerId);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().getCustomerDetailNew(param, new Callback<CustomerDetailResponse>() {
            @Override
            public void success(CustomerDetailResponse customerDataResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();
                if (isDestroyed()) {
                    return;
                }

                if (customerDataResponse.isSuccess()) {
                    customerData = customerDataResponse.getData();

                    if (customerData == null) {
                        return;
                    }

                    populateData();
                    Map<Integer, List<OrdersListModel>> model = new HashMap<>();
                    model.put(1, customerDataResponse.getData().getCustomerOrder());
                    ordersAdapter.setPageOrdersMap(model);
                } else {
                    Toast.makeText(CustomerDetailActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                ProgressDialogUtil.hideProgressDialog();
                if (!isDestroyed()) {
                    Toast.makeText(CustomerDetailActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateData() {

        linearLayoutMain.setVisibility(View.VISIBLE);

        if (customerData.getCustomers() != null) {
            textViewCustomerName.setText(customerData.getCustomers().getFullName());
            textViewCustomerNumber.setText(customerData.getCustomers().getPhone());
            textViewCustomerEmail.setText(customerData.getCustomers().getEmail());

            linearLayoutEmail.setVisibility(TextUtils.isEmpty(customerData.getCustomers().getEmail()) ? View.GONE : View.VISIBLE);
        }

        if (customerData.getCustomerAddress() != null) {
            textViewCustomerCity.setText(customerData.getCustomerAddress().getCity());
            linearLayoutArea.setVisibility(TextUtils.isEmpty(customerData.getCustomerAddress().getCity()) ? View.GONE : View.VISIBLE);
        } else {
            linearLayoutArea.setVisibility(View.GONE);
        }

        textViewDeliveryAddress.setText(customerData.getCustomers().getCustomerAddress());

        textViewLoyaltyPoints.setText(String.valueOf(customerData.getLoyalityPoints()));
        textViewActiveCount.setText(String.valueOf(customerData.getActiveOrders()));
        textViewTotalCount.setText(String.valueOf(customerData.getTotalOrders()));
        textViewAmountPaid.setText(Util.getPriceWithCurrency(Double.parseDouble(customerData.getPaidAmount()), AppPreference.getInstance().getCurrency()));
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerId = bundle.getString(CUSTOMER_ID);
        }
    }

    private void setUpAdapter() {
        ordersAdapter = new HomeOrdersAdapter(this, true);
        ordersAdapter.setListener(this);
        recyclerView.setAdapter(ordersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 12)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_orders);
        linearLayoutMain = findViewById(R.id.ll_main);
        textViewCustomerName = findViewById(R.id.tv_customer_address);
        textViewCustomerNumber = findViewById(R.id.tv_customer_state_area);
        textViewCustomerEmail = findViewById(R.id.tv_customer_email);
        textViewCustomerCity = findViewById(R.id.tv_customer_city);
        textViewActiveCount = findViewById(R.id.tv_active_count);
        textViewTotalCount = findViewById(R.id.tv_total_count);
        textViewAmountPaid = findViewById(R.id.tv_amount_paid);
        textViewDeliveryAddress = findViewById(R.id.tv_deliver_address);
        textViewLoyaltyPoints = findViewById(R.id.tv_loyalty_points);
        linearLayoutDelivery = findViewById(R.id.ll_deliver);
        linearLayoutGuide = findViewById(R.id.ll_guide_me);
        constraintLayoutDelivery = findViewById(R.id.const_delivery);
        imageViewWhatsapp = findViewById(R.id.iv_whatsapp);
        imageViewPhoneCall = findViewById(R.id.iv_phone_call);
        imageViewMessage = findViewById(R.id.iv_message);
        linearLayoutArea = findViewById(R.id.ll_city);
        linearLayoutEmail = findViewById(R.id.ll_email);

        constraintLayoutDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDelivery.setVisibility(linearLayoutDelivery.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        linearLayoutGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        imageViewWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerData == null || customerData.getCustomers() == null) {
                    return;
                }

                if (TextUtils.isEmpty(customerData.getCustomers().getPhone())) {
                    Toast.makeText(CustomerDetailActivity.this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
                    return;
                }

                openWhatsapp(customerData.getCustomers().getPhone());
            }
        });

        imageViewPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerData == null || customerData.getCustomers() == null) {
                    return;
                }

                if (TextUtils.isEmpty(customerData.getCustomers().getPhone())) {
                    DialogUtils.showAlertDialog(CustomerDetailActivity.this, Constant.APP_TITLE, "Sorry! phone number is not available.");
                    return;
                }

                callAlert(customerData.getCustomers().getPhone());
            }
        });

        imageViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerData == null || customerData.getCustomers() == null) {
                    return;
                }

                if (TextUtils.isEmpty(customerData.getCustomers().getPhone())) {
                    Toast.makeText(CustomerDetailActivity.this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(String.format("smsto:%s", customerData.getCustomers().getPhone()));
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);
            }
        });
    }

    private void openMap() {

        if (customerData == null || customerData.getCustomerAddress() == null) {
            return;
        }

        String lat = customerData.getCustomerAddress().getLat();
        String lon = customerData.getCustomerAddress().getLng();

        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) {
            String map = "http://maps.google.co.in/maps?q=" + customerData.getCustomerAddress().getAddress();
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

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void runAnimation() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            runAnimation();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCustomerDetail();
    }

    @Override
    public void onClickOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(OrderDetailActivity.ORDER_OBJECT, order);
        startActivity(OrderDetailActivity.getStartIntent(this, bundle));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onRejectOrder(final int position, final int pageNumber) {
        RejectOrderDialog rejectOrderDialog = RejectOrderDialog.getInstance(null);
        rejectOrderDialog.setListener(new RejectOrderDialog.DialogListener() {
            @Override
            public void onSubmit(String message) {
                OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
                updateOrderStatus(HomeOrdersAdapter.OrderType.REJECTED, order.getOrderId(), pageNumber, message);
            }
        });
        rejectOrderDialog.show(getSupportFragmentManager(), NotificationDialog.TAG);
    }

    @Override
    public void onAcceptOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ACCEPTED, order.getOrderId(), pageNumber, "");
    }

    @Override
    public void onShipOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.SHIPPED, order.getOrderId(), pageNumber, "");
    }

    @Override
    public void onReadyToBePicked(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.READY_TO_BE_PICKED, order.getOrderId(), pageNumber, "");
    }

    @Override
    public void onDeliverOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.DELIVERED, order.getOrderId(), pageNumber, "");
    }

    @Override
    public void onTheWayOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ON_THE_WAY, order.getOrderId(), pageNumber, "");
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

    @Override
    public void onCallCustomer(int position) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        String phone = order.getPhone();

        if (TextUtils.isEmpty(phone)) {
            DialogUtils.showAlertDialog(this, Constant.APP_TITLE, "Sorry! phone number is not available.");
        } else {
            callAlert(phone);
        }
    }

    @Override
    public void onAssignRunner(String runnerId, final int pageNumber, final String orderId, String areaId) {
        Bundle bundle = new Bundle();
        bundle.putString(ChooseRunnerDialog.RUNNER_ID, runnerId);
        bundle.putString(ChooseRunnerDialog.AREA_ID, areaId);
        ChooseRunnerDialog dialog = ChooseRunnerDialog.getInstance(bundle);
        dialog.setListener(new ChooseRunnerDialog.ChooseRunnerDialogListener() {
            @Override
            public void onSelectRunner(String id) {
                addRunner(id, orderId, pageNumber);
            }
        });
        dialog.show(getSupportFragmentManager(), ChooseRunnerDialog.TAG);
    }

    private void addRunner(String id, String orderId, final int pageNumber) {

        Map<String, String> param = new HashMap<>();
        param.put("order_id", orderId);
        param.put("runner_id", id);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().assignRunner(param, new Callback<CommonResponse>() {
            @Override
            public void success(CommonResponse response, Response response2) {

                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (response.isSuccess()) {
                    getCustomerDetail();
                }
                Toast.makeText(CustomerDetailActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                }
            }
        });
    }

    private void callAlert(final String phone) {
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(this);
        adb.setTitle("Call " + phone + " ?");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                actionCall(phone);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void actionCall(String phone) {
        try {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(this);
            } else {
                Toast.makeText(this, "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void updateOrderStatus(HomeOrdersAdapter.OrderType orderStatus, String orderId, final int pageNumber, String message) {
        ProgressDialogUtil.showProgressDialog(this);
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", customerId);
        param.put("order_status", String.valueOf(orderStatus.getStatusId()));
        param.put("order_ids", orderId);
        if (!TextUtils.isEmpty(message)) {
            param.put("order_rejection_note", message);
        }

        NetworkAdaper.getNetworkServices().setOrderStatus(param, new Callback<SetOrdersModel>() {
            @Override
            public void success(SetOrdersModel getValues, Response response) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (getValues.getSuccess()) {
                    getCustomerDetail();
                } else {
                    Toast.makeText(CustomerDetailActivity.this, getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(CustomerDetailActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
