package com.signity.shopkeeperapp.orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderPrintKOTActivity extends AppCompatActivity {
    public static final String ORDER_ID = "ORDER_ID";
    private static final int GALLERY_PERMISSION = 103;
    private TextView textViewAddress;
    private RecyclerView recyclerView1;
    private OrderPrintKOTAdapter orderPrintAdapter;
    private OrdersListModel ordersListModel;
    private Toolbar toolbar;
    private String orderId;
    private TextView textViewCustomerName;
    private TextView textViewCustomerNumber;
    private LinearLayout linearLayoutMain;
    private TextView textViewStoreName, textViewInvoice, textViewOrderDate, textViewOderType, textViewPaymentMode, textViewOrderNote, textViewWaiterDetails;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OrderPrintKOTActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, OrderPrintKOTActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_print_k_o_t);
        getExtra();
        initView();
        setUpToolbar();
        setUpAdapter();
        getOrderDetail();
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString(ORDER_ID);
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
                        if (!getValues.getData().getOrders().isEmpty()) {
                            ordersListModel = getValues.getData().getOrders().get(0);
                            setOrderDetails();
                            linearLayoutMain.setVisibility(View.VISIBLE);
                            orderPrintAdapter.setProductData(ordersListModel.getItems());
                        }
                    }
                } else {
                    Toast.makeText(OrderPrintKOTActivity.this, getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(OrderPrintKOTActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
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
                AnimUtil.slideFromLeftAnim(OrderPrintKOTActivity.this);
            }
        });
    }

    private void setUpAdapter() {
        orderPrintAdapter = new OrderPrintKOTAdapter(this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(orderPrintAdapter);
    }

    public void setOrderDetails() {

        textViewStoreName.setText(AppPreference.getInstance().getStoreName());

        if (!TextUtils.isEmpty(ordersListModel.getOrderId())) {
            textViewInvoice.setVisibility(View.VISIBLE);
            textViewInvoice.setText(String.format("Invoice No: %s", ordersListModel.getOrderId()));
        } else {
            textViewInvoice.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(ordersListModel.getCustomerName())) {
            textViewCustomerName.setVisibility(View.VISIBLE);
            textViewCustomerName.setText(String.format("Customer Name: %s", Util.toTitleCase(ordersListModel.getCustomerName())));
        } else {
            textViewCustomerName.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(ordersListModel.getPhone())) {
            textViewCustomerNumber.setVisibility(View.VISIBLE);
            textViewCustomerNumber.setText(String.format("Customer Number: %s", ordersListModel.getPhone()));
        } else {
            textViewCustomerNumber.setVisibility(View.GONE);
        }

        textViewOrderDate.setText(String.format("Order Date: %s", ordersListModel.getTime()));
        textViewOderType.setText(String.format("Order Type: %s", ordersListModel.getOrderFacility()));
        textViewPaymentMode.setText(String.format("Payment Mode: %s", ordersListModel.getPaymentMethod()));

        if (!TextUtils.isEmpty(ordersListModel.getNote())) {
            textViewOrderNote.setVisibility(View.VISIBLE);
            textViewOrderNote.setText(String.format("Order Note: %s", ordersListModel.getNote()));
        } else {
            textViewOrderNote.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(ordersListModel.getAddress())) {
            // FIXME: As mentioned by sachin sir
            textViewAddress.setVisibility(View.GONE);
            textViewAddress.setText(String.format("Address: %s", ordersListModel.getAddress()));
        } else {
            textViewAddress.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(ordersListModel.getServedByWaiter())) {
            textViewWaiterDetails.setVisibility(View.VISIBLE);
            textViewWaiterDetails.setText(String.format("Served by: %s%s%s", ordersListModel.getServedByWaiter(),
                    TextUtils.isEmpty(ordersListModel.getTableNumber()) ? "" : "\nTable No: " + ordersListModel.getTableNumber(),
                    TextUtils.isEmpty(ordersListModel.getTableNumber()) ? "" :  "\nNumber of People: " + ordersListModel.getNumberOfPeople()));
        } else {
            textViewWaiterDetails.setVisibility(View.GONE);
        }

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView1 = findViewById(R.id.recyclerView);
        linearLayoutMain = findViewById(R.id.ll_main);
        textViewInvoice = findViewById(R.id.tv_invoice);
        textViewStoreName = findViewById(R.id.tv_store_name);
        textViewCustomerName = findViewById(R.id.tv_customer_name);
        textViewCustomerNumber = findViewById(R.id.tv_customer_number);
        textViewAddress = findViewById(R.id.tv_address);
        textViewOrderDate = findViewById(R.id.tv_order_date);
        textViewOderType = findViewById(R.id.tv_order_type);
        textViewPaymentMode = findViewById(R.id.tv_payment_mode);
        textViewOrderNote = findViewById(R.id.tv_order_note);
        textViewWaiterDetails = findViewById(R.id.tv_waiter_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_print_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_print) {
            if (linearLayoutMain.getVisibility() == View.VISIBLE) {
                printPDF();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        printManager.print("print_any_view_job_name", new ViewPrintAdapter(this,
                linearLayoutMain), null);
    }

}
