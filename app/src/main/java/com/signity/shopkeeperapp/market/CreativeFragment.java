package com.signity.shopkeeperapp.market;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.creatives.Creative;
import com.signity.shopkeeperapp.model.creatives.CreativeModel;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class CreativeFragment extends Fragment implements CreativeRecycleAdapter.CreativeListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = CreativeFragment.class.getSimpleName();
    public static final String MARKET_MODE = "MARKET_MODE";
    private static final int REQUEST_PERMISSION = 1002;

    private RecyclerView recyclerViewCreative;

    private SwipeRefreshLayout swipeRefreshLayout;

    private CreativeRecycleAdapter creativeRecycleAdapter;
    private List<CreativeModel> creativeModelsFilter;
    private ArrayList<String> choosenList;
    private int mainPosition, childPosition;
    private boolean isFiltering;
    private Constant.MarketMode marketMode;

    public static CreativeFragment getInstance(@NonNull Bundle bundle) {
        CreativeFragment fragment = new CreativeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void setUp() {
        // TODO - Init Views
        getExtra();
        setUpCreativeAdapter();
        setUpSwipe();
        setHasOptionsMenu(true);
        showLoading();
        fetchApiData();
    }

    private void showLoading() {
        ProgressDialogUtil.showProgressDialog(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp();
    }

    private void fetchApiData() {
        switch (marketMode) {
            case CREATIVE:
                getCreatives();
                break;
            case FRAME:
                getFrames();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + marketMode);
        }
    }

    private void getExtra() {
        if (getArguments() != null) {
            marketMode = (Constant.MarketMode) getArguments().getSerializable(MARKET_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_creative, container, false);
    }

    private void setUpSwipe() {
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));
    }

    private void setUpCreativeAdapter() {
        creativeRecycleAdapter = new CreativeRecycleAdapter(getActivity(), marketMode);
        creativeRecycleAdapter.setListener(this);
        recyclerViewCreative.setAdapter(creativeRecycleAdapter);
        recyclerViewCreative.setLayoutManager(new LinearLayoutManager(getActivity()));

        choosenList = new ArrayList<>();
    }

    private void getCreatives() {

        NetworkAdaper.marketStore().getCreatives(new Callback<List<CreativeModel>>() {
            @Override
            public void success(List<CreativeModel> creativeModels, Response response) {
                if (!isAdded()) {
                    return;
                }

                swipeRefreshLayout.setRefreshing(false);

                if (creativeModels.size() == 0) {
                    return;
                }

                creativeModelsFilter = new ArrayList<>();
                choosenList.clear();

                for (CreativeModel creativeModel : creativeModels) {
                    if (creativeModel.getCreatives() == null || creativeModel.getCreatives().size() == 0) {
                        continue;
                    }
                    choosenList.add(creativeModel.getTitle());
                    creativeModelsFilter.add(creativeModel);
                }

                creativeRecycleAdapter.setCreativeModelList(creativeModelsFilter);
            }

            @Override
            public void failure(RetrofitError error) {
                if (isAdded()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void getFrames() {
        NetworkAdaper.marketStore().getFrames(new Callback<List<CreativeModel>>() {
            @Override
            public void success(List<CreativeModel> creativeModels, Response response) {
                if (!isAdded()) {
                    return;
                }

                swipeRefreshLayout.setRefreshing(false);

                if (creativeModels.size() == 0) {
                    return;
                }

                creativeModelsFilter = new ArrayList<>();
                choosenList.clear();

                for (CreativeModel creativeModel : creativeModels) {
                    if (creativeModel.getCreatives() == null || creativeModel.getCreatives().size() == 0) {
                        continue;
                    }
                    choosenList.add(creativeModel.getTitle());
                    creativeModelsFilter.add(creativeModel);
                }

                creativeRecycleAdapter.setCreativeModelList(creativeModelsFilter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (marketMode == Constant.MarketMode.CREATIVE) {
            inflater.inflate(R.menu.menu_shared_creative, menu);
        } else {
            inflater.inflate(R.menu.menu_creative, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_creative_filter) {

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("choosen", choosenList);
            bundle.putSerializable(FilterActivity.MARKET_MODE, marketMode);
            Intent intent = FilterActivity.getStartIntent(getActivity(), bundle);
            startActivityForResult(intent, FilterActivity.REQUEST_CODE);
        }

        if (item.getItemId() == R.id.action_shared_creatives) {
//            startActivity(SharedPostActivity.getStartIntent(getContext()));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == FilterActivity.REQUEST_CODE) {
            if (data != null && data.getExtras() != null) {

                choosenList = data.getExtras().getStringArrayList("result");

                if (choosenList == null || choosenList.size() == 0) {
                    creativeRecycleAdapter.setCreativeModelList(creativeModelsFilter);
                    for (CreativeModel creativeModel : creativeModelsFilter) {
                        choosenList.add(creativeModel.getTitle());
                    }
                    isFiltering = false;
                    return;
                }

                isFiltering = true;

                List<CreativeModel> list = new ArrayList<>();

                for (String dataa : choosenList) {
                    for (CreativeModel creativeModel : creativeModelsFilter) {
                        if (creativeModel.getTitle().equals(dataa)) {
                            list.add(creativeModel);
                            break;
                        }
                    }
                }
                Log.d(TAG, "onActivityResult: " + list.size());
                creativeRecycleAdapter.setCreativeModelList(list);
            }
        }
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
        }
        return false;
    }

    @Override
    public void onClickViewAll(int position) {
        if (marketMode == Constant.MarketMode.FRAME) {
            openCreativeDetail(position, null);
        } else {
            openCreativeDetail(position, Constant.CreativeView.VIEW_ALL);
        }
    }

    @Override
    public void onClickViewShared(int position) {
        openCreativeDetail(position, Constant.CreativeView.VIEW_SHARED);
    }

    private void openCreativeDetail(int position, Constant.CreativeView viewShared) {

        if (position < 0) {
            return;
        }

        CreativeModel creativeModel = creativeRecycleAdapter.getCreativeModelList().get(position);

        Intent intent = CreativeDetailActivity.getStartIntent(getActivity());
        intent.putExtra("tagId", creativeModel.getId());
        intent.putExtra("creativeTitle", creativeModel.getTitle());
        intent.putExtra("type", viewShared);
        intent.putExtra(CreativeDetailActivity.MARKET_MODE, this.marketMode);
        startActivity(intent);
    }

    @Override
    public void onClickCreative(int mainPosition, int childPosition) {

        this.mainPosition = mainPosition;
        this.childPosition = childPosition;
        int tagId = creativeRecycleAdapter.getCreativeModelList().get(mainPosition).getId();
        Creative creative = creativeRecycleAdapter.getCreativeModelList().get(mainPosition).getCreatives().get(childPosition);

        switch (marketMode) {
            case CREATIVE:
                shareIntent(creative, tagId);
                break;
            case FRAME:
                /*if (AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic")) {
                    openSubscribeDialog();
                    return;
                }*/
                Bundle bundle = new Bundle();
                bundle.putString("url", creative.getImageUrl());
                bundle.putString("desc", creative.getDescription());
                bundle.putString("title", creative.getTitle());
                bundle.putString("creativeId", String.valueOf(creative.getId()));
                startActivity(CustomFrameActivity.getStartIntent(getContext(), bundle));
                break;
            default:
                throw new RuntimeException("Mode not available");
        }

    }

    private void shareIntent(Creative creative, int tagId) {
        Intent intent = ShareCreativeActivity.getStartIntent(getActivity());
        intent.putExtra("url", creative.getImageUrl());
        intent.putExtra("desc", creative.getDescription());
        intent.putExtra("title", creative.getTitle());
        intent.putExtra("creativeId", String.valueOf(creative.getId()));
        intent.putExtra("isShared", creative.isShared());
        intent.putExtra("tagId", tagId);
        intent.putExtra(CreativeFragment.MARKET_MODE, marketMode);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onClickCreative(mainPosition, childPosition);
            } else {
                Toast.makeText(getActivity(), "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRefresh() {
        if (!isFiltering) {
            fetchApiData();
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
