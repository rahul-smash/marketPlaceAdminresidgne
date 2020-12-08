package com.signity.shopkeeperapp.market;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.products.ImageBottomDialog;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.Util;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;

import java.io.File;

/**
 * Created by Ketan Tetry on 20/1/20.
 */
public class CustomFrameActivity extends BaseActivity implements ImageBottomDialog.ImageListener {

    private static final String TAG = "CustomFrameActivity";
    private static final int CAMERA_REQUEST = 0223;
    private static final int PICK_REQUEST = 0210;
    private static final String FINAL_IMAGE = "final_image";
    private static final String FRAME_IMAGE = "frame_image";
    private static final int CAMERA_PERMISSION = 02230;
    private static final int GALLERY_PERMISSION = 02100;
    private static final int TARGET_HEIGHT = 900;
    private static final int TARGET_WIDTH = 1200;
    private Toolbar toolbar;
    private CustomUCropView uCropView;
    private ImageView imageViewFrame;
    private ImageView imageViewAddImage;
    private ConstraintLayout frameLayout;
    private View blockingView;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;

    private String url, desc, title, creativeId;

    private CustomGestureCropImageView gestureCropImageView;
    private OverlayView overlayView;
    private TranslateAnimation translateAnimation;
    private Animation fadeInAnim, fadeOutAnim;

    private Uri frameImageUri, finalImageUri;

    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {

        }

        @Override
        public void onScale(float currentScale) {

        }

        @Override
        public void onLoadComplete() {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.1f);
            alphaAnimation.setDuration(100);
            gestureCropImageView.startAnimation(alphaAnimation);
            blockingView.setClickable(false);
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            e.printStackTrace();
            finish();
        }

    };

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CustomFrameActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * Scale Bitmap only if image size is smaller than frame size
     *
     * @param bitmap
     * @param wantedWidth
     * @param wantedHeight
     * @return Custom sized bitmap
     */
    private static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_frame);
        initViews();
        setUp();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        imageViewFrame = findViewById(R.id.iv_frame);
        imageViewAddImage = findViewById(R.id.iv_add_image);
        uCropView = findViewById(R.id.custom_crop_view);
        blockingView = findViewById(R.id.v_blocking);
        constraintLayout = findViewById(R.id.const_buttons);
//        frameLayout = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.ll_adjust_);

        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddImage();
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });

        findViewById(R.id.ll_change_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeImage();
            }
        });

        findViewById(R.id.ll_save_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveFrame();
            }
        });
    }

    protected void setUp() {
        getExtras();
        setUpToolbar();
        setUpAnimator();
        createReset();
        getImageApi();

        // This is image is no longer needed as we are now capturing bitmap from view
        finalImageUri = Uri.fromFile(new File(getExternalFilesDir("ValueAppz"), FINAL_IMAGE.concat(".jpg")));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (linearLayout.getVisibility() == View.VISIBLE) {
                fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        linearLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                linearLayout.startAnimation(fadeOutAnim);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setUpAnimator() {
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.75f, Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(200);
        translateAnimation.setFillEnabled(true);
    }

    private void getExtras() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            url = getIntent().getExtras().getString("url");
            desc = getIntent().getExtras().getString("desc");
            title = getIntent().getExtras().getString("title");
            creativeId = getIntent().getExtras().getString("creativeId");
        }

    }

    private void getImageApi() {

        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageViewAddImage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imageViewFrame);
        } else {
            imageViewFrame.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        }

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void createReset() {
        blockingView.setClickable(true);

        uCropView.resetCropImageView();

        gestureCropImageView = uCropView.getCropImageView();
        gestureCropImageView.setTargetAspectRatio((float) 4 / 3);
        gestureCropImageView.setTransformImageListener(mImageListener);

        overlayView = uCropView.getOverlayView();

        overlayView.setShowCropFrame(false);
        overlayView.setShowCropGrid(false);
        overlayView.setDimmedColor(Color.BLACK);
    }

    /**
     * @param view Main layout view
     * @return Bitmap image of view
     */
    private Bitmap loadBitmapFromView(View view) {
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return b;
    }

    public void onSave() {

        // Get full view of the custom image
        Bitmap fullViewBitmap = loadBitmapFromView(gestureCropImageView);

        // Crop the image as frame
        Bitmap croppedBitmap = Bitmap.createBitmap(fullViewBitmap,
                (int) overlayView.getCropViewRect().left,
                (int) overlayView.getCropViewRect().top,
                (int) overlayView.getCropViewRect().width(),
                (int) overlayView.getCropViewRect().height());

        // Scale image to fit the frame
        Bitmap scaledBitmap = scaleBitmap(croppedBitmap, TARGET_WIDTH, TARGET_HEIGHT);

        // Generating frame image for ImageView
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewFrame.getDrawable();
//        File frameFile = CommonUtils.saveImageFilePng(bitmapDrawable.getBitmap(), "new_frame", getExternalFilesDir("Digi"));

        // Generate frame for fixed size
//        Bitmap frameBitmap = CommonUtils.decodeBitmap(frameFile, TARGET_WIDTH, TARGET_HEIGHT);
        Bitmap frameBitmap = scaleBitmap(bitmapDrawable.getBitmap(), TARGET_WIDTH, TARGET_HEIGHT);

        Bitmap customFrameBitmap = mergeBitmaps(scaledBitmap, frameBitmap);

        Util.saveImageFilePng(customFrameBitmap, "CustomFrame", getExternalFilesDir("ValueAppz"));

        // open share
        Intent intent = ShareCreativeActivity.getStartIntent(this);
        intent.putExtra("url", "");
        intent.putExtra("desc", desc);
        intent.putExtra("title", title);
        intent.putExtra("creativeId", String.valueOf(creativeId));
        intent.putExtra("isShared", false);
        intent.putExtra(CreativeFragment.MARKET_MODE, Constant.MarketMode.FRAME);
        startActivity(intent);
        finish();
    }

    private Bitmap mergeBitmaps(Bitmap background, Bitmap foreground) {
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(TARGET_WIDTH, TARGET_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, 0, 0, null);
        return bitmap;
    }

    public void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cameraIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION);
        }
    }

    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileCamera = new File(getExternalFilesDir("ValueAppz"), FRAME_IMAGE.concat(".jpg"));
        frameImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", fileCamera);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, frameImageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            galleryIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION);
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
    }

    public void onClickAddImage() {
        if (getSupportFragmentManager().findFragmentByTag(ImageBottomDialog.TAG) == null) {
            ImageBottomDialog imageBottomDialog = new ImageBottomDialog(this);
            imageBottomDialog.show(getSupportFragmentManager(), ImageBottomDialog.TAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case CAMERA_PERMISSION:
                    cameraIntent();
                    break;
                case GALLERY_PERMISSION:
                    galleryIntent();
                    break;
            }
        } else {
            Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    updateNewImage(frameImageUri);
                    break;
                case PICK_REQUEST:
                    Uri uri = data.getData();
                    updateNewImage(uri);
                    break;
            }
        }
    }

    private void updateNewImage(Uri uri) {

        createReset();
        showButtons();
        blockingView.setClickable(false);
        imageViewAddImage.setVisibility(View.GONE);

        try {
            gestureCropImageView.setImageUri(uri, finalImageUri);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void showButtons() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (constraintLayout.getVisibility() == View.GONE) {
                    constraintLayout.setVisibility(View.VISIBLE);
                    constraintLayout.startAnimation(translateAnimation);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout.startAnimation(fadeInAnim);
                }
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close_filter:
            case android.R.id.home:
                super.onBackPressed();
                hideKeyboard();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public void onClickGallery() {
        openGallery();
    }

    @Override
    public void onClickCamera() {
        openCamera();
    }

    public void onChangeImage() {
        onClickAddImage();
    }

    public void onSaveFrame() {
        onSave();
    }
}
