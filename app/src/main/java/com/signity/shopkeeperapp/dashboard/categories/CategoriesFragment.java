package com.signity.shopkeeperapp.dashboard.categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.dashboard.DashboardActivity;
import com.signity.shopkeeperapp.dashboard.home.HomeFragment;
import com.signity.shopkeeperapp.dashboard.orders.OrderDetailActivity;
import com.signity.shopkeeperapp.dashboard.orders.OrdersFragment;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.orders.DueOrderActivity;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoriesFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "CategoriesFragment";
    private List<GetCategoryData> categoryData = new ArrayList<>();
    CategoriesAdapter categoriesAdapter;
    private RecyclerView recyclerViewCategories;
    private LinearLayoutManager layoutManager;
    TextView txtAddCategory;
    private int pageSize = 10, currentPageNumber = 1, start, totalOrders;
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
                        currentPageNumber++;
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
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_category, menu);
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
        categoriesAdapter = new CategoriesAdapter(getContext(), categoryData);
        recyclerViewCategories.setAdapter(categoriesAdapter);
        recyclerViewCategories.addOnScrollListener(recyclerViewOnScrollListener);
    }

    private void initView(View rootView) {
        txtAddCategory = rootView.findViewById(R.id.txtAddCategory);
        recyclerViewCategories = rootView.findViewById(R.id.recyclerViewCategories);
        txtAddCategory.setOnClickListener(this);
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
        param.put("pagesize", pageSize);

        NetworkAdaper.getNetworkServices().getCategories(param, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (getCategoryResponse.getSuccess()) {
                    start += pageSize;

                    categoryData = getCategoryResponse.getData();
                    Log.i("@@------", "" + categoryData.size());
                    if (categoryData != null && categoryData.size() != 0) {
                        setUpAdapter();

                    } else {
                        //TODO:- Show Message
                        Toast.makeText(getActivity(), "Data not Found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
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
        if (view == txtAddCategory) {

        }
    }
}
