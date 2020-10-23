package com.signity.shopkeeperapp.products;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.adapter.SpacesItemImageDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.model.Categories.GetCategoryData;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.Categories.SubCategory;
import com.signity.shopkeeperapp.model.CategoryStatus.CategoryStatus;
import com.signity.shopkeeperapp.model.Product.DynamicField;
import com.signity.shopkeeperapp.model.Product.ImageObject;
import com.signity.shopkeeperapp.model.Product.ProductDetail;
import com.signity.shopkeeperapp.model.Product.ProductDetailResponse;
import com.signity.shopkeeperapp.model.Product.ProductImage;
import com.signity.shopkeeperapp.model.Product.StoreAttributes;
import com.signity.shopkeeperapp.model.image.ImageUploadResponse;
import com.signity.shopkeeperapp.model.image.MessageResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
public class AddProductActivity extends BaseActivity implements SubCategoryDialog.SubCategoryListener, CategoryDialog.CategoryListener {

    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String PRODUCT_ID = "PRODUCT_ID";
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
    private AppCompatSpinner appCompatSpinnerUnitType;
    private TextInputEditText editTextMRP;
    private TextInputEditText editTextSellingPrice;
    private TextInputEditText editTextDiscount;
    private RadioGroup radioGroupVegNonVeg;
    private CheckBox checkBoxDisplayVegNonVeg;
    private Switch switchInEx;
    private LinearLayout linearLayoutImage;
    private LinearLayout linearLayoutVariant;
    private RecyclerView recyclerViewVariant;
    private RecyclerView recyclerViewImages;
    private RecyclerView recyclerViewDynamicField;
    private ConstraintLayout constraintLayoutTags;
    private TextView textViewShowMoreLess;
    private VariantAdapter variantAdapter;
    private ImagesAdapter imagesAdapter;
    private List<GetCategoryData> categoryDataList = new ArrayList<>();
    private String selectedCategoryId = "";
    private String selectedSubCategoryId;
    private List<String> unitList = new ArrayList<>(Arrays.asList("Kg", "gram", "Ltr", "ml"));
    private String unitType;
    private String foodType;
    private List<Map<String, String>> variantList = new ArrayList<>();
    private List<DynamicField> dynamicFieldList = new ArrayList<>();
    private DynamicFieldAdapter dynamicFieldAdapter;
    private JSONArray jsonArrayVariant = new JSONArray();
    private ProductDetail productData;
    private String productId;

    // TODO - Get Product Details from API and fill the data
    // TODO - update the product
    // TODO - Refresh the page as in Orders Listing

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AddProductActivity.class);
    }

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = getStartIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getExtra();
        initView();
        setUpToolbar();
        setUpAdapter();
        setUpSpinner();
        getCategoriesApi();
    }

    private void getProductById() {

        if (TextUtils.isEmpty(productId)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("product_id", productId);

        NetworkAdaper.getNetworkServices().getProductById(map, new Callback<ProductDetailResponse>() {
            @Override
            public void success(ProductDetailResponse productDetailResponse, Response response) {

                if (productDetailResponse.isSuccess()) {
                    productData = productDetailResponse.getProductData();
                    populateProductData();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void populateProductData() {
        if (productData == null) {
            return;
        }

        setImagesData();
        setCategorySubCategory();
        setProductDetails();

        if (productData.getVariants() != null && productData.getVariants().size() > 0) {
            dynamicFieldAdapter.setFieldMap(productData.getVariants().get(0));
            variantAdapter.setVariantList(productData.getVariants());
        }

    }

    private void setProductDetails() {
        editTextProductName.setText(productData.getTitle());
        editTextDescription.setText(productData.getDescription());
        editTextTag.setText(productData.getTags());
        editTextTax.setText(productData.getGstTaxRate());
        switchInEx.setChecked(!productData.getGstTaxType().equalsIgnoreCase("inclusive"));
    }

    private void setCategorySubCategory() {
        selectedCategoryId = productData.getCategoryParentId();
        selectedSubCategoryId = productData.getCategoryIds();

        for (GetCategoryData categoryData : categoryDataList) {
            if (categoryData.getId().equals(selectedCategoryId)) {
                editTextCategory.setText(categoryData.getTitle());
                for (SubCategory category : categoryData.getSubCategory()) {
                    if (category.getId().equals(selectedSubCategoryId)) {
                        editTextSubCategory.setText(category.getTitle());
                    }
                }
            }
        }
    }

    private void setImagesData() {
        List<MessageResponse> messageResponses = new ArrayList<>();
        for (ImageObject imageObject : productData.getImageList()) {

            if (TextUtils.isEmpty(imageObject.getImage10080())) {
                continue;
            }

            int index = imageObject.getImage10080().lastIndexOf("/");
            String imageName = imageObject.getImage10080().substring(index + 1);

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setImageUrl(imageObject.getImage10080());
            messageResponse.setUrl(imageName);

            messageResponses.add(messageResponse);
        }

        imagesAdapter.setImageList(messageResponses);
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedCategoryId = bundle.getString(CATEGORY_ID);
            productId = bundle.getString(PRODUCT_ID);
        }
    }

    private void getStoreAttributes() {
        NetworkAdaper.getNetworkServices().getStoreAttributes(new Callback<StoreAttributes>() {
            @Override
            public void success(StoreAttributes storeAttributes, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (storeAttributes.isSuccess()) {
                    if (storeAttributes.getData() != null) {
                        dynamicFieldList = storeAttributes.getData().getDynamicFields();
                        dynamicFieldAdapter.setDynamicFieldList(storeAttributes.getData().getDynamicFields());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(AddProductActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpSpinner() {
        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, unitList);
        appCompatSpinnerUnitType.setAdapter(stringArrayAdapter);
        appCompatSpinnerUnitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitType = stringArrayAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void setUpAdapter() {
        variantAdapter = new VariantAdapter(this);
        recyclerViewVariant.setAdapter(variantAdapter);
        recyclerViewVariant.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVariant.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 16)));

        imagesAdapter = new ImagesAdapter(this);
        recyclerViewImages.setAdapter(imagesAdapter);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewImages.addItemDecoration(new SpacesItemImageDecoration((int) Util.pxFromDp(this, 8)));

        dynamicFieldAdapter = new DynamicFieldAdapter(this);
        recyclerViewDynamicField.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewDynamicField.setAdapter(dynamicFieldAdapter);
        recyclerViewDynamicField.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = (int) Util.pxFromDp(AddProductActivity.this, 8);
                outRect.top = 0;
            }
        });
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
        appCompatSpinnerUnitType = findViewById(R.id.spinner_unit_type);
        editTextMRP = findViewById(R.id.edt_mrp);
        editTextDiscount = findViewById(R.id.edt_discount);
        editTextSellingPrice = findViewById(R.id.edt_selling_price);
        checkBoxDisplayVegNonVeg = findViewById(R.id.cb_display);
        radioGroupVegNonVeg = findViewById(R.id.rg_veg_nonveg);
        switchInEx = findViewById(R.id.switch_in_ex);
        linearLayoutImage = findViewById(R.id.ll_add_products);
        linearLayoutVariant = findViewById(R.id.ll_add_variant);
        recyclerViewVariant = findViewById(R.id.rv_variant);
        recyclerViewImages = findViewById(R.id.rv_product_images);
        recyclerViewDynamicField = findViewById(R.id.rv_dynamic_fields);
        textViewShowMoreLess = findViewById(R.id.tv_show_less_more);
        constraintLayoutTags = findViewById(R.id.const_tags);

        textViewShowMoreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutTags.setVisibility(
                        constraintLayoutTags.getVisibility() == View.VISIBLE ?
                                View.GONE :
                                View.VISIBLE
                );
                textViewShowMoreLess.setText(constraintLayoutTags.getVisibility() == View.VISIBLE ? "Show Less" : "Show More");
            }
        });

        editTextCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedSubCategoryId = null;
                editTextSubCategory.setText(null);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(CategoryDialog.CATEGORY_DATA, (ArrayList<? extends Parcelable>) categoryDataList);
                CategoryDialog categoryDialog = CategoryDialog.getInstance(bundle);
                categoryDialog.show(getSupportFragmentManager(), CategoryDialog.TAG);
            }
        });

        editTextSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(selectedCategoryId)) {
                    return;
                }

                List<SubCategory> subCategories = new ArrayList<>();
                for (GetCategoryData categoryData : categoryDataList) {
                    if (categoryData.getId().equals(selectedCategoryId)) {
                        subCategories.addAll(categoryData.getSubCategory());
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SubCategoryDialog.SUBCATEGORY_DATA, (ArrayList<? extends Parcelable>) subCategories);
                SubCategoryDialog subCategoryDialog = SubCategoryDialog.getInstance(bundle);
                subCategoryDialog.show(getSupportFragmentManager(), SubCategoryDialog.TAG);
            }
        });

        linearLayoutVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(VariantActivity.getStartIntent(AddProductActivity.this), REQUEST_VARIANT);
                AnimUtil.slideFromRightAnim(AddProductActivity.this);
            }
        });

        checkBoxDisplayVegNonVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radioGroupVegNonVeg.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        radioGroupVegNonVeg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mrb_non_veg:
                        foodType = "non-veg";
                        break;
                    case R.id.mrb_veg:
                        foodType = "veg";
                        break;
                }
            }
        });

        editTextDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }

                String mrp = editTextMRP.getText().toString();

                if (TextUtils.isEmpty(mrp)) {
                    mrp = "0";
                }

                double doubleMrp = Double.parseDouble(mrp);
                double discount = Double.parseDouble(s.toString());

                if (discount < 0 || discount > 100) {
                    Toast.makeText(AddProductActivity.this, "Invalid discount value", Toast.LENGTH_SHORT).show();
                    return;
                }

                final double price = doubleMrp - (doubleMrp * (discount / 100));
                editTextSellingPrice.setText(String.format("%.2f", price));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        linearLayoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesAdapter.getItemCount() < 4) {
                    getGalleryImage();
                } else {
                    Toast.makeText(AddProductActivity.this, "Max 6 image allowded", Toast.LENGTH_SHORT).show();
                }
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

                    File fileLogo = new File(getExternalFilesDir("ValueAppz"), "product.png");
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
                default:
                    if (data == null || data.getExtras() == null) {
                        return;
                    }

                    Map<String, String> variant = (HashMap<String, String>) data.getExtras().getSerializable(VariantActivity.VARIANT_DATA);
                    variantList.add(variant);
                    variantAdapter.addVariantList(variantList);
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
                Toast.makeText(AddProductActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
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

    public void getCategoriesApi() {
        ProgressDialogUtil.showProgressDialog(this);
        Map<String, Object> param = new HashMap<>();
        param.put("page", 1);
        param.put("pagelength", 1000);
        NetworkAdaper.getNetworkServices().getCategories(param, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                if (getCategoryResponse.getSuccess()) {
                    categoryDataList = getCategoryResponse.getData();
                    Log.d(TAG, "success: " + selectedCategoryId);
                    for (GetCategoryData categoryData : categoryDataList) {
                        if (categoryData.getId().equals(selectedCategoryId)) {
                            editTextCategory.setText(categoryData.getTitle());
                            break;
                        }
                    }
                } else {
                    Toast.makeText(AddProductActivity.this, "Data not found!", Toast.LENGTH_SHORT).show();
                }
                getStoreAttributes();
                getProductById();
            }

            @Override
            public void failure(RetrofitError error) {
                if (isDestroyed()) {
                    return;
                }
                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(AddProductActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void runAnimation() {
        finish();
        AnimUtil.slideFromLeftAnim(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            runAnimation();
        }
        if (item.getItemId() == R.id.action_save) {
            saveProduct();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProduct() {
        String productName = editTextProductName.getText().toString().trim();
        String productDescription = editTextDescription.getText().toString().trim();
        String productTag = editTextTag.getText().toString().trim();
        String productTax = editTextTax.getText().toString().trim();
        String inclusiveExclusive = switchInEx.isChecked() ? "exclusive" : "inclusive";
        boolean displayVegNonVeg = checkBoxDisplayVegNonVeg.isChecked();
        List<MessageResponse> productImages = imagesAdapter.getImageList();
        Map<String, String> variantfieldMap = dynamicFieldAdapter.getFieldMap();

        // get images and check validation
        if (productImages.isEmpty()) {
            Toast.makeText(this, "Please add atleast one product image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate category
        if (TextUtils.isEmpty(selectedCategoryId)) {
            Toast.makeText(this, "Please choose category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate subcategory
        if (TextUtils.isEmpty(selectedSubCategoryId)) {
            Toast.makeText(this, "Please choose subcategory", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate product name
        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show();
            return;
        }

        // if display on get food type
        if (displayVegNonVeg) {
            if (TextUtils.isEmpty(foodType)) {
                Toast.makeText(this, "Please choose veg/non-veg", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Variant data
        if (variantfieldMap.isEmpty()) {
            Toast.makeText(this, "Please add variant details", Toast.LENGTH_SHORT).show();
            return;
        }

        for (DynamicField dynamicField : dynamicFieldList) {
            if (dynamicField.getValidation().equalsIgnoreCase("true")) {
                if (variantfieldMap.containsKey(dynamicField.getVariantFieldName())) {
                    if (TextUtils.isEmpty(variantfieldMap.get(dynamicField.getVariantFieldName()))) {
                        Toast.makeText(this, String.format("%s is empty", dynamicField.getLabel()), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, String.format("%s is empty", dynamicField.getLabel()), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject(variantfieldMap);
            jsonArray.put(jsonObject);

            for (Map<String, String> map : variantList) {
                jsonArray.put(new JSONObject(map.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<ProductImage> productImages1 = new ArrayList<>();
        for (MessageResponse messageResponse : productImages) {
            productImages1.add(new ProductImage(messageResponse.getUrl()));
        }

        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(productImages1, new TypeToken<List<ProductImage>>() {
        }.getType());
        JsonArray jsonArrayImage = element.getAsJsonArray();

        final Map<String, String> productData = new HashMap<>();
        productData.put("title", productName);
        productData.put("brand", productName);
        productData.put("category_id", selectedSubCategoryId);
        productData.put("description", productDescription);
        productData.put("nutrient", foodType);
        productData.put("tags", productTag);
        productData.put("image", productImages1.get(0).getImage());
        productData.put("Variants", jsonArray.toString());
        productData.put("images", jsonArrayImage.toString());
        if (!TextUtils.isEmpty(productTax)) {
            productData.put("gst_tax_type", inclusiveExclusive);
            productData.put("gst_tax_rate", productTax);
            productData.put("isTaxEnable", "1");
        }

        ProgressDialogUtil.showProgressDialog(this);
        NetworkAdaper.getNetworkServices().addProduct(productData, new Callback<CategoryStatus>() {
            @Override
            public void success(CategoryStatus res, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                if (res.getSuccess()) {
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                Toast.makeText(AddProductActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSelectCategory(String categoryId, String categoryName) {
        selectedCategoryId = categoryId;
        editTextCategory.setText(categoryName);
    }

    @Override
    public void onSelectSubCategory(String subCategoryId, String subCategoryName) {
        selectedSubCategoryId = subCategoryId;
        editTextSubCategory.setText(subCategoryName);
    }
}