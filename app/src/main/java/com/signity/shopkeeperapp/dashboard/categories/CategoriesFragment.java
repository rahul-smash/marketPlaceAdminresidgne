package com.signity.shopkeeperapp.dashboard.categories;

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
import com.signity.shopkeeperapp.adapter.RvGridSpacesItemDecoration;
import com.signity.shopkeeperapp.categories.AddCategoryActivity;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
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

public class CategoriesFragment extends Fragment implements CategoriesAdapter.CategoriesListener {
    public static final String TAG = "CategoriesFragment";
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView recyclerViewCategories;
    private LinearLayoutManager layoutManager;
    private LinearLayout linearLayoutAddCategory;
    private int pageSize = 10, currentPageNumber = 1, start, totalCategory;
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

    public static CategoriesFragment getInstance(Bundle bundle) {
        CategoriesFragment fragment = new CategoriesFragment();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCategories.setLayoutManager(layoutManager);
        categoriesAdapter = new CategoriesAdapter(getContext());
        categoriesAdapter.setListener(this);
        recyclerViewCategories.setAdapter(categoriesAdapter);
        recyclerViewCategories.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerViewCategories.addItemDecoration(new RvGridSpacesItemDecoration((int) Util.pxFromDp(getContext(), 16)));
    }

    private void initView(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        linearLayoutAddCategory = view.findViewById(R.id.ll_add_category);

        linearLayoutAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddCategoryActivity.getStartIntent(getContext()));
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
        getAllOrdersMethod();
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
    }

    public void getCategoriesApi() {
        ProgressDialogUtil.showProgressDialog(getActivity());
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);

        NetworkAdaper.getNetworkServices().getCategories(param, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (getCategoryResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalCategory = getCategoryResponse.getTotal();

                    List<SubCategory> categoryList = new ArrayList<>();
                    for (GetCategoryData categoryResponse : getCategoryResponse.getData()) {
                        for (SubCategory category : categoryResponse.getSubCategory()) {
                            category.setCategoryName(categoryResponse.getTitle());
                            categoryList.add(category);
                        }
                    }

                    categoriesAdapter.setCategoryDataList(categoryList);
                } else {
                    Toast.makeText(getContext(), "Data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    @Override
    public void onClickCategory(int position) {

    }
}
