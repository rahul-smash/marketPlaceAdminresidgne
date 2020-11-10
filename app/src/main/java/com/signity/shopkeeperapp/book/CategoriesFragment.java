package com.signity.shopkeeperapp.book;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.Product.GetProductData;
import com.signity.shopkeeperapp.model.Product.GetProductResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoriesFragment extends Fragment implements ExpandableCategoriesAdapter.ExpandableCategoriesListener {

    public static final String TAG = "CategoriesFragment";
    public static final String SELECTED_PRODUCTS = "SELECTED_PRODUCTS";
    private ExpandableCategoriesAdapter expandableCategoriesAdapter;
    private RecyclerView recyclerView;
    private BookOrderActivity bookOrderActivity;
    private ContentLoadingProgressBar progressBar;
    private List<GetProductData> selectedProductList = new ArrayList<>();
    private ArrayList<SubCategory> categoryList = new ArrayList<>();

    public static CategoriesFragment getInstance(Bundle bundle) {
        CategoriesFragment fragment = new CategoriesFragment();
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
        getExtra();
        setUpAdapter();
        getCategoriesApi();
    }

    private void getExtra() {
        if (getArguments() != null) {
            selectedProductList = getArguments().getParcelableArrayList(SELECTED_PRODUCTS);
        }
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rv_categories);
        progressBar = view.findViewById(R.id.category_progress);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                progressBar.hide();
                if (getCategoryResponse.getSuccess()) {
                    categoryList.clear();
                    for (GetCategoryData categoryResponse : getCategoryResponse.getData()) {
                        for (SubCategory category : categoryResponse.getSubCategory()) {
                            category.setCategoryName(categoryResponse.getTitle());
                            category.setCategoryId(categoryResponse.getId());
                            categoryList.add(category);
                        }
                    }
                    expandableCategoriesAdapter.setCategoryDataList(categoryList);
                } else {
                    Toast.makeText(getActivity(), "Data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                progressBar.hide();
                if (getContext() != null)
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BookOrderActivity) {
            bookOrderActivity = (BookOrderActivity) context;
        }
    }

    public void searchFilterCategories(String query) {

        if (categoryList.isEmpty()) {
            return;
        }

        List<SubCategory> filterList = new ArrayList<>();
        for (SubCategory subCategory : categoryList) {
            if (subCategory.getTitle().toLowerCase().startsWith(query.toLowerCase())) {
                filterList.add(subCategory);
            }
        }
        expandableCategoriesAdapter.setCategoryDataList(filterList);
    }

    public void onCloseSearch() {
        expandableCategoriesAdapter.setCategoryDataList(categoryList);
    }

    private void setUpAdapter() {
        expandableCategoriesAdapter = new ExpandableCategoriesAdapter(getContext());
        expandableCategoriesAdapter.setListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expandableCategoriesAdapter);
    }

    @Override
    public void onClickCategory(String subCategoryId, String categoryId) {
        getProductApiUsingFilter(subCategoryId, categoryId);
    }

    @Override
    public void onAddProduct(GetProductData getProductData) {
        bookOrderActivity.addOrders(getProductData);
    }

    @Override
    public void onRemoveProduct(GetProductData getProductData) {
        bookOrderActivity.removeOrders(getProductData);
    }

    public void getProductApiUsingFilter(String subCategoryId, String categoryId) {
        Map<String, Object> param = new HashMap<>();
        param.put("page", 1);
        param.put("pagelength", 1000);
        param.put("cat_id", categoryId);
        param.put("sub_cat_ids", subCategoryId);

        NetworkAdaper.getNetworkServices().getAllProducts(param, new Callback<GetProductResponse>() {
            @Override
            public void success(GetProductResponse getProductResponse, Response response) {
                if (!isAdded()) {
                    return;
                }

                if (getProductResponse.getSuccess()) {
                    if (getProductResponse.getData() != null) {
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
                        expandableCategoriesAdapter.setProductDataList(data);
                    } else {
                        Toast.makeText(getActivity(), "Data not Found!", Toast.LENGTH_SHORT).show();
                        expandableCategoriesAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "Data not Found!", Toast.LENGTH_SHORT).show();
                    expandableCategoriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isAdded()) {
                    return;
                }
                expandableCategoriesAdapter.notifyDataSetChanged();
                if (getContext() != null)
                    Toast.makeText(getContext(), "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
