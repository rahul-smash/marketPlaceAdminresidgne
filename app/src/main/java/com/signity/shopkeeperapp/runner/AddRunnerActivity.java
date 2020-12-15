package com.signity.shopkeeperapp.runner;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.customers.AreaCodesResp;
import com.signity.shopkeeperapp.model.customers.DataResp;
import com.signity.shopkeeperapp.model.image.ImageUploadResponse;
import com.signity.shopkeeperapp.model.runner.AddRunnerApiResponse;
import com.signity.shopkeeperapp.model.runner.AreaResponse;
import com.signity.shopkeeperapp.model.runner.AreaResponseData;
import com.signity.shopkeeperapp.model.runner.DataResponse;
import com.signity.shopkeeperapp.model.runner.RunnerDetailResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.products.ImageBottomDialog;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class AddRunnerActivity extends BaseActivity {

    public static final String RUNNER_ID = "RUNNER_ID";
    private static final String TAG = "AddRunnerActivity";
    private static final String RUNNER_IMAGE = "RUNNER_IMAGE";
    private static final int CAMERA_PERMISSION = 102;
    private static final int GALLERY_PERMISSION = 103;
    private static final int PICK_REQUEST = 101;
    private static final int REQUEST_PERMISSION = 104;
    private static final int CAMERA_REQUEST = 105;
    private Toolbar toolbar;
    private TextInputEditText textInputEditTextMobile, textInputEditTextName, textInputEditTextEmail;
    private String areaId;
    private List<AreaResponseData> areaList = new ArrayList<>();
    private List<DataResp> dataResps = new ArrayList<>();
    private String profileImage = "";
    private Uri cameraImageUri;
    private CircleImageView imageViewRunner;
    private String runnerId;
    private RecyclerView recyclerView;
    private AreaAdapter adapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AddRunnerActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = getStartIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_runner);
        getExtra();
        initViews();
        setUpToolbar();
        setUpAdapter();
        getAreaCodes();
    }

    private void setUpAdapter() {
        adapter = new AreaAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void getRunnerDetail() {

        if (TextUtils.isEmpty(runnerId)) {
            ProgressDialogUtil.hideProgressDialog();
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("runner_id", runnerId);

        NetworkAdaper.getNetworkServices().getRunnerById(param, new Callback<RunnerDetailResponse>() {
            @Override
            public void success(RunnerDetailResponse customerDataResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();
                if (isDestroyed()) {
                    return;
                }

                if (customerDataResponse.isSuccess()) {
                    if (customerDataResponse.getData() != null && !customerDataResponse.getData().isEmpty()) {
                        populateData(customerDataResponse.getData().get(0));
                    }
                } else {
                    Toast.makeText(AddRunnerActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                ProgressDialogUtil.hideProgressDialog();
                if (!isDestroyed()) {
                    Toast.makeText(AddRunnerActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateData(DataResponse dataResponse) {
        textInputEditTextName.setText(dataResponse.getFullName());
        textInputEditTextMobile.setText(dataResponse.getPhone());
        textInputEditTextEmail.setText(dataResponse.getEmail());

        try {
            if (!TextUtils.isEmpty(dataResponse.getProfileImage())) {
                int index = dataResponse.getProfileImage().lastIndexOf("/");
                profileImage = dataResponse.getProfileImage().substring(index + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setImage(dataResponse.getProfileImage10080());

        if (dataResponse.getArea() != null && !dataResponse.getArea().isEmpty()) {
            areaList.clear();
            for (DataResp resp : dataResps) {
                AreaResponseData data = new AreaResponseData();
                data.setArea(resp.getName());
                data.setAreaId(resp.getId());
                for (AreaResponse areaResponse : dataResponse.getArea()) {
                    if (resp.getId().equals(areaResponse.getId())) {
                        data.setChecked(true);
                        break;
                    }
                }
                areaList.add(data);
            }
            adapter.setAreaList(areaList);
        }
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            runnerId = bundle.getString(RUNNER_ID);
        }
    }

    private void getAreaCodes() {
        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().getAreaCodes(new Callback<AreaCodesResp>() {
            @Override
            public void success(AreaCodesResp areaCodesResp, Response response) {

                if (isDestroyed()) {
                    return;
                }
                areaList.clear();
                if (areaCodesResp.isSuccess()) {
                    dataResps = areaCodesResp.getData();
                    for (DataResp resp : dataResps) {
                        AreaResponseData data = new AreaResponseData();
                        data.setChecked(false);
                        data.setAreaId(resp.getId());
                        data.setArea(resp.getName());
                        areaList.add(data);
                    }
                    adapter.setAreaList(areaList);
                }
                getRunnerDetail();
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

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        textInputEditTextName = findViewById(R.id.edt_runner_name);
        textInputEditTextMobile = findViewById(R.id.edt_runner_mobile);
        textInputEditTextEmail = findViewById(R.id.edt_runner_email);
        imageViewRunner = findViewById(R.id.iv_runner);
        recyclerView = findViewById(R.id.rv_add_runner);

        imageViewRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
    }

    private void saveRunner() {

        String mobile = textInputEditTextMobile.getText().toString().trim();
        String name = textInputEditTextName.getText().toString().trim();
        String email = textInputEditTextEmail.getText().toString().trim();
        List<String> areas = adapter.getSelectedArea();

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

        if (areas.isEmpty()) {
            Toast.makeText(this, "Select Area", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("id", runnerId);
        param.put("full_name", name);
        param.put("mobile", mobile);
        param.put("email", email);
        param.put("profile_image", profileImage);
        param.put("area_id", TextUtils.join(",", areas));

        ProgressDialogUtil.showProgressDialog(this);
        saveRunner(param);
    }

    private void saveRunner(Map<String, Object> param) {
        NetworkAdaper.getNetworkServices().addRunner(param, new Callback<AddRunnerApiResponse>() {
            @Override
            public void success(AddRunnerApiResponse addCategoryResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (addCategoryResponse.isSuccess()) {
                    runAnimation();
                }
                Toast.makeText(AddRunnerActivity.this, addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();

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
        inflater.inflate(R.menu.menu_add_runner, menu);

        if (!TextUtils.isEmpty(runnerId)) {
            menu.getItem(0).setTitle("Update");
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
            runAnimation();
        }

        if (item.getItemId() == R.id.action_save_runner) {
            hideKeyboard();
            saveRunner();
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
                    profileImage = imageUploadResponse.getMessage().getUrl();
                    setImage(imageUploadResponse.getMessage().getImageUrl());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(AddRunnerActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setImage(String image) {
        if (!TextUtils.isEmpty(image)) {
            Picasso.with(AddRunnerActivity.this)
                    .load(image)
                    .placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.all_customers, null))
                    .into(imageViewRunner);
        }
    }

    public void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cameraIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION);
        }
    }

    private void cropImage(Uri uri) {
        File fileCamera = new File(getExternalFilesDir("VauleAppz"), RUNNER_IMAGE.concat("out.jpg"));
        Uri outCamera = Uri.fromFile(fileCamera);
        UCrop.of(uri, outCamera)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .start(this);
    }

    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileCamera = new File(getExternalFilesDir("VauleAppz"), RUNNER_IMAGE.concat(".jpg"));
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
                    cropImage(cameraImageUri);
                    break;
                case PICK_REQUEST:
                    Uri uri = data.getData();
                    cropImage(uri);
                    break;
                case UCrop.REQUEST_CROP:
                    final Uri resultUri = UCrop.getOutput(data);
                    uploadImage(FileUtils.getPath(this, resultUri));
                    break;
            }
        }
    }
}
