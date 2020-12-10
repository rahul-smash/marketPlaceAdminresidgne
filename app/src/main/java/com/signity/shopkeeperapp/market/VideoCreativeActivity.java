package com.signity.shopkeeperapp.market;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.onesignal.OneSignal;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.market.SharePermissionResponse;
import com.signity.shopkeeperapp.model.market.facebookPost.FacebookPostResponse;
import com.signity.shopkeeperapp.model.runner.CommonResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Ketan Tetry on 4/12/19.
 */
public class VideoCreativeActivity extends BaseActivity implements FacebookPagesDialog.PageCallback, FacebookDialog.PostCallback, ShareCreativeChooserDialog.ShareListener {
    private static final int REQUEST_PERMISSION = 1002;
    private static final String TAG = VideoCreativeActivity.class.getSimpleName();
    private Toolbar toolbar;

    private ImageView imageView;

    private PlayerView playerView;

    private ImageView playButton;

    private TextView textViewShared;

    private EditText editTextAbout;

    private TextView textViewTitle;

    private TextView textViewFacebook;

    private ConstraintLayout constraintLayoutFacebook;

    private ContentLoadingProgressBar progressBar;

    private SimpleExoPlayer player;
    private CallbackManager callbackManager;
    private String videoUrl, desc, title, videoId, thumbUrl;
    private boolean isShared, hasPermission, isLogged;
    private File file;
    private boolean flag;
    private boolean hasPage;
    private SharePermissionResponse.Data data;

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, VideoCreativeActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_creative);
        initViews();
        setUp();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.iv_about);
        playerView = findViewById(R.id.videoView);
        playButton = findViewById(R.id.btnPlay);
        textViewTitle = findViewById(R.id.tv_title);
        textViewShared = findViewById(R.id.tv_already_shared);
        textViewFacebook = findViewById(R.id.tv_share_facebook);
        editTextAbout = findViewById(R.id.edt_blurb);
        constraintLayoutFacebook = findViewById(R.id.const_facebook);
        progressBar = findViewById(R.id.progress_load);

        constraintLayoutFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFacebook();
            }
        });

        findViewById(R.id.nested_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOutside();
            }
        });
    }

    protected void setUp() {
        getExtra();
        setUpToolbar();
        setUpData();
        setFacebook();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializePlayer();
                progressBar.show();
                imageView.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.GONE);
            }
        });

        playButton.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        progressBar.show();
        initializePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
        playButton.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        progressBar.hide();
    }

    public void onClickOutside() {
        editTextAbout.clearFocus();
        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyboard();
    }

    private void setFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                getUserInfo(accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {


            }
        });
    }

    private void getUserInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                openPagesDialog();
                try {
                    String userId = object.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLogin();
    }

    public void initializePlayer() {
        if (player != null) {
            releasePlayer();
        }

        // Show progress here

        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        String playerInfo = Util.getUserAgent(this, "ExoPlayerInfo");

        // Default parameters, except allowCrossProtocolRedirects is true
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                playerInfo,
                null /* listener */,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                this, null, httpDataSourceFactory);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(videoUrl));

        playerView.setPlayer(player);
        player.prepare(mediaSource);
        //player.setPlayWhenReady(true);
        player.setRepeatMode(Player.REPEAT_MODE_OFF);
        //     player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d(TAG, "onLoadingChanged: " + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (Player.STATE_IDLE == playbackState) {
                    Log.d(TAG, "onPlayerStateChanged: Idle");
                }
                if (Player.STATE_READY == playbackState) {
                    Log.d(TAG, "onPlayerStateChanged: Ready");
                    // hide loading...
                    imageView.setVisibility(View.INVISIBLE);
                    progressBar.hide();
                    player.setPlayWhenReady(true);
                }
                if (Player.STATE_BUFFERING == playbackState) {
                    Log.d(TAG, "onPlayerStateChanged: Buffering...");
                    progressBar.show();
                }
                if (Player.STATE_ENDED == playbackState) {
                    Log.d(TAG, "onPlayerStateChanged: ended...");
                    playButton.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e("error", "onPlayerError: " + error.getLocalizedMessage());
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void checkLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        hasPage = AppPreference.getInstance().getFacebookPageId() != null;
        isLogged = accessToken != null;
        hasPermission = true;

        textViewFacebook.setText((isLogged || hasPage) ? "Share on Facebook" : "Login with Facebook");
        constraintLayoutFacebook.setVisibility(hasPermission ? View.VISIBLE : View.GONE);
    }

    private void openPagesDialog() {
        if (getSupportFragmentManager().findFragmentByTag(FacebookPagesDialog.TAG) == null) {
            FacebookPagesDialog facebookDialog = FacebookPagesDialog.getInstance(null);
            facebookDialog.setListener(VideoCreativeActivity.this);
            facebookDialog.show(getSupportFragmentManager(), FacebookPagesDialog.TAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    private void setUpData() {

        if (!TextUtils.isEmpty(thumbUrl)) {
            Glide.with(this)
                    .asBitmap()
                    .load(thumbUrl)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processedBitMap(resource, AppPreference.getInstance().getStoreName(), AppPreference.getInstance().getLocation(), AppPreference.getInstance().getUserMobile());
                                }
                            });
                            return false;
                        }
                    })
                    .submit();
        }

        final String descFormat = String.format("%s %s", desc, AppPreference.getInstance().getStoreUrl());
        editTextAbout.setText(descFormat);
        textViewTitle.setText(title);
        textViewShared.setVisibility(isShared ? View.VISIBLE : View.GONE);
        editTextAbout.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextAbout.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editTextAbout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextAbout.setBackground(hasFocus ? getResources().getDrawable(R.drawable.border_shape_stroke2) : getResources().getDrawable(R.drawable.border_shape_stroke));
                editTextAbout.setTextColor(hasFocus ? getResources().getColor(R.color.colorTextDark) : getResources().getColor(R.color.colorTextGrey));
            }
        });

    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            videoUrl = bundle.getString("videoUrl");
            desc = bundle.getString("desc");
            title = bundle.getString("title");
            videoId = bundle.getString("videoId");
            isShared = bundle.getBoolean("isShared");
            thumbUrl = bundle.getString("thumbUrl");
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void onClickFacebook() {

        if (isPlaying()) {
            releasePlayer();
            playButton.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }

        if (isLogged) {
            if (hasPage) {
//                showShareChooser();
                facebookPostNow();
            } else {
                openPagesDialog();
            }
        } else {
            LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("manage_pages", "publish_pages"));
        }
    }

    private void showFacebookDialog() {
        if (getSupportFragmentManager().findFragmentByTag(FacebookDialog.TAG) == null) {
            FacebookDialog facebookDialog = FacebookDialog.getInstance(null);
            facebookDialog.setCallback(this);
            facebookDialog.show(getSupportFragmentManager(), FacebookDialog.TAG);
        }
    }

    private void showShareChooser() {
        if (getSupportFragmentManager().findFragmentByTag(ShareCreativeChooserDialog.TAG) == null) {
            ShareCreativeChooserDialog chooserDialog = new ShareCreativeChooserDialog(this);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Type", Constant.ShareMode.SCHEDULE);
            chooserDialog.setArguments(bundle);
            chooserDialog.show(getSupportFragmentManager(), ShareCreativeChooserDialog.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
            super.onBackPressed();
            hideKeyboard();
        }
        return super.onOptionsItemSelected(item);
    }

    // Permission Check can be removed as it's no longer required here
    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPageSelected() {
        checkLogin();
//        showShareChooser();
        facebookPostNow();
    }

    @Override
    public void onNoPageFound() {
        checkLogin();
    }

    private void facebookPostNow() {

        if (file == null) {
            Toast.makeText(this, "Loading Image...", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = AppPreference.getInstance().getFacebookPageId();
        String accessToken1 = AppPreference.getInstance().getFacebookPageAccessToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("source", file.getName(), requestFile);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.facebookGraph().postFacebookVideo(id, editTextAbout.getText().toString().trim(), videoUrl, accessToken1, body, new Callback<FacebookPostResponse>() {
            @Override
            public void success(FacebookPostResponse facebookPostResponse, Response response) {
                if (isDestroyed()) {
                    return;
                }
                finish();
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(VideoCreativeActivity.this, "Video is being published on your page", Toast.LENGTH_SHORT).show();
                saveShared("facebook", true, facebookPostResponse.getPostId());
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                }
            }
        });
    }

    public void schedulePost(int time) {

        if (file == null) {
            Toast.makeText(this, "Loading Image...", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "schedulePost: " + time);
        String pageId = AppPreference.getInstance().getFacebookPageId();
        String accessToken1 = AppPreference.getInstance().getFacebookPageAccessToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("source", file.getName(), requestFile);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.facebookGraph().postScheduleFacebookVideo(pageId, editTextAbout.getText().toString().trim(), videoUrl, accessToken1, body, false, time, new Callback<FacebookPostResponse>() {
            @Override
            public void success(FacebookPostResponse facebookPostResponse, Response response) {
                if (isDestroyed()) {
                    return;
                }
                finish();
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(VideoCreativeActivity.this, "Video scheduled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                if (!isDestroyed()) {
                    ProgressDialogUtil.hideProgressDialog();
                }
            }
        });
    }

    private boolean isPlaying() {
        return player != null
                && player.getPlaybackState() != Player.STATE_ENDED
                && player.getPlaybackState() != Player.STATE_IDLE
                && player.getPlayWhenReady();
    }

    private void saveShared(String medium, final boolean isFinish, String postId) {
        Map<String, Object> param = new HashMap<>();
        param.put("valueapp_store_id", AppPreference.getInstance().getStoreId());
        param.put("creative", videoId);
        param.put("share_medium_type", medium);
        param.put("post_id", postId);
        param.put("creative_type", "video");

        NetworkAdaper.marketStore().saveSharedData(param, new Callback<CommonResponse>() {
            @Override
            public void success(CommonResponse response, Response response2) {
                if (isFinish)
                    finish();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onPostNow() {
        facebookPostNow();
    }

    @Override
    public void onSchedulePost(int time) {
/*
        if (AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic")) {
            openSubscribeDialog();
            return;
        }

        checkShareLimit(time, ShareType.SCHEDULE);*/
        schedulePost(time);
    }

/*    private void openSubscribeDialog() {
        if (getSupportFragmentManager().findFragmentByTag(SubscribeDialog.TAG) == null) {
            Bundle bundle = new Bundle();
            bundle.putString("message", "");
            bundle.putString("upgradeMessage", "Kindly upgrade your pack to\naccess video post");
            bundle.putString("title", "Upgrade Pack");
            bundle.putString("btnTitle", "Buy Now");
            SubscribeDialog subscribeDialog = SubscribeDialog.getInstance(bundle);
            subscribeDialog.setListener(new SubscribeDialog.DialogListener() {
                @Override
                public void onSubscribe() {
                    subscribeDialog.dismiss();
                    startActivity(SubscriptionActivity.getStartIntent(VideoCreativeActivity.this));
                }
            });
            subscribeDialog.show(getSupportFragmentManager(), SubscribeDialog.TAG);
        }
    }*/

    @Override
    public void onClickSchedule() {

/*        if (AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic")) {
            openSubscribeDialog();
            return;
        }*/

        showFacebookDialog();
    }

    @Override
    public void onClickPost() {

        /*if (AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic")) {
            openSubscribeDialog();
            return;
        }

        checkShareLimit(1, ShareType.POST_NOW);*/
        facebookPostNow();
    }

/*
    private void checkShareLimit(int time, ShareType shareType) {
        String type = "daily_video_count";
        showLoading();
        AppApiHelper.getApiHelper()
                .getSharePermission(AppPreferenceHelper.getInstance().getStoreId(), type)
                .enqueue(new DigiCallback<SharePermissionResponse>(this) {
                    @Override
                    public void onSuccess(SharePermissionResponse response) {
                        if (isDestroyed()) {
                            return;
                        }

                        hideLoading();
                        if (response.getIsshare()) {
                            data = response.getData();
                            switch (shareType) {
                                case POST_NOW:
                                    facebookPostNow();
                                    break;
                                case SCHEDULE:
                                    schedulePost(time);
                                    break;
                            }
                        } else {
                            showMessage(response.getMessage());
                        }

                    }

                    @Override
                    public void onFailure() {
                        hideLoading();
                    }
                });
    }
*/

/*    private boolean canPostToday() {
        return AppPreferenceHelper.getInstance().getVideoCountPerDay() < AppPreferenceHelper.getInstance().getDailyVideoLimit();
    }

    private boolean checkPostLimit() {
        return AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic") &&
                AppPreferenceHelper.getInstance().getPostCountPerMonthVideo() >= AppPreferenceHelper.getInstance().getMonthlyVideoLimit();
    }*/

    private void setTagCount() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OneSignal.getTags(new OneSignal.GetTagsHandler() {
                    @Override
                    public void tagsAvailable(JSONObject tags) {
                        try {
                            String videoCount = tags.optString("video_share");
                            OneSignal.sendTag("video_share", String.valueOf(TextUtils.isEmpty(videoCount) ? 0 : Integer.parseInt(videoCount) + 1));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }).start();

    }

    private void processedBitMap(Bitmap bitmap, String title, String subTitle, String mobile) {
        LinearLayout v = new LinearLayout(this);
        v.setOrientation(LinearLayout.HORIZONTAL);
        v.setLayoutParams(new ViewGroup.LayoutParams(bitmap.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        int sizeFourtyEight = 48;
        int sizeThirty = 30;
        v.setPadding(sizeFourtyEight, 12, sizeFourtyEight, 12);
        v.setBackgroundColor(Color.parseColor("#AA000000"));

        LinearLayout subContainer = new LinearLayout(this);
        subContainer.setOrientation(LinearLayout.VERTICAL);
        subContainer.setLayoutParams(new ViewGroup.LayoutParams(bitmap.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        TextView tvTitleName = new TextView(this);
        tvTitleName.setLayoutParams(params);
        subContainer.addView(tvTitleName);
        tvTitleName.setTextColor(Color.WHITE);
        tvTitleName.setText(title);
        tvTitleName.setMaxLines(1);
        tvTitleName.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeFourtyEight);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto);
        tvTitleName.setTypeface(typeface);

        TextView tvSubTitleName = new TextView(this);
        tvSubTitleName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        subContainer.addView(tvSubTitleName);
        tvSubTitleName.setTextColor(Color.WHITE);
        Typeface typeface2 = ResourcesCompat.getFont(this, R.font.roboto);
        tvSubTitleName.setTypeface(typeface2);
        tvSubTitleName.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeThirty);
        tvSubTitleName.setText(subTitle);
        tvSubTitleName.setMaxLines(1);
        tvSubTitleName.setEllipsize(TextUtils.TruncateAt.END);

        TextView tvMobile = new TextView(this);
        tvMobile.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvMobile.setTextColor(Color.WHITE);
        Typeface typeface3 = ResourcesCompat.getFont(this, R.font.roboto);
        tvMobile.setTypeface(typeface3, Typeface.BOLD);
        tvMobile.setText(mobile);
        tvMobile.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeFourtyEight);
        ((LinearLayout.LayoutParams) tvMobile.getLayoutParams()).gravity = Gravity.CENTER;
        v.addView(subContainer);
        v.addView(tvMobile);
        tvMobile.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subContainer.getLayoutParams().width = bitmap.getWidth() - tvMobile.getMeasuredWidth() - 2 * sizeFourtyEight;

        if (v.getMeasuredHeight() <= 0) {
            v.measure(bitmap.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            Bitmap viewBitMap = Bitmap.createBitmap(bitmap.getWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(viewBitMap);
            v.layout(0, 0, bitmap.getWidth(), v.getMeasuredHeight());
            v.draw(c);
            Bitmap combineImages = combineImages(bitmap, viewBitMap);
            imageView.setImageBitmap(combineImages);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            file = com.signity.shopkeeperapp.util.Util.saveImageFile(combineImages, "digisalon_share", getExternalFilesDir("Digi"));
        }

    }

    public Bitmap combineImages(Bitmap background, Bitmap foreground) {
        Bitmap cs;
        cs = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        background = Bitmap.createScaledBitmap(background, background.getWidth(), background.getHeight(), true);
        comboImage.drawBitmap(background, 0, 0, null);
        comboImage.drawBitmap(foreground, 0, background.getHeight() - foreground.getHeight() - 30, null);
        return cs;
    }

    enum ShareType {
        POST_NOW, SCHEDULE
    }

}
