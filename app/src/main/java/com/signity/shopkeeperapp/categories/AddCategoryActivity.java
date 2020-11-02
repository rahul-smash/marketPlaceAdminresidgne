package com.signity.shopkeeperapp.categories;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.category.AddCategoryResponse;
import com.signity.shopkeeperapp.model.category.CategoryDetailResponse;
import com.signity.shopkeeperapp.model.category.SubCategoryModel;
import com.signity.shopkeeperapp.model.image.ImageUploadResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.products.AddProductActivity;
import com.signity.shopkeeperapp.products.ImageBottomDialog;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
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

/**
 * Created by ketan on 25/09/20.
 */
public class AddCategoryActivity extends BaseActivity implements SubCategoryAdapter.SubCategoryAdapterListner {

    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    private static final String TAG = "AddCategoryActivity";
    private static final int REQUEST_PERMISSION = 1001;
    private static final int REQUEST_IMAGE_GET = 2002;
    private static final int CAMERA_REQUEST = 100;
    private static final int PICK_REQUEST = 200;
    private static final int CAMERA_PERMISSION = 1000;
    private static final int GALLERY_PERMISSION = 2000;
    private static final String CATEGORY_IMAGE = "CATEGORY_IMAGE";
    private RecyclerView recyclerViewSubCategory;
    private SubCategoryAdapter subCategoryAdapter;
    private TextInputEditText textInputEditTextCategoryName;
    private ImageView imageViewCategory;
    private LinearLayout linearLayoutAddCategoryImage;
    private Toolbar toolbar;
    private List<SubCategoryModel> subCategoryModels = new ArrayList<>();
    private String categoryImageUrl;
    private ImageUploadType imageUploadType = ImageUploadType.CATEGORY;
    private int position;
    private LinearLayout linearLayoutNext;
    private ImageView imageViewDeleteImage;
    private String categoryId;
    private ActivityType activityType = ActivityType.ADD;
    private TextView textViewNext;
    private Uri cameraImageUri;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AddCategoryActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = getStartIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getExtra();
        initView();
        setUpToolbar();
        setUpAdapter();
        if (activityType == ActivityType.EDIT) {
            textViewNext.setText("Save");
            getCategoryById();
        }
    }

    private void getCategoryById() {

        ProgressDialogUtil.showProgressDialog(this);
        Map<String, String> map = new HashMap<>();
        map.put("category_id", categoryId);

        NetworkAdaper.getNetworkServices().getCategoryById(map, new Callback<CategoryDetailResponse>() {
            @Override
            public void success(CategoryDetailResponse s, Response response) {

                ProgressDialogUtil.hideProgressDialog();

                if (s.getData() == null) {
                    return;
                }

                textInputEditTextCategoryName.setText(s.getData().getTitle());

                try {
                    if (!TextUtils.isEmpty(s.getData().getImage10080())) {
                        int indexC = s.getData().getImage10080().lastIndexOf("/");
                        String nameC = s.getData().getImage10080().substring(indexC + 1);
                        categoryImageUrl = nameC;
                        imageViewDeleteImage.setVisibility(View.VISIBLE);
                        imageViewCategory.setVisibility(View.VISIBLE);
                        linearLayoutAddCategoryImage.setVisibility(View.INVISIBLE);

                        Picasso.with(AddCategoryActivity.this)
                                .load(s.getData().getImage10080())
                                .placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.addimageicon, null))
                                .into(imageViewCategory);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<SubCategoryModel> models = new ArrayList<>();
                for (SubCategory subCategory : s.getData().getSubCategory()) {
                    SubCategoryModel model = new SubCategoryModel();
                    model.setId(subCategory.getId());
                    model.setSubCategoryName(subCategory.getTitle());
                    model.setSubCategoryImageUrl(subCategory.getImage10080());
                    if (!TextUtils.isEmpty(subCategory.getImage10080())) {
                        int index = subCategory.getImage10080().lastIndexOf("/");
                        String name = subCategory.getImage10080().substring(index + 1);
                        model.setSubCategoryImage(name);
                    }
                    models.add(model);
                }

                subCategoryAdapter.setSubCategoryModels(models);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                ProgressDialogUtil.hideProgressDialog();
            }
        });

    }

    private void getExtra() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            categoryId = bundle.getString(CATEGORY_ID);
            activityType = (ActivityType) bundle.getSerializable(ACTIVITY_TYPE);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpAdapter() {
        subCategoryAdapter = new SubCategoryAdapter(this);
        subCategoryAdapter.setListener(this);
        recyclerViewSubCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSubCategory.setAdapter(subCategoryAdapter);
    }

    private void initView() {
        recyclerViewSubCategory = findViewById(R.id.rv_add_sub_category);
        textInputEditTextCategoryName = findViewById(R.id.edt_category_name);
        imageViewCategory = findViewById(R.id.iv_category);
        imageViewDeleteImage = findViewById(R.id.iv_cancel);
        linearLayoutAddCategoryImage = findViewById(R.id.ll_add_category_image);
        toolbar = findViewById(R.id.toolbar);
        linearLayoutNext = findViewById(R.id.ll_next);
        textViewNext = findViewById(R.id.tv_next);

        linearLayoutAddCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUploadType = ImageUploadType.CATEGORY;
                openImageChooser();
            }
        });

        linearLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
            }
        });

        imageViewDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryImageUrl = null;
                imageViewCategory.setVisibility(View.GONE);
                imageViewDeleteImage.setVisibility(View.GONE);
                imageViewCategory.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.addimageicon, null));
                linearLayoutAddCategoryImage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveCategory() {
        String categoryName = textInputEditTextCategoryName.getText().toString().trim();

        if (TextUtils.isEmpty(categoryName)) {
            textInputEditTextCategoryName.requestFocus();
            textInputEditTextCategoryName.setError("Please add category name");
            return;
        }

        if (TextUtils.isEmpty(categoryImageUrl)) {
            Toast.makeText(this, "Please add category image", Toast.LENGTH_SHORT).show();
            return;
        }

        List<SubCategoryModel> subCategoryModel = subCategoryAdapter.getSubCategoryModels();

        if (subCategoryModel.size() < 1) {
            Toast.makeText(this, "Please add subcategory", Toast.LENGTH_SHORT).show();
            return;
        }

        for (SubCategoryModel model : subCategoryModel) {
            if (TextUtils.isEmpty(model.getSubCategoryName())) {
                Toast.makeText(this, "Please add subcategory", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(model.getSubCategoryImage())) {
                Toast.makeText(this, "Please add subcategory image", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(subCategoryAdapter.getSubCategoryModels(), new TypeToken<List<SubCategoryModel>>() {
        }.getType());
        JsonArray jsonArray = element.getAsJsonArray();

        Map<String, String> map = new HashMap<>();
        map.put("title", categoryName);
        map.put("image", categoryImageUrl);
        map.put("subCate", jsonArray.toString());
        if (activityType == ActivityType.EDIT) {
            map.put("category_id", categoryId);
            ProgressDialogUtil.showProgressDialog(this);
            NetworkAdaper.getNetworkServices().editCategory(map, new Callback<AddCategoryResponse>() {
                @Override
                public void success(AddCategoryResponse addCategoryResponse, Response response) {
                    if (isDestroyed()) {
                        return;
                    }
                    ProgressDialogUtil.hideProgressDialog();
                    if (addCategoryResponse.isSuccess()) {
                        finish();
                    } else {
                        Toast.makeText(AddCategoryActivity.this, addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    if (isDestroyed()) {
                        return;
                    }
                    ProgressDialogUtil.hideProgressDialog();
                    Toast.makeText(AddCategoryActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().addCategory(map, new Callback<AddCategoryResponse>() {
            @Override
            public void success(AddCategoryResponse addCategoryResponse, Response response) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                if (addCategoryResponse.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AddProductActivity.CATEGORY_ID, addCategoryResponse.getData().getParentId());
                    startActivity(AddProductActivity.getStartIntent(AddCategoryActivity.this, bundle));
                    finish();
                } else {
                    Toast.makeText(AddCategoryActivity.this, addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(AddCategoryActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
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
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    cropImage(cameraImageUri);
                    break;
                case PICK_REQUEST:

                    if (data == null || data.getData() == null) {
                        return;
                    }

                    Uri uri = data.getData();
                    cropImage(uri);
                    break;
                case UCrop.REQUEST_CROP:

                    if (data == null) {
                        return;
                    }

                    final Uri resultUri = UCrop.getOutput(data);
                    uploadImage(FileUtils.getPath(this, resultUri));
                    break;
            }
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
                    switch (imageUploadType) {
                        case CATEGORY:
                            if (imageViewCategory != null) {
                                categoryImageUrl = imageUploadResponse.getMessage().getUrl();
                                String imageUrl = imageUploadResponse.getMessage().getImageUrl();
                                imageViewDeleteImage.setVisibility(View.VISIBLE);
                                imageViewCategory.setVisibility(View.VISIBLE);
                                linearLayoutAddCategoryImage.setVisibility(View.INVISIBLE);
                                Picasso.with(AddCategoryActivity.this)
                                        .load(imageUrl)
                                        .placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.addimageicon, null))
                                        .into(imageViewCategory);
                            }
                            break;
                        case SUBCATEGORY:
                            String subCategoryImageUrl = imageUploadResponse.getMessage().getUrl();
                            String imageUrl = imageUploadResponse.getMessage().getImageUrl();
                            SubCategoryModel subCategoryModel = subCategoryAdapter.getSubCategoryModels().get(position);
                            subCategoryModel.setSubCategoryImage(subCategoryImageUrl);
                            subCategoryModel.setSubCategoryImageUrl(imageUrl);
                            subCategoryAdapter.notifyItemChanged(position, subCategoryModel);
                            break;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(AddCategoryActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
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

    public void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cameraIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION);
        }
    }

    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileCamera = new File(getExternalFilesDir("VauleAppz"), CATEGORY_IMAGE.concat(".jpg"));
        cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", fileCamera);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void cropImage(Uri uri) {
        File fileCamera = new File(getExternalFilesDir("VauleAppz"), CATEGORY_IMAGE.concat("out.jpg"));
        Uri outCamera = Uri.fromFile(fileCamera);
        UCrop.of(uri, outCamera)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .start(this);
    }

    private void runAnimation() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            runAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddImage(int position) {
        imageUploadType = ImageUploadType.SUBCATEGORY;
        this.position = position;
        openImageChooser();
    }

    private enum ImageUploadType {
        CATEGORY, SUBCATEGORY
    }

    public enum ActivityType {
        EDIT, ADD;
    }

}