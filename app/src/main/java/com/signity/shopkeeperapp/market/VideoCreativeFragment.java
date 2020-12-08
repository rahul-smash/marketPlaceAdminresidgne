package com.signity.shopkeeperapp.market;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.market.videoCreative.VideoCreative;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ketan Tetry on 1/11/19.
 */
public class VideoCreativeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, VideoRecycleAdapter.VideoListener {

    public static final String TAG = "VideoCreativeFragment";

    RecyclerView recyclerViewVideo;

    SwipeRefreshLayout swipeRefreshLayout;

    private VideoRecycleAdapter videoRecycleAdapter;

    private List<VideoCreative> videoCreativeList = new ArrayList<>();

    public static VideoCreativeFragment getInstance() {
        return new VideoCreativeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_creative, container, false);
    }

    protected void setUp() {
        setUpCreativeAdapter();
        setUpSwipe();
        setHasOptionsMenu(true);
        getVideoCreatives();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setUp();
    }

    private void initViews(View view) {
        recyclerViewVideo = view.findViewById(R.id.rv_video);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
    }

    private void setUpSwipe() {
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));
    }

    private void setUpCreativeAdapter() {
        videoRecycleAdapter = new VideoRecycleAdapter(getActivity());
        videoRecycleAdapter.setListener(this);
        recyclerViewVideo.setAdapter(videoRecycleAdapter);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getVideoCreatives() {

        NetworkAdaper.marketStore().getVideoCreatives(1, 1, new Callback<List<VideoCreative>>() {
            @Override
            public void success(List<VideoCreative> videoCreatives, Response response) {
                if (!isAdded()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);

                videoCreativeList = videoCreatives;
                videoRecycleAdapter.setVideoCreativeList(videoCreativeList);
            }

            @Override
            public void failure(RetrofitError error) {
                if (isAdded()) {
                    ProgressDialogUtil.hideProgressDialog();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == FilterActivity.REQUEST_CODE) {
            if (data != null && data.getExtras() != null) {
            }
        }
    }

    @Override
    public void onRefresh() {
        getVideoCreatives();
    }

    @Override
    public void onClickVideo(int position) {

        VideoCreative videoCreative = videoCreativeList.get(position);

        Bundle bundle = new Bundle();
        bundle.putBoolean("isShared", videoCreative.getShared());
        bundle.putString("desc", videoCreative.getDescription());
        bundle.putString("title", videoCreative.getTitle());
        bundle.putString("videoId", String.valueOf(videoCreative.getId()));
        bundle.putString("videoUrl", videoCreative.getVideo());
        bundle.putString("thumbUrl", videoCreative.getVideoThumb());

        startActivity(VideoCreativeActivity.getStartIntent(getContext(), bundle));
    }
}
