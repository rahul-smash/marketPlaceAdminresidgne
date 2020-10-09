package com.signity.shopkeeperapp.dashboard.Products;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.RvGridSpacesItemDecoration;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.GetProductResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.products.AddProductActivity;
import com.signity.shopkeeperapp.products.CategoryDialog;
import com.signity.shopkeeperapp.products.SubCategoryDialog;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductFragment extends Fragment implements View.OnClickListener, ProductsAdapter.OnItemClickListener {
    public static final String TAG = "ProductFragment";
    View hiddenView;
    private ProductsAdapter categoriesAdapter;
    private LinearLayout linearLayoutAddProduct;
    private List<GetProductData> categoryData = new ArrayList<>();
    private RecyclerView recyclerViewProduct;
    private LinearLayoutManager layoutManager;
    private int pageSize = 10, currentPageNumber = 1, start, totalOrders;
    private List<GetCategoryData> categoryDataList = new ArrayList<>();
    private String selectedCategoryId = "0";
    private String selectedSubCategoryId = "0";
    private TextInputEditText txtSelectCategory, txtSubCategory;
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

            if (!isLoading()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    if (start < totalOrders) {
                        getAllOrdersMethod();
                    }
                }
            }
        }
    };

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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_products, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_saerch) {

        }
        if (item.getItemId() == R.id.action_filter) {
            // TODO - Filter Menu
            showOverViewPopMenu();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
        getCategoriesApi();
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewProduct.setLayoutManager(layoutManager);
        categoriesAdapter = new ProductsAdapter(getContext());
        recyclerViewProduct.setAdapter(categoriesAdapter);
        recyclerViewProduct.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerViewProduct.addItemDecoration(new RvGridSpacesItemDecoration((int) Util.pxFromDp(getContext(), 16)));
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
        getAllOrdersMethod();
    }


    public void getAllOrdersMethod() {

        if (!Util.checkIntenetConnection(getContext())) {
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            return;
        }

        getProductApi();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getProductApi() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);
        param.put("cat_id", selectedCategoryId);
        param.put("sub_cat_ids", selectedSubCategoryId);

        NetworkAdaper.getNetworkServices().getAllProducts(param, new Callback<GetProductResponse>() {
            @Override
            public void success(GetProductResponse getProductResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (getProductResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalOrders = getProductResponse.getTotal();
                    categoryData = getProductResponse.getData();
                    if (categoryData != null && categoryData.size() != 0) {
                        categoriesAdapter.setmData(categoryData);
                    } else {
                        Toast.makeText(getActivity(), "Data not Found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Data not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == linearLayoutAddProduct) {
            showAlertDialog(getActivity());


         /* //Hide code here
          startActivity(AddProductActivity.getStartIntent(getContext()));
            AnimUtil.slideFromRightAnim(getActivity());*/
        }
    }

    @Override
    public void onItemClick(View itemView, int position, GetProductData productData) {

    }

    private void showOverViewPopMenu() {

        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.popup_product_filters, null, false);
        final PopupWindow popupWindowOverView = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowOverView.setOutsideTouchable(true);

        popupWindowOverView.setBackgroundDrawable(new ColorDrawable());
        popupWindowOverView.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) // here I want to close the pw when clicking outside it but at all this is just an example of how it works and you can implement the onTouch() or the onKey() you want
                {
                    popupWindowOverView.dismiss();
                    return true;
                }
                return false;
            }

        });

        float den = getActivity().getResources().getDisplayMetrics().density;
        int offsetY = (int) (den * 2);
        popupWindowOverView.showAtLocation(layout, Gravity.TOP, 0, 0);
        txtSelectCategory = layout.findViewById(R.id.edt_category);
        Button btnApply = layout.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPageNumber = 1;
                getProductApi();
                popupWindowOverView.dismiss();

            }
        });
        Button btnCancel = layout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        Log.i("---categoryName", "" + categoryName);

                        selectedCategoryId = categoryId;
                        txtSelectCategory.setText(categoryName);
                    }
                });
                categoryDialog.show(getActivity().getSupportFragmentManager(), CategoryDialog.TAG);

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
                        Log.i("---subCategoryName", "" + subCategoryName);

                        selectedSubCategoryId = subCategoryId;
                        txtSubCategory.setText(subCategoryName);
                    }
                });
                subCategoryDialog.show(getActivity().getSupportFragmentManager(), SubCategoryDialog.TAG);
            }
        });
    }


    public void getCategoriesApi() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, Object> param = new HashMap<>();
        param.put("page", 1);
        param.put("pagesize", 1000);

        NetworkAdaper.getNetworkServices().getCategories(param, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (getCategoryResponse.getSuccess()) {
                    categoryDataList = getCategoryResponse.getData();
                } else {
                    Toast.makeText(getActivity(), "Data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
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
}

