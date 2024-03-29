package com.signity.shopkeeperapp.market;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.book.BookOrderActivity;
import com.signity.shopkeeperapp.book.OrderCart;
import com.signity.shopkeeperapp.book.OrderCartListener;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.GetProductResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductGalleryFragment extends Fragment implements OrderCartListener, ProductGalleryAdapter.OnProductSelection {

    public static final String TAG = "ProductGalleryFragment";
    private RecyclerView recyclerView;
    private ProductGalleryAdapter galleryAdapter;
    private int pageSize = 20, currentPageNumber = 1, start, totalOrders;
    private boolean isLoading;
    private GridLayoutManager layoutManager;
    private BookOrderActivity bookOrderActivity;
    private ContentLoadingProgressBar progressBar;
    private String keyword = "";
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
                        getBestSelling();
                    }
                }
            }
        }
    };

    public static ProductGalleryFragment getInstance(Bundle bundle) {
        ProductGalleryFragment fragment = new ProductGalleryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
        getBestSelling();
    }

    private void getBestSelling() {

        if (!Util.checkIntenetConnection(getContext())) {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);
        param.put("keyword", keyword);

        isLoading = true;
        NetworkAdaper.getNetworkServices().getAllProducts(param, new Callback<GetProductResponse>() {
            @Override
            public void success(GetProductResponse getProductResponse, Response response) {

                if (!isAdded()) {
                    return;
                }
                isLoading = false;
                progressBar.hide();
                if (getProductResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalOrders = getProductResponse.getTotal();
                    if (getProductResponse.getData() != null) {

                        List<GetProductData> selectedProductList = new ArrayList<>();
                        if (!OrderCart.isCartEmpty()) {
                            selectedProductList.addAll(OrderCart.getOrderCartMap().values());
                        }

                        List<GetProductData> data = getProductResponse.getData();
                        for (GetProductData getProductData : data) {
                            for (GetProductData selectedData : selectedProductList) {
                                if (selectedData.getId().equals(getProductData.getId())) {
                                    getProductData.setCount(selectedData.getCount());
                                    getProductData.setSelected(selectedData.isSelected());
                                    getProductData.setSelectedVariantIndex(selectedData.getSelectedVariantIndex());
                                    break;
                                }
                            }
                        }

                        galleryAdapter.addProductData(data);
                    } else {
                        Toast.makeText(getActivity(), "Data not Found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Data not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                progressBar.hide();
                isLoading = false;
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpAdapter() {
        layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        galleryAdapter = new ProductGalleryAdapter(getContext(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_best_seller);
        progressBar = view.findViewById(R.id.best_progress);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OrderCart.isCartEmpty()) {
            onCloseSearch();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_best_seller, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BookOrderActivity) {
            bookOrderActivity = (BookOrderActivity) context;
        }
    }

    @Override
    public void onAddProduct(GetProductData getProductData) {
        bookOrderActivity.addOrders(getProductData);
    }

    @Override
    public void onRemoveProduct(GetProductData getProductData) {
        bookOrderActivity.removeOrders(getProductData);
    }

    public void filterProduct(String query) {
        keyword = query;
        start = 0;
        currentPageNumber = 1;
        if (galleryAdapter != null) {
            if (progressBar != null) {
                progressBar.show();
            }
            galleryAdapter.clearData();
            getBestSelling();
        }
    }

    public void onCloseSearch() {
        keyword = "";
        start = 0;
        currentPageNumber = 1;
        if (galleryAdapter != null) {
            if (progressBar != null) {
                progressBar.show();
            }
            galleryAdapter.clearData();
            getBestSelling();
        }
    }

    @Override
    public void onProductSelected(GetProductData productData) {

        if (TextUtils.isEmpty(productData.getImage())) {
            Toast.makeText(getContext(), "Image not available", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialogUtil.showProgressDialog(getContext());
        Picasso.with(getContext())
                .load(productData.getImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (getContext() == null) {
                            return;
                        }
                        ProgressDialogUtil.hideProgressDialog();

                        File fileProduct = new File(getContext().getExternalFilesDir("ValueAppz"), "product_share.png");
                        try {
                            fileProduct.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(fileProduct);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", fileProduct);
                        Intent intent = new Intent();
                        intent.setData(uri);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        if (getContext() == null) {
                            return;
                        }
                        ProgressDialogUtil.hideProgressDialog();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }
}
