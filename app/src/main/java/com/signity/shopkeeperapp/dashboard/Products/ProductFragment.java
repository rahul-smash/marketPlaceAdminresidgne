package com.signity.shopkeeperapp.dashboard.Products;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.DeleteCategory.DeleteCategories;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.GetProductResponse;
import com.signity.shopkeeperapp.model.productStatus.ProductStatus;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.products.AddProductActivity;
import com.signity.shopkeeperapp.products.CategoryDialog;
import com.signity.shopkeeperapp.products.SubCategoryDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductFragment extends Fragment implements View.OnClickListener, ProductsAdapter.ProductAdapterListener {
    public static final String TAG = "ProductFragment";
    private View hiddenView;
    private ProductsAdapter productsAdapter;
    private LinearLayout linearLayoutAddProduct;
    private List<GetProductData> productData = new ArrayList<>();
    private RecyclerView recyclerViewProduct;
    private LinearLayoutManager layoutManager;
    private int pageSize = 10, currentPageNumber = 1, start, totalOrders;
    private List<GetCategoryData> categoryDataList = new ArrayList<>();
    private String selectedCategoryId = "0";
    private String selectedSubCategoryId = "0";
    private TextInputEditText txtSelectCategory, txtSubCategory;
    private boolean isFiltering;
    private boolean isLoading;
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
                    if (start < totalOrders && !isFiltering) {
                        getAllOrdersMethod();
                    }
                }
            }
        }
    };
    private boolean needRefesh;

    public static ProductFragment getInstance(Bundle bundle) {
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_products, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setQueryHint("Search Product");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText.trim());
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productData = productsAdapter.getmData();
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isFiltering = hasFocus;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                productsAdapter.setmData(productData, totalOrders, true);
                return false;
            }
        });
    }

    private void filterProducts(String trim) {

        List<GetProductData> newFilterProducts = new ArrayList<>();

        if (productData.size() == 0) {
            return;
        }

        for (GetProductData data : productData) {
            if (data.getTitle().toLowerCase().startsWith(trim.toLowerCase())) {
                newFilterProducts.add(data);
            }
        }

        productsAdapter.setmData(newFilterProducts, newFilterProducts.size(), false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showOverViewPopMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
        getAllOrdersMethod();
        getCategoriesApi();
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProduct.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(getContext());
        productsAdapter.setListener(this);
        recyclerViewProduct.setAdapter(productsAdapter);
        recyclerViewProduct.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private void initView(View rootView) {
        hiddenView = rootView.findViewById(R.id.view);
        linearLayoutAddProduct = rootView.findViewById(R.id.ll_add_product);
        recyclerViewProduct = rootView.findViewById(R.id.recyclerViewProduct);
        linearLayoutAddProduct.setOnClickListener(this);
    }

    public boolean isLoading() {
        return ProgressDialogUtil.isProgressLoading();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initView(view);
    }

    public void getAllOrdersMethod() {

        if (!Util.checkIntenetConnection(getContext())) {
            productsAdapter.setShowLoading(false);
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            return;
        }

        getProductApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefesh) {
            currentPageNumber = 1;
            start = 0;
            productsAdapter.clearData();
            getAllOrdersMethod();
            needRefesh = false;
        }
    }

    public void getProductApi() {
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);
        param.put("cat_id", selectedCategoryId);
        param.put("sub_cat_ids", selectedSubCategoryId);

        isLoading = true;
        NetworkAdaper.getNetworkServices().getAllProducts(param, new Callback<GetProductResponse>() {
            @Override
            public void success(GetProductResponse getProductResponse, Response response) {
                if (!isAdded()) {
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
                isLoading = false;
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
                    productsAdapter.setShowLoading(false);
                }
            }
        });
    }

    public void getProductApiUsingFilter() {
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);
        param.put("cat_id", selectedCategoryId);
        param.put("sub_cat_ids", selectedSubCategoryId);

        isLoading = true;
        NetworkAdaper.getNetworkServices().getAllProducts(param, new Callback<GetProductResponse>() {
            @Override
            public void success(GetProductResponse getProductResponse, Response response) {
                if (!isAdded()) {
                    return;
                }
                isLoading = false;
                if (getProductResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalOrders = getProductResponse.getTotal();
                    if (getProductResponse.getData() != null) {
                        productsAdapter.setmData(getProductResponse.getData(), totalOrders, true);
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
                isLoading = false;
                if (getContext() != null)
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == linearLayoutAddProduct) {
            startActivity(AddProductActivity.getStartIntent(getContext()));
            AnimUtil.slideFromRightAnim(getActivity());
        }
    }

    private void showOverViewPopMenu() {

        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.popup_product_filters, null, false);
        final PopupWindow popupWindowOverView = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowOverView.setOutsideTouchable(true);
        popupWindowOverView.setBackgroundDrawable(new ColorDrawable());
        popupWindowOverView.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindowOverView.dismiss();
                    return true;
                }
                return false;
            }

        });
        popupWindowOverView.showAsDropDown(hiddenView, 0, 0);
        txtSelectCategory = layout.findViewById(R.id.edt_category);
        Button btnApply = layout.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean shouldApply = true;
                if (selectedCategoryId.equals("0") || selectedSubCategoryId.equals("0")) {
                    shouldApply = false;
                }
                if (shouldApply) {
                    currentPageNumber = 1;
                    start = 0;
                    productsAdapter.clearData();
                    getProductApiUsingFilter();
                    popupWindowOverView.dismiss();
                }
            }
        });
        Button btnCancel = layout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean shouldReload = true;
                if (selectedCategoryId.equals("0") || selectedSubCategoryId.equals("0")) {
                    shouldReload = false;
                }

                if (shouldReload) {
                    currentPageNumber = 1;
                    selectedCategoryId = "0";
                    selectedSubCategoryId = "0";
                    start = 0;
                    productsAdapter.clearData();
                    getAllOrdersMethod();
                }
                popupWindowOverView.dismiss();
            }
        });
        txtSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(CategoryDialog.CATEGORY_DATA, (ArrayList<? extends Parcelable>) categoryDataList);
                CategoryDialog categoryDialog = CategoryDialog.getInstance(bundle);
                categoryDialog.setListenerCategory(new CategoryDialog.CategoryListener() {
                    @Override
                    public void onSelectCategory(String categoryId, String categoryName) {
                        selectedCategoryId = categoryId;
                        txtSelectCategory.setText(categoryName);
                    }
                });
                categoryDialog.show(getChildFragmentManager(), CategoryDialog.TAG);
            }
        });
        txtSubCategory = layout.findViewById(R.id.edt_sub_category);
        txtSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(selectedCategoryId)) {
                    return;
                }

                List<SubCategory> subCategories = new ArrayList<>();
                for (GetCategoryData categoryData : categoryDataList) {
                    if (categoryData.getId().equals(selectedCategoryId)) {
                        subCategories.addAll(categoryData.getSubCategory());
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SubCategoryDialog.SUBCATEGORY_DATA, (ArrayList<? extends Parcelable>) subCategories);
                SubCategoryDialog subCategoryDialog = SubCategoryDialog.getInstance(bundle);
                subCategoryDialog.setListenerCategory(new SubCategoryDialog.SubCategoryListener() {
                    @Override
                    public void onSelectSubCategory(String subCategoryId, String subCategoryName) {
                        selectedSubCategoryId = subCategoryId;
                        txtSubCategory.setText(subCategoryName);
                    }
                });
                subCategoryDialog.show(getChildFragmentManager(), SubCategoryDialog.TAG);
            }
        });

        for (GetCategoryData categoryData : categoryDataList) {
            if (categoryData.getId().equals(selectedCategoryId)) {
                txtSelectCategory.setText(categoryData.getTitle());
                for (SubCategory subCategory : categoryData.getSubCategory()) {
                    if (subCategory.getId().equals(selectedSubCategoryId)) {
                        txtSubCategory.setText(subCategory.getTitle());
                        break;
                    }
                }
                break;
            }
        }
    }

    public void deleteCategory(String productId, final int position) {
        ProgressDialogUtil.showProgressDialog(getContext());
        Map<String, Object> param = new HashMap<>();
        param.put("product_id", productId);

        NetworkAdaper.getNetworkServices().delProduct(param, new Callback<DeleteCategories>() {
            @Override
            public void success(DeleteCategories deleteCategories, Response response) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (deleteCategories.getSuccess()) {
                    productsAdapter.removeItem(position);
                    if (start < totalOrders) {
                        getAllOrdersMethod();
                    }
                } else {
                    Toast.makeText(getContext(), deleteCategories.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (getContext() != null)
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setProductStatus(final String subProductId, String status) {
        ProgressDialogUtil.showProgressDialog(getContext());
        Map<String, Object> param = new HashMap<>();
        param.put("product_id", subProductId);
        param.put("product_status", status);

        NetworkAdaper.getNetworkServices().setProductStatus(param, new Callback<ProductStatus>() {
            @Override
            public void success(ProductStatus productStatus, Response response) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (productStatus.getSuccess()) {
                    productsAdapter.updateProductStatus(subProductId);
                } else {
                    Toast.makeText(getContext(), productStatus.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (getContext() != null)
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCategoriesApi() {
        Map<String, Object> param = new HashMap<>();
        param.put("page", 1);
        param.put("pagelength", 1000);

        NetworkAdaper.getNetworkServices().getCategories(param, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {
                if (!isAdded()) {
                    return;
                }
                if (getCategoryResponse.getSuccess()) {
                    categoryDataList = getCategoryResponse.getData();
                } else {
                    Toast.makeText(getActivity(), "Data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                if (getContext() != null)
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlertDialog(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Please visit Admin Portal to add new product.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getString(R.string.str_lbl_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(AddProductActivity.getStartIntent(getContext()));
                        AnimUtil.slideFromRightAnim(getActivity());
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void shareIntent(String extra, String shareTitle, Uri uri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, extra);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setType("*/*");

        Intent shareIntent = Intent.createChooser(sendIntent, shareTitle);
        startActivity(shareIntent);
    }

    private void shareIntent(String extra, String shareTitle) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, extra);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, shareTitle);
        startActivity(shareIntent);
    }

    @Override
    public void onClickDeleteProduct(String id, int position) {
        deleteCategory(id, position);
    }

    @Override
    public void onClickShareProduct(int position) {

        final GetProductData productData = productsAdapter.getmData().get(position);

        final String storeUrl = AppPreference.getInstance().getStoreUrl();
        final String mobile = AppPreference.getInstance().getUserMobile();

        if (TextUtils.isEmpty(productData.getImage300200())) {
            String price = "";
            if (productData.getVariants() != null && productData.getVariants().size() > 0) {
                price = productData.getVariants().get(0).getPrice();
            }
            String message = String.format("Item Name: %s\nPrice:%s\n", productData.getTitle(), price);
            String message1 = String.format("Place your order here %s/product/%s. ", storeUrl, productData.getId());
            String message2 = String.format("Feel free to call us on %s if you need any help with ordering online. Thank you.", mobile);

            StringBuilder builder = new StringBuilder();
            builder.append(message);
            builder.append(message1);
            builder.append(message2);

            shareIntent(builder.toString(), "Share Product");
            return;
        }

        Picasso.with(getContext())
                .load(productData.getImage300200())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {

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
                                String price = "";
                                if (productData.getVariants() != null && productData.getVariants().size() > 0) {
                                    price = productData.getVariants().get(0).getPrice();
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

    @Override
    public void onClickSwitchProduct(String id, String status) {
        setProductStatus(id, status);
    }

    @Override
    public void onClickProduct(String productId) {
        needRefesh = true;
        Bundle bundle = new Bundle();
        bundle.putString(AddProductActivity.PRODUCT_ID, productId);
        startActivity(AddProductActivity.getStartIntent(getContext(), bundle));
    }
}