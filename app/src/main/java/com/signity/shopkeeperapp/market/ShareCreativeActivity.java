package com.signity.shopkeeperapp.market;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

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
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


/**
 * Created by Ketan Tetry on 4/12/19.
 */
public class ShareCreativeActivity extends BaseActivity implements FacebookPagesDialog.PageCallback, FacebookDialog.PostCallback, ShareCreativeChooserDialog.ShareListener {
    public static final String CREATIVE = "CREATIVE";
    private static final int REQUEST_PERMISSION = 1002;
    private static final String TAG = ShareCreativeActivity.class.getSimpleName();
    private ImageView imageViewFb;
    private TextView textViewShared;
    private TextView textViewFb;
    private EditText editTextAbout;
    private TextView textViewTitle;
    private TextView textViewFacebook;
    private ConstraintLayout constraintLayoutFacebook;
    private ConstraintLayout layoutBottomSheet;
    private View blackView;
    private ImageView arrow;
    private BottomSheetBehavior sheetBehavior;
    private Bitmap image;
    private FacebookPostDialog facebookPostDialog;
    private Toolbar toolbar;
    private ImageView imageView;
    private Constant.MarketMode marketMode = Constant.MarketMode.CREATIVE;
    private CallbackManager callbackManager;
    private String url, desc, title, creativeId;
    private boolean isShared, hasPermission, isLogged;
    private File file;
    private boolean flag;
    private boolean hasPage;
    private int tagId;
    private SharePermissionResponse.Data data;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ShareCreativeActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_creative);
        setUp();
    }

    protected void setUp() {
        setupStatusBar();
        getExtra();
        setUpToolbar();
        setUpData();
        setFacebook();

        blackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        editTextAbout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.edt_blurb) {

                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(100);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        if (!flag) {
                            blackView.setVisibility(View.VISIBLE);
                            blackView.startAnimation(alphaAnimation);
                            flag = true;
                        }
                        textViewFb.setText(editTextAbout.getText().toString().trim());
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        blackView.setVisibility(View.GONE);
                        flag = false;
                        reverseRotate(arrow);
                        bottomSheet.setBackground(getResources().getDrawable(R.drawable.background_top_round_gray));
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (!flag) {
                            blackView.setVisibility(View.VISIBLE);
                            blackView.startAnimation(alphaAnimation);
                            rotate(arrow);
                            bottomSheet.setBackground(getResources().getDrawable(R.drawable.background_top_round_white));
                            flag = true;
                        }
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void setupStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhiteTransparent));
    }

    public void rotate(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f);
        rotate.setDuration(1000);
        rotate.start();
    }

    public void reverseRotate(View view) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 180f, 0f);
        rotate.setDuration(500);
        rotate.start();
    }

    public void onClickOutside() {
        editTextAbout.clearFocus();
        hideKeyboard(editTextAbout);
    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
            hideKeyboard();
        }
    }

    @OnClick(R.id.tv_already_shared)
    void onClickDate() {
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
                Analytics.log(AppConstants.FACEBOOK_LOGIN);
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

    private void checkLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        hasPage = AppPreferenceHelper.getInstance().getFacebookPageId() != null;
        isLogged = accessToken != null;
        hasPermission = AppPreferenceHelper.getInstance().getRoleType().equalsIgnoreCase("store_owner");

        textViewFacebook.setText((isLogged || hasPage) ? "Share on Facebook" : "Login with Facebook");
        constraintLayoutFacebook.setVisibility(hasPermission ? View.VISIBLE : View.GONE);
        layoutBottomSheet.setVisibility(hasPermission ? View.VISIBLE : View.GONE);
    }

    private void openPagesDialog() {
        if (getSupportFragmentManager().findFragmentByTag(FacebookPagesDialog.TAG) == null) {
            FacebookPagesDialog facebookDialog = FacebookPagesDialog.getInstance(null);
            facebookDialog.setListener(ShareCreativeActivity.this);
            facebookDialog.show(getSupportFragmentManager(), FacebookPagesDialog.TAG);
        }
    }

    @OnClick(R.id.tv_preview_fb)
    public void onClickPreview() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            onClickOutside();
            rotate(arrow);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            layoutBottomSheet.setBackground(getResources().getDrawable(R.drawable.background_top_round_white));
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
        if (responseCode == Activity.RESULT_OK && requestCode == 123) {

            if (intent.getExtras() == null) {
                return;
            }

            String filePath = intent.getExtras().getString("file");
            boolean userCreative = intent.getExtras().getBoolean("shouldApply");
            file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (userCreative) {
                processedBitMap(BitmapFactory.decodeFile(filePath), AppPreferenceHelper.getInstance().getDisplayName(), AppPreferenceHelper.getInstance().getStoreAddress(), AppPreferenceHelper.getInstance().getDisplayNumber());
            } else {
                imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                imageViewFb.setImageBitmap(BitmapFactory.decodeFile(filePath));
                imageViewFb.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image = bitmap;
            }

            /*
            Glide.with(this).load(file).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(getResources().getDrawable(R.drawable.bigdefaluticon)).into(imageView);
            Glide.with(this).load(file).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(getResources().getDrawable(R.drawable.bigdefaluticon)).into(imageViewFb);
        */
        }
    }

    private void setUpData() {

        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .asBitmap()
                    .load(url)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processedBitMap(resource, AppPreferenceHelper.getInstance().getDisplayName(), AppPreferenceHelper.getInstance().getStoreAddress(), AppPreferenceHelper.getInstance().getDisplayNumber());
                                }
                            });
                            return false;
                        }
                    }).submit();
        } else {
            File file = null;
            if (marketMode == AppConstants.MarketMode.FRAME) {
                file = new File(getExternalFilesDir("Digi"), "CustomFrame.png");
            } else if (marketMode == AppConstants.MarketMode.GALLERY) {
                file = new File(getExternalFilesDir("Digi"), "style.jpg");
            }

            if (file != null)
                processedBitMap(BitmapFactory.decodeFile(file.getAbsolutePath()), AppPreferenceHelper.getInstance().getDisplayName(), AppPreferenceHelper.getInstance().getStoreAddress(), AppPreferenceHelper.getInstance().getDisplayNumber());
        }

        final String descFormat = String.format("%s https://%s/#appointment", desc, AppPreferenceHelper.getInstance().getWebsite());
        editTextAbout.setText(descFormat);
        textViewTitle.setText(title);
        textViewFb.setText(desc);
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
            url = getIntent().getExtras().getString("url");
            desc = getIntent().getExtras().getString("desc");
            title = getIntent().getExtras().getString("title");
            creativeId = getIntent().getExtras().getString("creativeId");
            isShared = getIntent().getExtras().getBoolean("isShared");
            tagId = getIntent().getExtras().getInt("tagId", 0);
            marketMode = (AppConstants.MarketMode) getIntent().getSerializableExtra(CreativeFragment.MARKET_MODE);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }

    @OnClick(R.id.const_share)
    public void onClickShare() {

        if (file == null) {
            showMessage("Loading Image...");
            return;
        }

        String text = String.format("%s", editTextAbout.getText().toString().trim());
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        shareIntent(text, uri);

    }


    @OnClick({R.id.const_preview, R.id.bottomlayout})
    public void onClickPreviewPost() {

    }

    @OnClick(R.id.const_facebook)
    public void onClickFacebook() {

        if (isLogged) {
            if (hasPage) {
                showShareChooser();
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
            bundle.putSerializable("Type", AppConstants.ShareMode.SCHEDULE);
            chooserDialog.setArguments(bundle);
            chooserDialog.show(getSupportFragmentManager(), ShareCreativeChooserDialog.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share_creative, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard();
                super.onBackPressed();
                hideKeyboard();
                break;
            case R.id.action_edit_creative:
                if (file == null) {
                    showMessage("Loading Image...");
                    return false;
                }
                Intent intent = new Intent(this, EditImageActivity.class);
                intent.putExtra("imagebitmap", file.getAbsolutePath());
                startActivityForResult(intent, 123);
                break;
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
                onClickShare();
            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareIntent(String text, Uri uri) {

        if (!TextUtils.isEmpty(text)) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText(null, text));
            Toast.makeText(this, "Post description copied", Toast.LENGTH_SHORT).show();
        }

        Intent receiver = new Intent(this, ApplicationSelectorReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivity(Intent.createChooser(shareIntent, "Share Creative", pendingIntent.getIntentSender()));
        } else {
            startActivity(Intent.createChooser(shareIntent, "Share Creative"));
        }
    }

    @Override
    public void onPageSelected() {
        checkLogin();
        showShareChooser();
    }

    @Override
    public void onNoPageFound() {
        checkLogin();
        openFacebookPostDialog();
    }

    private void shareOnProfile() {
        ShareDialog shareDialog = new ShareDialog(this);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                showMessage("Post published on facebook profile");
                saveShared("facebook_profile", false, result.getPostId());
                if (marketMode != AppConstants.MarketMode.GALLERY) {
                    updateCounter();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        shareDialog.show(content);
    }

    private void openFacebookPostDialog() {
        if (getSupportFragmentManager().findFragmentByTag(FacebookPostDialog.TAG) == null) {
            facebookPostDialog = FacebookPostDialog.getInstance(null);
            facebookPostDialog.setListener(new FacebookPostDialog.ProfileShareListener() {
                @Override
                public void postOnProfile() {
                    if (marketMode == AppConstants.MarketMode.GALLERY) {
                        shareOnProfile();
                    } else {
                        checkShareLimit(1, ShareType.PROFILE);
                    }
                }
            });
            facebookPostDialog.show(getSupportFragmentManager(), FacebookPostDialog.TAG);
        }
    }

    private void facebookPostNow() {

        if (file == null) {
            showMessage("Loading Image...");
            return;
        }

        String id = AppPreferenceHelper.getInstance().getFacebookPageId();
        String accessToken1 = AppPreferenceHelper.getInstance().getFacebookPageAccessToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("source", file.getName(), requestFile);

        showLoading();
        AppApiHelper.getApiHelper()
                .postFacebook(id, editTextAbout.getText().toString().trim(), accessToken1, body)
                .enqueue(new DigiCallback<FacebookPostResponse>(this) {
                    @Override
                    public void onSuccess(FacebookPostResponse response) {

                        hideLoading();
                        showMessage("Content is being published on your page");
                        if (marketMode == AppConstants.MarketMode.CREATIVE) {
                            saveShared("facebook", true, response.getPostId());
                        } else {
                            saveShared("facebook", true, null);
                        }
                        if (marketMode != AppConstants.MarketMode.GALLERY) {
                            updateCounter();
                        }
                        setTagCount();
                    }

                    @Override
                    public void onFailure() {
                        hideLoading();
                    }
                });
    }

    private void updateCounter() {
        if (marketMode == AppConstants.MarketMode.CREATIVE) {
            AppApiHelper.getApiHelper().updateShareCount(new UpdateShareData.CreativeCount(AppPreferenceHelper.getInstance().getStoreId(),
                    1)).enqueue(new DigiCallback<SharePermissionResponse>(this) {
                @Override
                public void onSuccess(SharePermissionResponse response) {

                }

                @Override
                public void onFailure() {

                }
            });
        } else {
            AppApiHelper.getApiHelper().updateShareCount(new UpdateShareData.CustomFrameCount(AppPreferenceHelper.getInstance().getStoreId(),
                    1)).enqueue(new DigiCallback<SharePermissionResponse>(this) {
                @Override
                public void onSuccess(SharePermissionResponse response) {

                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    private void setTagCount() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OneSignal.getTags(new OneSignal.GetTagsHandler() {
                    @Override
                    public void tagsAvailable(JSONObject tags) {
                        try {
                            String frameCount = tags.optString("frame_share");
                            String premiumCount = tags.optString("premium_share");
                            Log.d(TAG, "tagsAvailable:-- " + frameCount);
                            Log.d(TAG, "tagsAvailable:-- " + premiumCount);
                            Log.d(TAG, "tagsAvailable:--tags " + tags.toString());

                            if (marketMode == AppConstants.MarketMode.FRAME) {
                                //set custom vou
                                OneSignal.sendTag("frame_share", String.valueOf(TextUtils.isEmpty(frameCount) ? 0 : Integer.parseInt(frameCount) + 1));

                            } else if (marketMode == AppConstants.MarketMode.CREATIVE) {
                                //set premium couny
                                OneSignal.sendTag("premium_share", String.valueOf(TextUtils.isEmpty(premiumCount) ? 0 : Integer.parseInt(premiumCount) + 1));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }).start();

    }

    public void schedulePost(int time) {

        if (file == null) {
            showMessage("Loading Image...");
            return;
        }

        Log.d(TAG, "schedulePost: " + time);
        String id = AppPreferenceHelper.getInstance().getFacebookPageId();
        String accessToken1 = AppPreferenceHelper.getInstance().getFacebookPageAccessToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("source", file.getName(), requestFile);

        showLoading();
        AppApiHelper.getApiHelper()
                .postScheduleFacebook(id, false, editTextAbout.getText().toString().trim(), accessToken1, time, body)
                .enqueue(new DigiCallback<FacebookPostResponse>(this) {
                    @Override
                    public void onSuccess(FacebookPostResponse response) {
                        hideLoading();
                        showMessage("Post scheduled");
                        if (marketMode == AppConstants.MarketMode.CREATIVE) {
                            saveShared("facebook", true, response.getPostId());
                        } else {
                            saveShared("facebook", true, null);
                        }
                        if (marketMode != AppConstants.MarketMode.GALLERY) {
                            updateCounter();
                        }
                        setTagCount();
                    }

                    @Override
                    public void onFailure() {
                        hideLoading();
                    }
                });
    }

    private void saveShared(String medium, boolean isFinish, String postId) {
        SharedFacebookRequest request = new SharedFacebookRequest();
        request.setBrand(String.valueOf(AppPreferenceHelper.getInstance().getBrandId()));
        request.setStoreId(AppPreferenceHelper.getInstance().getStoreId());
        request.setCreative(creativeId);
        request.setCreativeType(marketMode.name().toLowerCase());
        request.setShareMediumType(medium);
        request.setPostId(postId);
        request.setTagId(tagId);

        AppApiHelper.getApiHelper().setFacebookShared(request)
                .enqueue(new DigiCallback<ResponseBody>(this) {
                    @Override
                    public void onSuccess(ResponseBody response) {

                        if (medium.equalsIgnoreCase("facebook") || medium.equals("facebook_profile")) {

                            if (marketMode == AppConstants.MarketMode.FRAME) {
                                int count = AppPreferenceHelper.getInstance().getFramePostCountPerDay();
                                int countPerMonth = AppPreferenceHelper.getInstance().getPostCountPerMonthFrames();
                                AppPreferenceHelper.getInstance().setFramePostCountPerDay(count + 1);
                                AppPreferenceHelper.getInstance().setPostCountPerMonthFrames(countPerMonth + 1);
                            } else if (marketMode == AppConstants.MarketMode.CREATIVE) {
                                int count = AppPreferenceHelper.getInstance().getPostCountPerDay();
                                int countPerMonth = AppPreferenceHelper.getInstance().getPostCountPerMonth();
                                AppPreferenceHelper.getInstance().setPostCountPerDay(count + 1);
                                AppPreferenceHelper.getInstance().setPostCountPerMonth(countPerMonth + 1);
                            }

                        }

                        if (isFinish)
                            finish();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
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
            image = combineImages;
            imageView.setImageBitmap(Bitmap.createScaledBitmap(combineImages, 800, 600, true));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imageViewFb.setImageBitmap(Bitmap.createScaledBitmap(combineImages, 800, 600, true));
            imageViewFb.setScaleType(ImageView.ScaleType.FIT_CENTER);

            file = CommonUtils.saveImageFile(combineImages, "digisalon_share", getExternalFilesDir("Digi"));
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

    @Override
    public void onPostNow() {
        facebookPostNow();
    }

    @Override
    public void onSchedulePost(int time) {
        if (marketMode == AppConstants.MarketMode.GALLERY) {
            schedulePost(time);
        } else {
            if (AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic")) {
                openSubscribeDialog(" ", "Kindly upgrade your pack to\naccess Schedule Post");
                return;
            }
            checkShareLimit(time, ShareType.SCHEDULE);
        }
    }

    private void checkShareLimit(int time, ShareType shareType) {
        String type;
        if (marketMode == AppConstants.MarketMode.CREATIVE) {
            type = "daily_creative_count";
        } else {
            type = "daily_custom_creative_count";
        }

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
                                case PROFILE:
                                    shareOnProfile();
                                    break;
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

    private void openSubscribeDialog(String message, String upgradeMessage) {
        if (getSupportFragmentManager().findFragmentByTag(SubscribeDialog.TAG) == null) {
            Bundle bundle = new Bundle();
            bundle.putString("message", TextUtils.isEmpty(message) ? "You have exceeded your monthly limit of Facebook creatives sharing." : message);
            bundle.putString("upgradeMessage", TextUtils.isEmpty(upgradeMessage) ? "Kindly upgrade your pack now." : upgradeMessage);
            bundle.putString("title", "Upgrade Pack");
            bundle.putString("btnTitle", "Buy Now");
            SubscribeDialog subscribeDialog = SubscribeDialog.getInstance(bundle);
            subscribeDialog.setListener(new SubscribeDialog.DialogListener() {
                @Override
                public void onSubscribe() {
                    subscribeDialog.dismiss();
                    startActivity(SubscriptionActivity.getStartIntent(ShareCreativeActivity.this));
                }
            });
            subscribeDialog.show(getSupportFragmentManager(), SubscribeDialog.TAG);
        }
    }

    @Override
    public void onClickSchedule() {
        showFacebookDialog();
    }

    @Override
    public void onClickPost() {
        if (marketMode == AppConstants.MarketMode.GALLERY) {
            facebookPostNow();
        } else {
            checkShareLimit(1, ShareType.POST_NOW);
        }
    }

    private boolean canPostToday() {
        if (marketMode == AppConstants.MarketMode.FRAME) {
            return AppPreferenceHelper.getInstance().getFramePostCountPerDay() < AppPreferenceHelper.getInstance().getDailyFrameLimit();
        }

        if (marketMode == AppConstants.MarketMode.GALLERY) {
            return true;
        }

        return AppPreferenceHelper.getInstance().getPostCountPerDay() < AppPreferenceHelper.getInstance().getDailyCreativeLimit();
    }

    private boolean checkPostLimit() {

        if (marketMode == AppConstants.MarketMode.FRAME) {
            return AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic") &&
                    AppPreferenceHelper.getInstance().getPostCountPerMonthFrames() >= AppPreferenceHelper.getInstance().getMonthlyFrameLimit();
        }

        return AppPreferenceHelper.getInstance().getPackageType().equalsIgnoreCase("basic") &&
                AppPreferenceHelper.getInstance().getPostCountPerMonth() >= AppPreferenceHelper.getInstance().getMonthlyCreativeLimit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final String medium = AppPreferenceHelper.getInstance().getShareIntentMedium();

        if (!TextUtils.isEmpty(medium)) {
            AppPreferenceHelper.getInstance().setShareIntentMedium("");
            saveShared(medium, false, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPreferenceHelper.getInstance().setShareIntentMedium("");
    }

    enum ShareType {
        POST_NOW, SCHEDULE, PROFILE
    }
}