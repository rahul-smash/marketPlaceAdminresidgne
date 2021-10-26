package com.signity.shopkeeperapp.dashboard.orders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderPrintActivity extends BaseActivity {

    public static final String ORDER_ID = "ORDER_ID";
    private static final int GALLERY_PERMISSION = 103;
    private TextView textViewTotalPrice, textViewPayableAmount, textViewCartSavings, textViewAddress;
    private RecyclerView recyclerView1;
    private OrderPrintAdapter orderPrintAdapter;
    private OrdersListModel ordersListModel;
    private Toolbar toolbar;
    private TextView textViewDeliveryCharges;
    private TextView textViewCouponDiscount;
    private TextView textViewMrpDiscount;
    private String orderId;
    private TextView textViewCustomerName;
    private TextView textViewCustomerNumber;
    private TextView textViewOrderTax;
    private TextView textViewCouponCode;
    private LinearLayout linearLayoutDiscountCoupon;
    private LinearLayout linearLayoutMain;
    private TextView textViewStoreName, textViewInvoice, textViewOrderAmount, textViewOrderDate, textViewOderType, textViewPaymentMode, textViewOrderNote;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OrderPrintActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, OrderPrintActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_print_activity);
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
                    Toast.makeText(OrderPrintActivity.this, getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(OrderPrintActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
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
                AnimUtil.slideFromLeftAnim(OrderPrintActivity.this);
            }
        });
    }

    private void setUpAdapter() {
        orderPrintAdapter = new OrderPrintAdapter(this);
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
            textViewCustomerName.setText(String.format("Customer Name: %s", ordersListModel.getCustomerName()));
        } else {
            textViewCustomerName.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(ordersListModel.getPhone())) {
            textViewCustomerNumber.setVisibility(View.VISIBLE);
            textViewCustomerNumber.setText(String.format("Customer Number: %s", ordersListModel.getPhone()));
        } else {
            textViewCustomerNumber.setVisibility(View.GONE);
        }

        textViewOrderAmount.setText(String.format("Order Amount: %s", Util.getPriceWithCurrency(ordersListModel.getTotal(), AppPreference.getInstance().getCurrency())));
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
            textViewAddress.setVisibility(View.VISIBLE);
            textViewAddress.setText(String.format("Address: %s", ordersListModel.getAddress()));
        } else {
            textViewAddress.setVisibility(View.GONE);
        }

        double mrpDiscount = 0;
        try {
            for (ItemListModel response : ordersListModel.getItems()) {
                double mrp = response.getMrpPrice();
                double price = response.getPrice();
                int quantity = Integer.parseInt(response.getQuantity());
                mrpDiscount += (mrp - price) * quantity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double cartSaving = mrpDiscount + ordersListModel.getDiscount();

        if (ordersListModel.getDiscount() != 0) {
            linearLayoutDiscountCoupon.setVisibility(View.VISIBLE);
            textViewCouponCode.setText(String.format("Coupon Applied(%s)", ordersListModel.getCouponCode().toUpperCase()));
        }
        textViewTotalPrice.setText(Util.getPriceWithCurrency(ordersListModel.getCheckout(), AppPreference.getInstance().getCurrency()));
        textViewMrpDiscount.setText(Util.getPriceWithCurrency(mrpDiscount, AppPreference.getInstance().getCurrency()));
        textViewCouponDiscount.setText(Util.getPriceWithCurrency(ordersListModel.getDiscount(), AppPreference.getInstance().getCurrency()));
        textViewDeliveryCharges.setText(Util.getPriceWithCurrency(ordersListModel.getShippingCharges(), AppPreference.getInstance().getCurrency()));
        textViewPayableAmount.setText(Util.getPriceWithCurrency(ordersListModel.getTotal(), AppPreference.getInstance().getCurrency()));
        textViewCartSavings.setText(String.format("Cart Savings: %s", Util.getPriceWithCurrency(cartSaving, AppPreference.getInstance().getCurrency())));
        textViewOrderTax.setText(Util.getPriceWithCurrency(ordersListModel.getTax(), AppPreference.getInstance().getCurrency()));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView1 = findViewById(R.id.recyclerView);
        textViewTotalPrice = findViewById(R.id.tv_total_price);
        textViewPayableAmount = findViewById(R.id.tv_payable_amount);
        textViewCartSavings = findViewById(R.id.tv_cart_saving);
        textViewDeliveryCharges = findViewById(R.id.tv_delivery_amount);
        textViewCouponDiscount = findViewById(R.id.tv_coupon_discount);
        textViewMrpDiscount = findViewById(R.id.tv_mpr_discount);
        textViewCouponCode = findViewById(R.id.tv_coupon_code);
        linearLayoutDiscountCoupon = findViewById(R.id.ll_discount_coupon);
        textViewOrderTax = findViewById(R.id.tv_tax);
        linearLayoutMain = findViewById(R.id.ll_main);
        textViewInvoice = findViewById(R.id.tv_invoice);
        textViewStoreName = findViewById(R.id.tv_store_name);
        textViewCustomerName = findViewById(R.id.tv_customer_name);
        textViewCustomerNumber = findViewById(R.id.tv_customer_number);
        textViewAddress = findViewById(R.id.tv_address);
        textViewOrderAmount = findViewById(R.id.tv_order_amount);
        textViewOrderDate = findViewById(R.id.tv_order_date);
        textViewOderType = findViewById(R.id.tv_order_type);
        textViewPaymentMode = findViewById(R.id.tv_payment_mode);
        textViewOrderNote = findViewById(R.id.tv_order_note);
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
                checkPermission();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void generatePDF() {
        PdfGenerator.getBuilder()
                .setContext(this)
                .fromViewSource()
                .fromView(linearLayoutMain)
//                .setPageSize(PdfGenerator.PageSize.WRAP_CONTENT)
                .setFileName("order_receipt_pdf")
                .openPDFafterGeneration(false)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onStartPDFGeneration() {
                        Log.d("PDF", "onStartPDFGeneration: ");
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                        Log.d("PDF", "onFinishPDFGeneration: ");
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        createReceipt(response.getFile());
                    }
                });
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            generatePDF();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == GALLERY_PERMISSION) {
                generatePDF();
            }
        } else {
            Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
        }
    }

    public void createReceipt(final File file) {
        try {
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

            PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
                @Override
                public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
                    if (cancellationSignal.isCanceled()) {
                        callback.onLayoutCancelled();
                    } else {
                        String pdfFile = String.format("order_id_%s.pdf", orderId);
                        PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder(pdfFile);
                        builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT);
                        builder.setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN);
                        builder.build();

                        callback.onLayoutFinished(builder.build(), newAttributes.equals(oldAttributes));
                    }
                }

                @Override
                public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                    OutputStream outputStream = null;
                    InputStream inputStream = null;

                    try {
                        inputStream = new FileInputStream(file);
                        outputStream = new FileOutputStream(destination.getFileDescriptor());

                        byte[] bytes = new byte[16384];
                        int size;
                        while ((size = inputStream.read(bytes)) >= 0 && !cancellationSignal.isCanceled()) {
                            outputStream.write(bytes, 0, size);
                        }
                        if (cancellationSignal.isCanceled()) {
                            callback.onWriteCancelled();
                        } else {
                            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onWriteFailed(e.getMessage());
                    } finally {
                        try {
                            if (inputStream != null)
                                inputStream.close();
                            if (outputStream != null)
                                outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).build());

        } catch (Exception c) {
            c.printStackTrace();
        }

    }
}
