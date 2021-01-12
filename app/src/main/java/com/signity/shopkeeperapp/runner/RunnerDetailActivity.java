package com.signity.shopkeeperapp.runner;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.dashboard.orders.HomeOrdersAdapter;
import com.signity.shopkeeperapp.dashboard.orders.OrderDetailActivity;
import com.signity.shopkeeperapp.dashboard.orders.RejectOrderDialog;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.runner.CommonResponse;
import com.signity.shopkeeperapp.model.runner.DataResponse;
import com.signity.shopkeeperapp.model.runner.RunnerDetailResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.notifications.NotificationDialog;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

public class RunnerDetailActivity extends BaseActivity implements HomeOrdersAdapter.OrdersListener {

    public static final String RUNNER_ID = "RUNNER_ID";
    private static final String TAG = "RunnerDetailActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private HomeOrdersAdapter ordersAdapter;
    private String runnerId;
    private LinearLayout linearLayoutMain;
    private TextView textViewRunnerName, textViewTotalOrders, textViewRunnerNumber, textViewRunnerEmail, textViewActiveCount, textViewRunnerCity;
    private DataResponse runnerData;
    private LinearLayout linearLayoutArea, linearLayoutEmail;
    private ImageView imageViewWhatsapp, imageViewPhoneCall, imageViewMessage;
    private String filter = "";
    private ChipGroup chipGroup;
    private Chip chipAll, chipPending, chipAccepted, chipRejected;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RunnerDetailActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, RunnerDetailActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner_detail);
        getExtra();
        initViews();
        setUpToolbar();
        setUpAdapter();
        ProgressDialogUtil.showProgressDialog(this);
        getRunnerDetail();
    }

    public void getRunnerDetail() {
        Map<String, Object> param = new HashMap<>();
        param.put("runner_id", runnerId);
        param.put("runner_delivery_filter", filter);

        NetworkAdaper.getNetworkServices().getRunnerById(param, new Callback<RunnerDetailResponse>() {
            @Override
            public void success(RunnerDetailResponse customerDataResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (customerDataResponse.isSuccess()) {
                    if (customerDataResponse.getData() != null && !customerDataResponse.getData().isEmpty()) {
                        runnerData = customerDataResponse.getData().get(0);
                        populateData();
                    }
                } else {
                    Toast.makeText(RunnerDetailActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                    Toast.makeText(RunnerDetailActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateData() {

        linearLayoutMain.setVisibility(View.VISIBLE);

        textViewRunnerName.setText(runnerData.getFullName());
        textViewRunnerNumber.setText(runnerData.getPhone());
        textViewRunnerEmail.setText(runnerData.getEmail());

        if (runnerData.getArea() != null && !runnerData.getArea().isEmpty()) {
            textViewRunnerCity.setText(runnerData.getArea().get(0).getName());
        } else {
            linearLayoutArea.setVisibility(View.GONE);
        }

        linearLayoutEmail.setVisibility(TextUtils.isEmpty(runnerData.getEmail()) ? View.GONE : View.VISIBLE);
        textViewActiveCount.setText(String.valueOf(runnerData.getActiveOrder()));

        if (runnerData.getOrders() != null && !runnerData.getOrders().isEmpty()) {
            Map<Integer, List<OrdersListModel>> model = new HashMap<>();
            model.put(1, runnerData.getOrders());
            ordersAdapter.setPageOrdersMap(model);
            textViewTotalOrders.setText(String.format(Locale.getDefault(), "Total Orders: %d", runnerData.getOrders().size()));
        } else {
            ordersAdapter.clearPageMap();
            ordersAdapter.setShowLoading(false);
            textViewTotalOrders.setVisibility(View.GONE);
        }

        chipAll.setText(String.format("%d | All", runnerData.getTotalOrders()));
        chipPending.setText(String.format("%d | Pending", runnerData.getPendingOrders()));
        chipAccepted.setText(String.format("%d | Accepted", runnerData.getActiveOrder()));
        chipRejected.setText(String.format("%d | Rejected", runnerData.getRejectedOrders()));
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            runnerId = bundle.getString(RUNNER_ID);
        }
    }

    private void setUpAdapter() {
        ordersAdapter = new HomeOrdersAdapter(this, false);
        ordersAdapter.setListener(this);
        recyclerView.setAdapter(ordersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 12)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_orders);
        linearLayoutMain = findViewById(R.id.ll_main);
        textViewRunnerName = findViewById(R.id.tv_runner_name);
        textViewRunnerNumber = findViewById(R.id.tv_runner_number);
        textViewRunnerEmail = findViewById(R.id.tv_runner_email);
        textViewRunnerCity = findViewById(R.id.tv_runner_city);
        textViewActiveCount = findViewById(R.id.tv_active_count);
        imageViewWhatsapp = findViewById(R.id.iv_whatsapp);
        imageViewPhoneCall = findViewById(R.id.iv_phone_call);
        imageViewMessage = findViewById(R.id.iv_message);
        linearLayoutArea = findViewById(R.id.ll_area);
        linearLayoutEmail = findViewById(R.id.ll_email);
        textViewTotalOrders = findViewById(R.id.tv_total_orders);

        findViewById(R.id.iv_more).setVisibility(View.GONE);
        findViewById(R.id.ll_switch).setVisibility(View.GONE);

        chipGroup = findViewById(R.id.chip_group);
        chipAll = findViewById(R.id.chip_all);
        chipAll.setChecked(true);
        chipPending = findViewById(R.id.chip_pending);
        chipAccepted = findViewById(R.id.chip_accepted);
        chipRejected = findViewById(R.id.chip_rejected);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.chip_all:
                        filter = "";
                        break;
                    case R.id.chip_pending:
                        filter = "0";
                        break;
                    case R.id.chip_accepted:
                        filter = "1";
                        break;
                    case R.id.chip_rejected:
                        filter = "2";
                        break;
                }
                ordersAdapter.clearPageMap();
                getRunnerDetail();
            }
        });

        imageViewWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (runnerData == null) {
                    return;
                }

                if (TextUtils.isEmpty(runnerData.getPhone())) {
                    Toast.makeText(RunnerDetailActivity.this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
                    return;
                }

                openWhatsapp(runnerData.getPhone());
            }
        });

        imageViewPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runnerData == null) {
                    return;
                }

                if (TextUtils.isEmpty(runnerData.getPhone())) {
                    DialogUtils.showAlertDialog(RunnerDetailActivity.this, Constant.APP_TITLE, "Sorry! phone number is not available.");
                    return;
                }

                callAlert(runnerData.getPhone());
            }
        });

        imageViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (runnerData == null) {
                    return;
                }

                if (TextUtils.isEmpty(runnerData.getPhone())) {
                    Toast.makeText(RunnerDetailActivity.this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri uri = Uri.parse(String.format("smsto:%s", runnerData.getPhone()));
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);
            }
        });
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
        getRunnerDetail();
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
                updateOrderStatus(HomeOrdersAdapter.OrderType.REJECTED, order.getOrderId(), pageNumber, message, order.getUserId());
            }
        });
        rejectOrderDialog.show(getSupportFragmentManager(), NotificationDialog.TAG);
    }

    @Override
    public void onAcceptOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ACCEPTED, order.getOrderId(), pageNumber, "", order.getUserId());
    }

    @Override
    public void onShipOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.SHIPPED, order.getOrderId(), pageNumber, "",order.getUserId());
    }

    @Override
    public void onReadyToBePicked(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.READY_TO_BE_PICKED, order.getOrderId(), pageNumber, "",order.getUserId());
    }

    @Override
    public void onDeliverOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.DELIVERED, order.getOrderId(), pageNumber, "",order.getUserId());
    }

    @Override
    public void onTheWayOrder(int position, int pageNumber) {
        OrdersListModel order = ordersAdapter.getOrdersListModels().get(position);
        updateOrderStatus(HomeOrdersAdapter.OrderType.ON_THE_WAY, order.getOrderId(), pageNumber, "",order.getUserId());
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
                    getRunnerDetail();
                }
                Toast.makeText(RunnerDetailActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateOrderStatus(HomeOrdersAdapter.OrderType orderStatus, String orderId, final int pageNumber, String message, String customerId) {
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
                    getRunnerDetail();
                } else {
                    Toast.makeText(RunnerDetailActivity.this, getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(RunnerDetailActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
