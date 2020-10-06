package com.signity.shopkeeperapp.products;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.categories.SubCategoryAdapter;
import com.signity.shopkeeperapp.util.AnimUtil;

/**
 * Created by ketan on 25/09/20.
 */
public class AddProductActivity extends AppCompatActivity implements SubCategoryAdapter.SubCategoryAdapterListner {

    private static final String TAG = "AddProductActivity";
    private static final int REQUEST_PERMISSION = 1001;
    private static final int REQUEST_IMAGE_GET = 2002;
    private static final int REQUEST_VARIANT = 3003;
    private Toolbar toolbar;
    private TextInputEditText editTextCategory;
    private TextInputEditText editTextSubCategory;
    private TextInputEditText editTextProductName;
    private TextInputEditText editTextDescription;
    private TextInputEditText editTextTag;
    private TextInputEditText editTextTax;
    private TextInputEditText editTextWeight;
    private TextInputEditText editTextUnitType;
    private TextInputEditText editTextMRP;
    private TextInputEditText editTextSellingPrice;
    private TextInputEditText editTextDiscount;
    private RadioGroup radioGroupVegNonVeg;
    private CheckBox checkBoxDisplayVegNonVeg;
    private Switch switchInEx;
    private LinearLayout linearLayoutImage;
    private LinearLayout linearLayoutVariant;
    private RecyclerView recyclerViewVariant;
    private VariantAdapter variantAdapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AddProductActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
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
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        editTextCategory = findViewById(R.id.edt_category);
        editTextSubCategory = findViewById(R.id.edt_sub_category);
        editTextProductName = findViewById(R.id.edt_product_name);
        editTextDescription = findViewById(R.id.edt_descriptionn);
        editTextTag = findViewById(R.id.edt_add_tag);
        editTextTax = findViewById(R.id.edt_tax);
        editTextWeight = findViewById(R.id.edt_weight);
        editTextUnitType = findViewById(R.id.edt_unit);
        editTextMRP = findViewById(R.id.edt_mrp);
        editTextDiscount = findViewById(R.id.edt_discount);
        editTextSellingPrice = findViewById(R.id.edt_selling_price);
        checkBoxDisplayVegNonVeg = findViewById(R.id.cb_display);
        radioGroupVegNonVeg = findViewById(R.id.rg_veg_nonveg);
        switchInEx = findViewById(R.id.switch_in_ex);
        linearLayoutImage = findViewById(R.id.ll_add_products);
        linearLayoutVariant = findViewById(R.id.ll_add_variant);
        recyclerViewVariant = findViewById(R.id.rv_variant);

        editTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddProductActivity.this, "Category", Toast.LENGTH_SHORT).show();
                CategoryDialog categoryDialog = CategoryDialog.getInstance(null);
                categoryDialog.show(getSupportFragmentManager(), CategoryDialog.TAG);
            }
        });

        editTextSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddProductActivity.this, "Sub Category", Toast.LENGTH_SHORT).show();
                CategoryDialog categoryDialog = CategoryDialog.getInstance(null);
                categoryDialog.show(getSupportFragmentManager(), CategoryDialog.TAG);
            }
        });

        linearLayoutVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(VariantActivity.getStartIntent(AddProductActivity.this), REQUEST_VARIANT);
                AnimUtil.slideFromRightAnim(AddProductActivity.this);
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