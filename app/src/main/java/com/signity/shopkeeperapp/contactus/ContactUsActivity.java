package com.signity.shopkeeperapp.contactus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemImageDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.Product.ProductImage;
import com.signity.shopkeeperapp.model.image.ImageUploadResponse;
import com.signity.shopkeeperapp.model.image.MessageResponse;
import com.signity.shopkeeperapp.model.runner.CommonResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.products.ImageBottomDialog;
import com.signity.shopkeeperapp.products.ImagesAdapter;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ContactUsActivity extends BaseActivity {

    private static final String TAG = "ContactUsActivity";
    private static final String CONTACT_US = "CONTACT_US";
    private static final int CAMERA_PERMISSION = 102;
    private static final int GALLERY_PERMISSION = 103;
    private static final int PICK_REQUEST = 101;
    private static final int REQUEST_PERMISSION = 104;
    private static final int CAMERA_REQUEST = 105;
    private Toolbar toolbar;
    private TextInputEditText textInputEditTextMobile, textInputEditTextName, textInputEditTextEmail, textInputEditTextDescription;
    private Uri cameraImageUri;
    private LinearLayout linearLayoutImage;
    private RecyclerView recyclerViewImages;
    private ImagesAdapter imagesAdapter;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> spinnerData = new ArrayList<>();
    private String reason;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ContactUsActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = getStartIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initViews();
        populateData();
        setUpToolbar();
        setUpSpinner();
        setUpAdapter();
    }

    private void populateData() {
        textInputEditTextName.setText(AppPreference.getInstance().getUserName());
        textInputEditTextMobile.setText(AppPreference.getInstance().getUserMobile());
        textInputEditTextEmail.setText(AppPreference.getInstance().getUserEmail());
    }

    private void setUpSpinner() {
        spinnerData.add("Facing issues");
        spinnerData.add("Pack Upgrade");
        spinnerData.add("Need more information");
        spinnerData.add("Others");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerData);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reason = spinnerData.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpAdapter() {
        imagesAdapter = new ImagesAdapter(this);
        recyclerViewImages.setAdapter(imagesAdapter);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewImages.addItemDecoration(new SpacesItemImageDecoration((int) Util.pxFromDp(this, 8)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textInputEditTextName = findViewById(R.id.edt_runner_name);
        textInputEditTextMobile = findViewById(R.id.edt_runner_mobile);
        textInputEditTextEmail = findViewById(R.id.edt_runner_email);
        textInputEditTextDescription = findViewById(R.id.edt_descriptionn);
        linearLayoutImage = findViewById(R.id.ll_add_products);
        recyclerViewImages = findViewById(R.id.rv_product_images);
        spinner = findViewById(R.id.spinner_area);

        linearLayoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesAdapter.getItemCount() < 4) {
                    openImageChooser();
                } else {
                    Toast.makeText(ContactUsActivity.this, "Max 4 image allowded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitContactUs() {

        String mobile = textInputEditTextMobile.getText().toString().trim();
        String name = textInputEditTextName.getText().toString().trim();
        String email = textInputEditTextEmail.getText().toString().trim();
        String message = textInputEditTextDescription.getText().toString().trim();
        List<MessageResponse> productImages = imagesAdapter.getImageList();

        if (TextUtils.isEmpty(name)) {
            textInputEditTextName.setError("Enter name");
            textInputEditTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            textInputEditTextMobile.setError("Enter mobile");
            textInputEditTextMobile.requestFocus();
            return;
        }

        if (!Util.checkValidEmail(email)) {
            textInputEditTextEmail.setError("Enter valid email");
            textInputEditTextEmail.requestFocus();
            return;
        }

        List<ProductImage> productImages1 = new ArrayList<>();
        for (MessageResponse messageResponse : productImages) {
            productImages1.add(new ProductImage(messageResponse.getUrl()));
        }

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(productImages1, new TypeToken<List<ProductImage>>() {
        }.getType());
        JsonArray jsonArrayImage = element.getAsJsonArray();

        Map<String, Object> param = new HashMap<>();
        param.put("user_id", AppPreference.getInstance().getUserId());
        param.put("name", name);
        param.put("contact_number", mobile);
        param.put("email", email);
        param.put("reason", reason);
        param.put("message", message);
        param.put("platform", Constant.PLATFORM);
        param.put("city", AppPreference.getInstance().getLocation());
        if (!productImages1.isEmpty())
            param.put("image", productImages1.get(0).getImage());

        submitContactUs(param);
    }

    private void submitContactUs(Map<String, Object> param) {
        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().contactUs(param, new Callback<CommonResponse>() {
            @Override
            public void success(CommonResponse addCategoryResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (addCategoryResponse.isSuccess()) {
                    runAnimation();
                }
                Toast.makeText(ContactUsActivity.this, addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void runAnimation() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact_us, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
            runAnimation();
        }

        if (item.getItemId() == R.id.action_submit_contact_us) {
            hideKeyboard();
            submitContactUs();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openImageChooser() {
        if (getSupportFragmentManager().findFragmentByTag(ImageBottomDialog.TAG) == null) {
            ImageBottomDialog imageBottomDialog = new ImageBottomDialog(new ImageBottomDialog.ImageListener() {
                @Override
                public void onClickGallery() {
                    getGalleryImage();
                }

                @Override
                public void onClickCamera() {
                    openCamera();
                }
            });
            imageBottomDialog.show(getSupportFragmentManager(), ImageBottomDialog.TAG);
        }
    }

    private void getGalleryImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_REQUEST);
        }
    }

    private void uploadImage(String path) {
        File file = new File(path);

        TypedFile typedFile = new TypedFile("multipart/form-data", file);

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().uploadImage(typedFile, new Callback<ImageUploadResponse>() {
            @Override
            public void success(ImageUploadResponse imageUploadResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();

                if (imageUploadResponse.isSuccess()) {
                    imagesAdapter.addImage(imageUploadResponse.getMessage());
                    if (imagesAdapter.getItemCount() > 0) {
                        recyclerViewImages.smoothScrollToPosition(imagesAdapter.getItemCount() - 1);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(ContactUsActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
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
        File fileCamera = new File(getExternalFilesDir("VauleAppz"), CONTACT_US.concat(".jpg"));
        cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", fileCamera);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
                    getGalleryImage();
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
                    uploadImage(FileUtils.getPath(this, cameraImageUri));
                    break;
                case PICK_REQUEST:
                    Uri uri = data.getData();
                    uploadImage(FileUtils.getPath(this, uri));
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.slideFromLeftAnim(this);
    }
}
