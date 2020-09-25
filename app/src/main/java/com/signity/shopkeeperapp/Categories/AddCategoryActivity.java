package com.signity.shopkeeperapp.Categories;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.model.category.SubCategoryModel;
import com.signity.shopkeeperapp.util.AnimUtil;

import java.util.ArrayList;
import java.util.List;

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

        linearLayoutAddCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGalleryImage();
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
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GET) {
            if (data == null) {
                return;
            }
        }
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
        getGalleryImage();
    }
}