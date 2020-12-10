package com.signity.shopkeeperapp.market;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.classes.FacebookManager;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.GetProductResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductShareActivity extends BaseActivity implements ProductsShareAdapter.ProductAdapterListener, FacebookPagesDialog.PageCallback {

    private Toolbar toolbar;
    private ProductsShareAdapter productsAdapter;
    private RecyclerView recyclerViewProduct;
    private LinearLayoutManager layoutManager;
    private int pageSize = 10, currentPageNumber = 1, start, totalOrders;
    private boolean isLoading;
    private FacebookManager facebookManager;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    if (start < totalOrders) {
                        getAllOrdersMethod();
                    }
                }
            }
        }
    };
    private int productPosition;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProductShareActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ProductShareActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_share);
        initViews();
        setUpToolbar();
        setUpAdapter();
        getAllOrdersMethod();
        facebookManager = new FacebookManager(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (facebookManager.getCallbackManager() != null) {
            facebookManager.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initViews() {
        recyclerViewProduct = findViewById(R.id.rv_products);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(this);
        recyclerViewProduct.setLayoutManager(layoutManager);
        productsAdapter = new ProductsShareAdapter(this);
        productsAdapter.setListener(this);
        recyclerViewProduct.setAdapter(productsAdapter);
        recyclerViewProduct.addOnScrollListener(recyclerViewOnScrollListener);
    }

    public void getAllOrdersMethod() {

        if (!Util.checkIntenetConnection(this)) {
            productsAdapter.setShowLoading(false);
            DialogUtils.showAlertDialog(this, "Internet", "Please check your Internet Connection.");
            return;
        }

        getProductApi();
    }

    public void getProductApi() {
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);

        isLoading = true;
        NetworkAdaper.getNetworkServices().getAllProducts(param, new Callback<GetProductResponse>() {
            @Override
            public void success(GetProductResponse getProductResponse, Response response) {
                if (isDestroyed()) {
                    return;
                }
                isLoading = false;
                if (getProductResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalOrders = getProductResponse.getTotal();
                    if (getProductResponse.getData() != null) {
                        productsAdapter.addData(getProductResponse.getData(), totalOrders);
                    } else {
                        Toast.makeText(ProductShareActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                        productsAdapter.setShowLoading(false);
                    }

                } else {
                    Toast.makeText(ProductShareActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                    productsAdapter.setShowLoading(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                isLoading = false;
                Toast.makeText(ProductShareActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                productsAdapter.setShowLoading(false);
            }
        });
    }

    private void shareIntent(String extra, String shareTitle, Uri uri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, extra);
        if (uri != null) {
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        sendIntent.setType("*/*");
        sendIntent.setPackage("com.whatsapp");

        Intent shareIntent = Intent.createChooser(sendIntent, shareTitle);
        startActivity(shareIntent);
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

    private void shareIntent(String extra, String shareTitle) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, extra);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, shareTitle);
        startActivity(shareIntent);
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
                AnimUtil.slideFromLeftAnim(ProductShareActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public void onClickDeleteProduct(String id, int position) {

    }

    @Override
    public void shareOnWhatsapp(int position) {

        if (!whatsappInstalledOrNot("com.whatsapp")) {
            Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
            return;
        }

        final GetProductData productData = productsAdapter.getmData().get(position);

        final String storeUrl = AppPreference.getInstance().getStoreUrl();
        final String mobile = AppPreference.getInstance().getUserMobile();

        if (TextUtils.isEmpty(productData.getImage300200())) {
            String price = "";
            if (productData.getVariants() != null && productData.getVariants().size() > 0) {
                price = productData.getVariants().get(0).getPrice();
            }

            if (!TextUtils.isEmpty(price)) {
                price = Util.getPriceWithCurrency(Double.parseDouble(price), AppPreference.getInstance().getCurrency());
            }

            String message = String.format("Item Name: %s\nPrice:%s\n", productData.getTitle(), price);
            String message1 = String.format("Place your order here %s/product/%s. ", storeUrl, productData.getId());
            String message2 = String.format("Feel free to call us on %s if you need any help with ordering online. Thank you.", mobile);

            StringBuilder builder = new StringBuilder();
            builder.append(message);
            builder.append(message1);
            builder.append(message2);

            shareIntent(builder.toString(), "Share Product", null);
            return;
        }

        Picasso.with(this)
                .load(productData.getImage300200())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                File fileProduct = new File(getExternalFilesDir("ValueAppz"), "product_share.png");
                                try {
                                    fileProduct.createNewFile();
                                    FileOutputStream ostream = new FileOutputStream(fileProduct);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, ostream);
                                    ostream.flush();
                                    ostream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Uri uri = FileProvider.getUriForFile(ProductShareActivity.this, getPackageName() + ".provider", fileProduct);
                                String price = "";
                                if (productData.getVariants() != null && productData.getVariants().size() > 0) {
                                    price = productData.getVariants().get(0).getPrice();
                                }
                                if (!TextUtils.isEmpty(price)) {
                                    price = Util.getPriceWithCurrency(Double.parseDouble(price), AppPreference.getInstance().getCurrency());
                                }
                                String message = String.format("Item Name: %s\nPrice:%s\n", productData.getTitle(), price);
                                String message1 = String.format("Place your order here %s/product/%s. ", storeUrl, productData.getId());
                                String message2 = String.format("Feel free to call us on %s if you need any help with ordering online. Thank you.", mobile);

                                StringBuilder builder = new StringBuilder();
                                builder.append(message);
                                builder.append(message1);
                                builder.append(message2);

                                shareIntent(builder.toString(), "Share Product", uri);

                            }
                        }).start();

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    private void openPagesDialog() {
        if (getSupportFragmentManager().findFragmentByTag(FacebookPagesDialog.TAG) == null) {
            FacebookPagesDialog facebookDialog = FacebookPagesDialog.getInstance(null);
            facebookDialog.setListener(this);
            facebookDialog.show(getSupportFragmentManager(), FacebookPagesDialog.TAG);
        }
    }

    @Override
    public void shareOnFacebook(int position) {
        productPosition = position;
        if (!facebookManager.isFacebookLoggedIn()) {
            loginFacebook();
            return;
        }

        final GetProductData productData = productsAdapter.getmData().get(position);

        final String storeUrl = AppPreference.getInstance().getStoreUrl();
        final String mobile = AppPreference.getInstance().getUserMobile();

        if (!TextUtils.isEmpty(productData.getImage())) {
            String price = "";
            if (productData.getVariants() != null && productData.getVariants().size() > 0) {
                price = productData.getVariants().get(0).getPrice();
            }

            if (!TextUtils.isEmpty(price)) {
                price = Util.getPriceWithCurrency(Double.parseDouble(price), AppPreference.getInstance().getCurrency());
            }

            String message = String.format("Item Name: %s\nPrice:%s\n", productData.getTitle(), price);
            String message1 = String.format("Place your order here %s/product/%s. ", storeUrl, productData.getId());
            String message2 = String.format("Feel free to call us on %s if you need any help with ordering online. Thank you.", mobile);

            StringBuilder builder = new StringBuilder();
            builder.append(message);
            builder.append(message1);
            builder.append(message2);

            Bundle bundle = new Bundle();
            bundle.putString("url", productData.getImage());
            bundle.putBoolean("published", true);
            bundle.putString("message", builder.toString());
            bundle.putString("access_token", AppPreference.getInstance().getFacebookPageAccessToken());
            ProgressDialogUtil.showProgressDialog(this);
            facebookManager.postImageWithUrl(bundle, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (response.getError() == null) {
                        Toast.makeText(ProductShareActivity.this, "Content published on facebook", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loginFacebook() {
        facebookManager.performFacebookLogin(new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (isDestroyed()) {
                    return;
                }
                openPagesDialog();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ProductShareActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickProduct(String productId) {

    }

    @Override
    public void onPageSelected() {
        shareOnFacebook(productPosition);
    }

    @Override
    public void onNoPageFound() {
        Toast.makeText(this, "Facebook Page now available", Toast.LENGTH_SHORT).show();
    }
}
