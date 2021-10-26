package com.signity.shopkeeperapp.orders;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.signity.shopkeeperapp.BuildConfig;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.book.PaymentModeAdapter;
import com.signity.shopkeeperapp.dashboard.orders.OrderPrintActivity;
import com.signity.shopkeeperapp.dashboard.orders.printSetting.PrintSettingResponse;
import com.signity.shopkeeperapp.model.ItemListModel;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderPrintV2Activity extends BaseActivity {
    public static final String ORDER_PRINT_URL = "https://admin.storeapp.com.au/orders/printAppOrder/";
    public static final String ORDER_ID = "ORDER_ID";
    private static final int GALLERY_PERMISSION = 103;
    String noMediaFolder = ".noMedia";
    private String orderId;
    private OrdersListModel ordersListModel;
    private PrintSettingResponse printSettingResponse;
    private Toolbar toolbar;
    private LinearLayout linearLayoutMain;
    private OrderPrintV2Adapter orderPrintAdapter;
    private RecyclerView recyclerView1;
    private LinearLayout linearLayoutDiscountCoupon;
    private TextView textViewTotalPrice, textViewPayableAmount,
            textViewCartSavings, textViewDeliveryCharges,
            textViewInvoice, textViewOrderDate,
            textViewCouponDiscount, textViewMrpDiscount,
            textViewCouponCode,
            textViewOrderTax,
            textViewFssi,
            textViewStoreContactNumber,
            textViewFooterContent, textViewStoreAddress,
            textViewWaiterDetails;
    private ImageView imageStoreLogo;
    private TextView textViewCustName;
    private TextView textViewPaymentType;
    private WebView mWebView;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OrderPrintActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, OrderPrintV2Activity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_print_v2);
        getExtra();
        initView();
        setUpToolbar();
        setUpAdapter();
//        new DownloadFile().execute();
//        doWebViewPrint();
        getPrintDetail();

//        https://s3.amazonaws.com/store-asset/1622698246-226.pdf?1622698247

//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/receipt");
//        File file = new File(Objects.requireNonNull(uri.getPath()));
//        createReceipt(file);
    }

    private void setUpAdapter() {
        orderPrintAdapter = new OrderPrintV2Adapter(this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(orderPrintAdapter);
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString(ORDER_ID);
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
                onBackPressed();
                AnimUtil.slideFromLeftAnim(OrderPrintV2Activity.this);
            }
        });
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
                            List<ItemListModel> items = ordersListModel.getItems();
                            List<ItemListModel> adpaterItem = new ArrayList<>();
                            for (ItemListModel itemListModel : items) {
                                if (!itemListModel.getStatus().equals("2")) {
                                    adpaterItem.add(itemListModel);
                                }
                            }

                            orderPrintAdapter.setProductData(adpaterItem);
                        }
                    }
                } else {
                    Toast.makeText(OrderPrintV2Activity.this, getValues.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(OrderPrintV2Activity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPrintDetail() {
        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().getPrintDetails(new Callback<PrintSettingResponse>() {
            @Override
            public void success(PrintSettingResponse getValues, Response response) {

                if (isDestroyed()) {
                    return;
                }
                printSettingResponse = getValues;
                if (printSettingResponse != null && printSettingResponse.getSuccess()) {
                    getOrderDetail();
                } else {
                    ProgressDialogUtil.hideProgressDialog();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(OrderPrintV2Activity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        linearLayoutMain = findViewById(R.id.ll_main);
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
        textViewInvoice = findViewById(R.id.tv_invoice);
//        textViewStoreName = findViewById(R.id.tv_store_name);
//        textViewCustomerName = findViewById(R.id.tv_customer_name);
//        textViewCustomerNumber = findViewById(R.id.tv_customer_number);
//        textViewAddress = findViewById(R.id.tv_address);
//        textViewOrderAmount = findViewById(R.id.tv_order_amount);
        textViewOrderDate = findViewById(R.id.tv_order_date);
        textViewFssi = findViewById(R.id.tv_fssi_no);
        textViewFooterContent = findViewById(R.id.tv_footer_content);
        textViewStoreContactNumber = findViewById(R.id.tv_store_contact_no);
        textViewStoreAddress = findViewById(R.id.tv_store_address);
        textViewCustName = findViewById(R.id.tv_customer_name);
        textViewPaymentType = findViewById(R.id.tv_payment_type);
        textViewFssi = findViewById(R.id.tv_fssi_no);
        imageStoreLogo = findViewById(R.id.iv_store_logo);
        textViewWaiterDetails = findViewById(R.id.tv_waiter_details);
//        textViewOderType = findViewById(R.id.tv_order_type);
//        textViewPaymentMode = findViewById(R.id.tv_payment_mode);
//        textViewOrderNote = findViewById(R.id.tv_order_note);
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
//                checkPermission();
//                doWebViewPrint();
                printPDF();
            }
        }

        if (item.getItemId() == R.id.action_print2) {
            if (linearLayoutMain.getVisibility() == View.VISIBLE) {
//                checkPermission();
//                doWebViewPrint();
                /*Bitmap bitmap = Bitmap.createBitmap(linearLayoutMain.getWidth(), linearLayoutMain.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                linearLayoutMain.draw(canvas);
                PrintHelper printHelper = new PrintHelper(this);
                printHelper.setScaleMode(PrintHelper.SCALE_MODE_FILL);
                printHelper.setOrientation(PrintHelper.ORIENTATION_PORTRAIT);
                printHelper.printBitmap("Test",bitmap);*/
                printPDF();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            generatePDF();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION);
        }
    }

    public void setOrderDetails() {

        if (printSettingResponse.getData().getStorePrintLogo() != null
                && !TextUtils.isEmpty(printSettingResponse.getData().getStorePrintLogo())) {
            imageStoreLogo.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(printSettingResponse.getData().getStorePrintLogo())
                    .into(imageStoreLogo);
        } else {
            imageStoreLogo.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(AppPreference.getInstance().getStoreAddress())) {
            textViewStoreAddress.setVisibility(View.VISIBLE);
            textViewStoreAddress.setText(AppPreference.getInstance().getStoreAddress());
        }

        if (!TextUtils.isEmpty(ordersListModel.getOrderId())) {
            textViewInvoice.setVisibility(View.VISIBLE);
            textViewInvoice.setText(String.format("Receipt No: %s", ordersListModel.getOrderId()));
        } else {
            textViewInvoice.setVisibility(View.GONE);
        }

        textViewOrderDate.setText(String.format("TAX INVOICE %s", ordersListModel.getTime()));
        if (AppPreference.getInstance().getStoreId().equals("7")) {
            textViewFooterContent.setText(String.format("Our Business Hours\n%s\nEmail: %s", "5:00 PM to 9:30 PM 7 days\n11:30 AM to 2:30 AM Wed to Sun", AppPreference.getInstance().getStoreEmail()));
        } else
            textViewFooterContent.setText(String.format("Our Business Hours\n%s %s %s\nEmail: %s", printSettingResponse.getData().getStoreTimeSetting().getOpenhoursFrom(), TextUtils.isEmpty(printSettingResponse.getData().getStoreTimeSetting().getOpenhoursFrom()) ? "" : "to", printSettingResponse.getData().getStoreTimeSetting().getOpenhoursTo(), AppPreference.getInstance().getStoreEmail()));


        if (!TextUtils.isEmpty(AppPreference.getInstance().getStoreMobile())) {
            textViewStoreContactNumber.setText(AppPreference.getInstance().getStoreMobile());
        } else {
            findViewById(R.id.layout_store_contact).setVisibility(View.GONE);
        }
        if (printSettingResponse.getData().getPrintOrderSetting() != null && printSettingResponse.getData().getPrintOrderSetting().getIsFssiOn().equalsIgnoreCase("1")) {
            textViewFssi.setText(printSettingResponse.getData().getPrintOrderSetting().getFssi());
        } else {
            findViewById(R.id.layout_fssi).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(ordersListModel.getCustomerName())) {
            textViewCustName.setVisibility(View.VISIBLE);
            textViewCustName.setText(String.format("Customer Name: %s", ordersListModel.getCustomerName()));
        } else {
            textViewCustName.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(ordersListModel.getPaymentMethod())) {
            textViewPaymentType.setVisibility(View.VISIBLE);
            textViewPaymentType.setText(ordersListModel.getPaymentMethod().contains("online") ? PaymentModeAdapter.PaymentModes.CARD.getTitle() : PaymentModeAdapter.PaymentModes.CASH.getTitle());
        } else {
            textViewPaymentType.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(ordersListModel.getServedByWaiter())) {
            textViewWaiterDetails.setVisibility(View.VISIBLE);
            textViewWaiterDetails.setText(String.format("Served by: %s%s", ordersListModel.getServedByWaiter(),
                    TextUtils.isEmpty(ordersListModel.getTableNumber()) ? "" : "\nTable No: " + ordersListModel.getTableNumber()));
        } else {
            textViewWaiterDetails.setVisibility(View.GONE);
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

    private void generatePDF() {
//        printPDF();

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
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_B7).build());

        } catch (Exception c) {
            c.printStackTrace();
        }

    }

    public void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        printManager.print("print_any_view_job_name", new ViewPrintAdapter(this,
                linearLayoutMain), null);
    }

    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(getClass().getSimpleName(), "page finished loading " + url);
                createWebPrintJob(view);
                mWebView = null;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        // Generate an HTML document on the fly:
//        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
//                "testing, testing...</p></body></html>";
        String url = String.format("%s/%s/%s", ORDER_PRINT_URL, AppPreference.getInstance().getStoreId(), orderId);
        Log.d("PRINT", "doWebViewPrint: " + url);
        webView.loadUrl(url);
//        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager =
                (PrintManager) getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

    }

    class DownloadFile extends AsyncTask<Void, Void, File> {

        protected File doInBackground(Void... arg) {
            boolean downloading = true;
            try {
                DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request mRqRequest = new DownloadManager.Request(
                        Uri.parse("https://s3.amazonaws.com/store-asset/1622698246-226.pdf?1622698247"));
                mRqRequest.setTitle("My File");
                mRqRequest.setDescription("Downloading");
                mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // path of external files
                File externalFilesDir = getExternalFilesDir("");
                if (externalFilesDir != null) {
//                    String streamFolderPath = externalFilesDir.getAbsolutePath();
//                    File streamFileDir = new File(streamFolderPath);
                    File newFile = new File(Environment.getExternalStorageDirectory(), "receipt_" + System.currentTimeMillis() + ".pdf");
                    Uri destinationUri = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) ?
                            FileProvider.getUriForFile(OrderPrintV2Activity.this, BuildConfig.APPLICATION_ID + ".provider", newFile) :
                            Uri.fromFile(newFile);
//                    Uri destinationUri = Uri.parse(streamFileDir + File.separator + "receipt_" + System.currentTimeMillis() + ".pdf");

                    mRqRequest.setDestinationUri(destinationUri);
                    mManager.enqueue(mRqRequest);
                    DownloadManager.Query query = null;
                    query = new DownloadManager.Query();
                    Cursor c = null;
                    if (query != null) {
                        query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
                    } else {
                        return null;
                    }

                    while (downloading) {
                        c = mManager.query(query);
                        if (c.moveToFirst()) {
                            Log.i("FLAG", "Downloading");
                            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                Log.i("FLAG", "done");
                                downloading = false;
                                break;
                            }
                            if (status == DownloadManager.STATUS_FAILED) {
                                Log.i("FLAG", "Fail");
                                downloading = false;
                                break;
                            }
                        }
                    }
                    return newFile;

                }


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(File downloaded) {
            super.onPostExecute(downloaded);
            Log.e(getClass().getSimpleName(), "onPostExecute: ");
            if (downloaded != null)
                createReceipt(downloaded);
        }
    }

//    public class ViewPrintAdapter extends PrintDocumentAdapter {
//
//        private PrintedPdfDocument mDocument;
//        private Context mContext;
//        private View mView;
//
//        public ViewPrintAdapter(Context context, View view) {
//            mContext = context;
//            mView = view;
//        }
//
//        @Override
//        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
//                             CancellationSignal cancellationSignal,
//                             LayoutResultCallback callback, Bundle extras) {
//
//            mDocument = new PrintedPdfDocument(mContext, newAttributes);
//
//            if (cancellationSignal.isCanceled()) {
//                callback.onLayoutCancelled();
//                return;
//            }
//
//            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
//                    .Builder("print_output.pdf")
//                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN);
//
//            PrintDocumentInfo info = builder.build();
//            callback.onLayoutFinished(info, true);
//        }
//
//        @Override
//        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
//                            CancellationSignal cancellationSignal,
//                            WriteResultCallback callback) {
//
//            // Start the page
//            int range = 0;
//            float newPageHeight = 0;
//            for (int j = 0; j < 100; j++) {
//                PdfDocument.Page page = mDocument.startPage(j);
//
//                // Create a bitmap and put it a canvas for the view to draw to. Make it the size of the view
//                Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
//                        Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//                mView.draw(canvas);
//
//                RectF contentRect = new RectF(page.getInfo().getContentRect());
//                float pageWidth = contentRect.width();
//                float pageHeight = contentRect.height();
//
//
//                float scale = contentRect.width() / bitmap.getWidth();
//                scale = Math.max(scale, contentRect.height() / bitmap.getHeight());
//                Matrix matrix = new Matrix();
//                matrix.postScale(scale, scale);
//
//                float scaleHeight = bitmap.getHeight() * scale;
//
//                Log.e("TAG", "onWrite: " + newPageHeight);
//                Log.e("TAG", "onWrite: " + scaleHeight);
//
//
//                matrix.postTranslate(0, -newPageHeight);
//                page.getCanvas().drawBitmap(bitmap, matrix, null);
//                newPageHeight += pageHeight;
//                range++;
//
///*
//            // how can we fit the Rect src onto this page while maintaining aspect ratio?
//            float scale = Math.min(pageWidth / src.width(), pageHeight / src.height());
//            float left = pageWidth / 2 - src.width() * scale / 2;
//            float top = pageHeight / 2 - src.height() * scale / 2;
//            float right = pageWidth / 2 + src.width() * scale / 2;
//            float bottom = pageHeight / 2 + src.height() * scale / 2;
//            RectF dst = new RectF(left, top, right, bottom);
//
//            pageCanvas.drawBitmap(bitmap, src, dst, null);*/
//                mDocument.finishPage(page);
//
//                if (newPageHeight >= scaleHeight) {
//                    break;
//                }
//            }
//
//            try {
//                mDocument.writeTo(new FileOutputStream(
//                        destination.getFileDescriptor()));
//            } catch (IOException e) {
//                callback.onWriteFailed(e.toString());
//                return;
//            } finally {
//                mDocument.close();
//                mDocument = null;
//            }
//            callback.onWriteFinished(new PageRange[]{new PageRange(0, range)});
//        }
//    }

    public static Matrix getMatrix(int imageWidth, int imageHeight, RectF content) {
        Matrix matrix = new Matrix();

        // Compute and apply scale to fill the page.
        float scale = content.width() / imageWidth;
        Log.e("TAG", "getMatrix: scale" + scale);
        scale = Math.max(scale, content.height() / imageHeight);
        Log.e("TAG", "getMatrix: scale" + scale);
        matrix.postScale(scale, scale);

        // Center the content.
//        final float translateX = (content.width()
//                - imageWidth * scale) / 2;
//        final float translateY = (content.height()
//                - imageHeight * scale) / 2;
//        matrix.postTranslate(translateX, translateY);
        return matrix;
    }
}