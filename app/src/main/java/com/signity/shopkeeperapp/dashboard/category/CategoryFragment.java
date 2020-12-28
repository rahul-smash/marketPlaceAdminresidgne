package com.signity.shopkeeperapp.dashboard.category;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.categories.AddCategoryActivity;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.CategoryStatus.CategoryStatus;
import com.signity.shopkeeperapp.model.DeleteCategory.DeleteCategories;
import com.signity.shopkeeperapp.network.NetworkAdaper;
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

public class CategoryFragment extends Fragment implements CategoryAdapter.CategoriesListener {
    public static final String TAG = "CategoryFragment";
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerViewCategories;
    private LinearLayoutManager layoutManager;
    private LinearLayout linearLayoutAddCategory;
    private int pageSize = 1000, currentPageNumber = 1, start, totalCategory;
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
                    if (start < totalCategory) {
                        getAllOrdersMethod();
                    }
                }
            }
        }
    };

    public static CategoryFragment getInstance(Bundle bundle) {
        CategoryFragment fragment = new CategoryFragment();
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_category, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            publishOnline();
        }
        return super.onOptionsItemSelected(item);
    }

    private void publishOnline() {
        NetworkAdaper.getNetworkServices().publish(new Callback<CategoryStatus>() {
            @Override
            public void success(CategoryStatus categoryStatus, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCategories.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(getContext());
        categoryAdapter.setListener(this);
        recyclerViewCategories.setAdapter(categoryAdapter);
        recyclerViewCategories.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private void initView(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        linearLayoutAddCategory = view.findViewById(R.id.ll_add_category);

        linearLayoutAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AddCategoryActivity.ACTIVITY_TYPE, AddCategoryActivity.ActivityType.ADD);
                startActivity(AddCategoryActivity.getStartIntent(getContext(), bundle));
                AnimUtil.slideFromRightAnim(getActivity());
            }
        });
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
            DialogUtils.showAlertDialog(getActivity(), "Internet", "Please check your Internet Connection.");
            return;
        }

        getCategoriesApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPageNumber = 1;
        start = 0;
        categoryAdapter.clearData();
        getAllOrdersMethod();
    }

    public void getCategoriesApi() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);

        NetworkAdaper.getNetworkServices().getCategories(param, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (getCategoryResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalCategory = getCategoryResponse.getTotal();

                    List<SubCategory> categoryList = new ArrayList<>();
                    for (GetCategoryData categoryResponse : getCategoryResponse.getData()) {
                        for (SubCategory category : categoryResponse.getSubCategory()) {
                            category.setCategoryName(categoryResponse.getTitle());
                            category.setCategoryId(categoryResponse.getId());
                            categoryList.add(category);
                        }
                    }

                    categoryAdapter.setCategoryDataList(categoryList);
                } else {
                    Toast.makeText(getContext(), "Data not found!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClickDeleteCategory(String subCategoryId, int position) {
        deleteCategory(subCategoryId, position);
    }

    @Override
    public void onClickSwitchProduct(String id, String status) {
        setCategoryStatus(id, status);
    }

    @Override
    public void onClickCategory(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(AddCategoryActivity.CATEGORY_ID, id);
        bundle.putSerializable(AddCategoryActivity.ACTIVITY_TYPE, AddCategoryActivity.ActivityType.EDIT);
        startActivity(AddCategoryActivity.getStartIntent(getContext(), bundle));
    }

    public void showAlertDialog(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Please visit Admin Portal to add new category.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                getString(R.string.str_lbl_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AddCategoryActivity.ACTIVITY_TYPE, AddCategoryActivity.ActivityType.ADD);
                        startActivity(AddCategoryActivity.getStartIntent(getContext(), bundle));
                        AnimUtil.slideFromRightAnim(getActivity());
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    public void deleteCategory(String subCategoryId, final int position) {
        ProgressDialogUtil.showProgressDialog(getContext());
        Map<String, Object> param = new HashMap<>();
        param.put("catid", subCategoryId);

        NetworkAdaper.getNetworkServices().delCategory(param, new Callback<DeleteCategories>() {
            @Override
            public void success(DeleteCategories deleteCategories, Response response) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (deleteCategories.getSuccess()) {
                    categoryAdapter.removeItem(position);
                    if (start < totalCategory) {
                        getAllOrdersMethod();
                    }
                    publishOnline();
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

    public void setCategoryStatus(final String subCategoryId, String status) {
        ProgressDialogUtil.showProgressDialog(getContext());
        Map<String, Object> param = new HashMap<>();
        param.put("cat_id", subCategoryId);
        param.put("category_status", status);

        NetworkAdaper.getNetworkServices().setCategoryStatus(param, new Callback<CategoryStatus>() {
            @Override
            public void success(CategoryStatus categoryStatus, Response response) {
                if (!isAdded()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();

                if (categoryStatus.getSuccess()) {
                    categoryAdapter.updateCategoryStatus(subCategoryId);
                    publishOnline();
                } else {
                    Toast.makeText(getContext(), categoryStatus.getMessage(), Toast.LENGTH_SHORT).show();
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
}
