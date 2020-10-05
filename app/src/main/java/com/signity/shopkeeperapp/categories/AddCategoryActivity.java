package com.signity.shopkeeperapp.categories;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.category.AddCategoryResponse;
import com.signity.shopkeeperapp.model.category.SubCategoryModel;
import com.signity.shopkeeperapp.model.image.ImageUploadResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
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
public class AddCategoryActivity extends AppCompatActivity implements SubCategoryAdapter.SubCategoryAdapterListner {

    private static final String TAG = "AddCategoryActivity";
    private static final int REQUEST_PERMISSION = 1001;
    private static final int REQUEST_IMAGE_GET = 2002;
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

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AddCategoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        initView();
        setUpToolbar();
        setUpAdapter();
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
        linearLayoutAddCategoryImage = findViewById(R.id.ll_add_category_image);
        toolbar = findViewById(R.id.toolbar);
        linearLayoutNext = findViewById(R.id.ll_next);

        linearLayoutAddCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUploadType = ImageUploadType.CATEGORY;
                getGalleryImage();
            }
        });

        linearLayoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
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

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().addCategory(map, new Callback<AddCategoryResponse>() {
            @Override
            public void success(AddCategoryResponse addCategoryResponse, Response response) {

                ProgressDialogUtil.hideProgressDialog();
                if (addCategoryResponse.isSuccess()) {
                    // TODO - Finish this activity and open add product
                } else {
                    Toast.makeText(AddCategoryActivity.this, addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_GET:

                    if (data == null) {
                        return;
                    }

                    File fileLogo = new File(getExternalFilesDir("ValueAppz"), "category.png");
                    Uri output = Uri.fromFile(fileLogo);

                    Uri fullPhotoUri = data.getData();

                    if (fullPhotoUri == null) {
                        return;
                    }

                    UCrop.of(fullPhotoUri, output)
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(500, 500)
                            .start(this);
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
                ProgressDialogUtil.hideProgressDialog();

                if (imageUploadResponse.isSuccess()) {
                    categoryImageUrl = imageUploadResponse.getMessage().getUrl();
                    String imageUrl = String.format("https://s3.amazonaws.com/store-asset/%s", categoryImageUrl);

                    switch (imageUploadType) {
                        case CATEGORY:
                            if (imageViewCategory != null) {
                                imageViewCategory.setVisibility(View.VISIBLE);
                                Picasso.with(AddCategoryActivity.this)
                                        .load(imageUrl)
                                        .into(imageViewCategory);
                            }
                            break;
                        case SUBCATEGORY:
                            SubCategoryModel subCategoryModel = subCategoryAdapter.getSubCategoryModels().get(position);
                            subCategoryModel.setSubCategoryImage(categoryImageUrl);
                            subCategoryAdapter.notifyItemChanged(position);
                            break;
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
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
        getGalleryImage();
    }

    private enum ImageUploadType {
        CATEGORY, SUBCATEGORY
    }

}