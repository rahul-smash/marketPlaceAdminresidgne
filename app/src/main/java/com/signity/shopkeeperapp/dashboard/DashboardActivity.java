package com.signity.shopkeeperapp.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.login.LoginManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.signity.shopkeeperapp.BuildConfig;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.SplashActivity;
import com.signity.shopkeeperapp.app.MyApplication;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.book.BookOrderActivity;
import com.signity.shopkeeperapp.contactus.ContactUsActivity;
import com.signity.shopkeeperapp.customers.CustomersActivity;
import com.signity.shopkeeperapp.dashboard.account.AccountFragment;
import com.signity.shopkeeperapp.dashboard.catalog.CatalogFragment;
import com.signity.shopkeeperapp.dashboard.home.HomeFragment;
import com.signity.shopkeeperapp.dashboard.orders.OrdersFragment;
import com.signity.shopkeeperapp.market.CreativeFragment;
import com.signity.shopkeeperapp.market.ShareCreativeActivity;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.model.ModelForceUpdate;
import com.signity.shopkeeperapp.model.ResponseForceUpdate;
import com.signity.shopkeeperapp.model.dashboard.Data;
import com.signity.shopkeeperapp.model.dashboard.InfoDialog;
import com.signity.shopkeeperapp.model.dashboard.StoreVersionDTO;
import com.signity.shopkeeperapp.model.dashboard.WelcomeResponse;
import com.signity.shopkeeperapp.model.market.industry.IndustryRegistration;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.products.ImageBottomDialog;
import com.signity.shopkeeperapp.runner.RunnerActivity;
import com.signity.shopkeeperapp.stores.StoresActivity;
import com.signity.shopkeeperapp.twilio.chat.CustomerSupportActivity;
import com.signity.shopkeeperapp.twilio.chat.TwilioLogin;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DashboardActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener, NavDrawerAdapter.NavigationListener {

    private static final String TAG = "DashboardActivity";
    private static final int MY_REQUEST_CODE = 1001;
    private static final int CAMERA_REQUEST = 100;
    private static final int PICK_REQUEST = 200;
    private static final String STYLE_IMAGE = "style_image";
    private static final int CAMERA_PERMISSION = 1000;
    private static final int GALLERY_PERMISSION = 2000;
    public BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView textViewToolbarTitle;
    private TextView textViewNavigationTitle;
    private TextView textViewAppVersion;
    private boolean doubleBackToExitPressedOnce;
    private NavDrawerAdapter navDrawerAdapter;
    private ListView listViewNavigation;
    private int navSelectedId;
    private TextView textViewLogout;
    private ImageView imageViewGenerateOrder;
    private Uri cameraImageUri;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();
        setUpToolbar();
        setUpNavigationAdapter();
        setUpDrawerToggle();
        setUpBottomNavigation();
        storeAppVersion();
        showWelcome();

        if (!AppPreference.getInstance().isPrivateChannelCreated() && BuildConfig.DEBUG) {
            TwilioLogin twilioLogin = new TwilioLogin(this);
            twilioLogin.performLoginCreatePrivateChannel();
        }

        registerStore();
    }

    private void registerStore() {

        Map<String, String> param = new HashMap<>();
        param.put("email", AppPreference.getInstance().getUserEmail());
        param.put("number", AppPreference.getInstance().getUserMobile());
        param.put("store_id", AppPreference.getInstance().getStoreId());
        param.put("store_title", AppPreference.getInstance().getStoreName());
        // TODO - Defaut country selected to India
        param.put("country", "India");

        NetworkAdaper.marketStore().registerStore(param, new Callback<IndustryRegistration>() {
            @Override
            public void success(IndustryRegistration response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void setUpStoreData() {
        String storeName = AppPreference.getInstance().getStoreName();
        textViewNavigationTitle.setText(storeName);
        textViewAppVersion.setText(String.format("Version %s", BuildConfig.VERSION_NAME));
    }

    private void setUpNavigationAdapter() {
        navDrawerAdapter = new NavDrawerAdapter(this, this);
        listViewNavigation.setAdapter(navDrawerAdapter);
        listViewNavigation.setDivider(null);
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        textViewToolbarTitle = findViewById(R.id.tv_toolbar_dashboard);
        listViewNavigation = findViewById(R.id.lv_navigation);
        textViewNavigationTitle = findViewById(R.id.tv_store_name_nav);
        textViewAppVersion = findViewById(R.id.tv_app_version);
        textViewLogout = findViewById(R.id.tv_logout_title);
        imageViewGenerateOrder = findViewById(R.id.float_action);

        imageViewGenerateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BookOrderActivity.getIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callLogOutApi();
            }
        });
    }

    private void createOrdersBadge(int count) {
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.action_bottom_orders);
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.colorBadge));
        badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.colorTextDark));
        badgeDrawable.setNumber(count);
        badgeDrawable.setVisible(count > 0);
    }

    private void setUpDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.menuicon);
        mDrawerToggle.syncState();

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setItemHorizontalTranslationEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_bottom_home);
    }

    private void toggleDrawer() {
        hideKeyboard();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        navDrawerAdapter.setSelectedId(navSelectedId);
        checkForceDownload();
        setUpStoreData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.action_bottom_home:
                navSelectedId = 0;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText(AppPreference.getInstance().getStoreName());
                showFragment(HomeFragment.getInstance(null), HomeFragment.TAG);
                break;
            case R.id.action_bottom_orders:
                navSelectedId = 1;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText("Orders");
                showFragment(OrdersFragment.getInstance(null), OrdersFragment.TAG);
                break;
            case R.id.action_bottom_catalog:
                navSelectedId = 2;
                navDrawerAdapter.setSelectedId(navSelectedId);
                textViewToolbarTitle.setText("Catalog");
                showFragment(CatalogFragment.getInstance(null), CatalogFragment.TAG);
                break;
            case R.id.action_bottom_categories:
//                textViewToolbarTitle.setText("Categories");
//                showFragment(CategoriesFragment.getInstance(null), CategoriesFragment.TAG);
                break;
            case R.id.action_bottom_account:
                textViewToolbarTitle.setText("Account");
                showFragment(AccountFragment.getInstance(null), AccountFragment.TAG);
                break;
        }
        return false;
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (manager.findFragmentByTag(tag) == null) {
            fragmentTransaction.replace(R.id.fl_dashboard, fragment, tag);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_bottom_home);
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            hideKeyboard();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClickViewAllOrders() {
        bottomNavigationView.setSelectedItemId(R.id.action_bottom_orders);
    }

    @Override
    public void onClickViewProducts() {
        bottomNavigationView.setSelectedItemId(R.id.action_bottom_catalog);
    }

    @Override
    public void onClickViewCustomers() {
        startActivity(CustomersActivity.getStartIntent(this));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onUpdateOrdersCount(int count) {
        createOrdersBadge(count);
    }

    @Override
    public void onChooseImage() {
        openImageChooser();
    }

    @Override
    public void onClickNavigation(NavDrawerAdapter.NavigationItems navigationItems) {
        switch (navigationItems) {
            case DASHBOARD:
                bottomNavigationView.setSelectedItemId(R.id.action_bottom_home);
                break;
            case ORDERS:
                bottomNavigationView.setSelectedItemId(R.id.action_bottom_orders);
                break;
            case PRODUCTS:
                bottomNavigationView.setSelectedItemId(R.id.action_bottom_catalog);
                break;
            case CUSTOMERS:
                onClickViewCustomers();
                break;
            /*case MARKET:
                startActivity(new Intent(this, MarketActivity.class));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;
            case BOOK:
                startActivity(BookOrderActivity.getIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;*/
            case RUNNER:
                startActivity(RunnerActivity.getStartIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;
            case SWITCH_STORE:
                startActivity(StoresActivity.getStartIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;
            case CUSTOMER_SUPPORT:
                startActivity(CustomerSupportActivity.getStartIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;
            case CONTACT_US:
                startActivity(ContactUsActivity.getStartIntent(DashboardActivity.this));
                AnimUtil.slideFromRightAnim(DashboardActivity.this);
                break;
            case LOGOUT:
                callLogOutApi();
                break;
        }
        toggleDrawer();
    }

    private void checkForceDownload() {
        NetworkAdaper.getNetworkServices().forceDownload(new Callback<ResponseForceUpdate>() {
            @Override
            public void success(ResponseForceUpdate responseForceUpdate, Response response) {

                if (isDestroyed()) {
                    return;
                }

                if (responseForceUpdate != null && responseForceUpdate.getSuccess()) {
                    try {
                        ModelForceUpdate forceUpdate = responseForceUpdate.getData().get(0);
                        checkForceUpdate(forceUpdate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private String getDeviceOSName() {
        Field[] fields = Build.VERSION_CODES.class.getFields();
        String codeName = "UNKNOWN";
        for (Field field : fields) {
            try {
                if (field.getInt(Build.VERSION_CODES.class) == Build.VERSION.SDK_INT) {
                    codeName = field.getName();
                    Log.d(TAG, "Version name: " + codeName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return codeName;
    }

    private void storeAppVersion() {

        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Map<String, Object> param = new HashMap<>();
        param.put("user_id", AppPreference.getInstance().getUserId());
        param.put("app_version", BuildConfig.VERSION_NAME);
        param.put("device_id", deviceId);
        param.put("device_token", AppPreference.getInstance().getDeviceToken());
        param.put("device_brand", Build.BRAND);
        param.put("device_model", Build.MODEL);
        param.put("device_os", Build.VERSION.RELEASE);
        param.put("device_os_version", getDeviceOSName());
        param.put("device_type", "android");

        NetworkAdaper.getNetworkServices().storeAppVersion(param, new Callback<StoreVersionDTO>() {
            @Override
            public void success(StoreVersionDTO storeVersionDTO, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void checkForceUpdate(ModelForceUpdate forceUpdate) {

        String currentVersion;
        String playStoreVersion;
        if (forceUpdate != null) {
            currentVersion = BuildConfig.VERSION_NAME;
            playStoreVersion = forceUpdate.getAndroidAppVerison();

            if (!TextUtils.isEmpty(playStoreVersion)) {
                try {
                    double playVersion = Double.parseDouble(playStoreVersion);

                    double appVersion = Double.parseDouble(currentVersion);
                    Log.i("@@playversion", "" + playVersion);
                    Log.i("@@appVersion", "" + appVersion);

                    if (playVersion > appVersion) {
                        openDialogForVersion(forceUpdate);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private void openDialogForVersion(ModelForceUpdate forceUpdate) {
        if (forceUpdate.getForceDownload() != null) {
            final DialogHandler dialogHandler = new DialogHandler(this);
            dialogHandler.setDialog("APPLICATION UPDATE", forceUpdate.getForceDownloadMessage());
            if (forceUpdate.getForceDownload().equalsIgnoreCase("1")) {
                dialogHandler.setCancelable(false);
                dialogHandler.setPostiveButton("Update", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPlayStoreLink();
                        dialogHandler.dismiss();
                    }

                });
            } else {
                dialogHandler.setNegativeButton("Cancel", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogHandler.dismiss();
                    }
                });
                dialogHandler.setPostiveButton("Update", true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPlayStoreLink();
                        dialogHandler.dismiss();
                    }
                });
            }
        }
    }

    private void openPlayStoreLink() {
        final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
        Log.i("@@openPlayStoreLink", "" + appPackageName);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void callLogOutApi() {
        ProgressDialogUtil.showProgressDialog(this);

        Map<String, String> param = new HashMap<String, String>();
        param.put("login_id", AppPreference.getInstance().getUserMobile());
        param.put("type", "phone");

        NetworkAdaper.getNetworkServices().logout(param, new Callback<LoginModel>() {
            @Override
            public void success(LoginModel loginModel, Response response) {

                if (isDestroyed()) {
                    return;
                }

                ProgressDialogUtil.hideProgressDialog();
                AppPreference.getInstance().clearAll();
                LoginManager.getInstance().logOut();
                NetworkAdaper.setupRetrofitClient(NetworkAdaper.setBaseUrl(""));
                MyApplication.getInstance().getBasicChatClient().shutdown();
                startActivity(SplashActivity.getIntent(DashboardActivity.this));
                AnimUtil.slideFromLeftAnim(DashboardActivity.this);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
            }
        });
    }


    private void openImageChooser() {
        if (getSupportFragmentManager().findFragmentByTag(ImageBottomDialog.TAG) == null) {
            ImageBottomDialog imageBottomDialog = new ImageBottomDialog(new ImageBottomDialog.ImageListener() {
                @Override
                public void onClickGallery() {
                    openGallery();
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
        File fileCamera = new File(getExternalFilesDir("ValueAppz"), STYLE_IMAGE.concat(".jpg"));
        cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", fileCamera);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        Bitmap scale = Util.scaleBitmap(bitmap, 1200, 900);
                        Util.saveImageFile(scale, "style", getExternalFilesDir("ValueAppz"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (resultUri != null)
                        openShareCreative();
                    break;
            }
        }
    }

    private void openShareCreative() {
        Intent intent = ShareCreativeActivity.getStartIntent(this);
        intent.putExtra("url", "");
        intent.putExtra("desc", "Style Gallery");
        intent.putExtra("title", "Style Gallery");
        intent.putExtra("creativeId", "0");
        intent.putExtra("isShared", false);
        intent.putExtra(CreativeFragment.MARKET_MODE, Constant.MarketMode.GALLERY);
        startActivity(intent);
    }

    private void cropImage(Uri uri) {
        File fileCamera = new File(getExternalFilesDir("ValueAppz"), "style.jpg");
        Uri outCamera = Uri.fromFile(fileCamera);
        UCrop.of(uri, outCamera)
                .withAspectRatio(4, 3)
                .withMaxResultSize(1200, 900)
                .start(this);
    }

    private void showWelcome() {
        NetworkAdaper.getNetworkServices().getWelcomeMessage(AppPreference.getInstance().getStoreId(), new Callback<WelcomeResponse>() {
            @Override
            public void success(WelcomeResponse welcomeResponse, Response response) {
                if (isDestroyed()) {
                    return;
                }

                if (welcomeResponse.isStatus()) {
                    showWelcomeDialog(welcomeResponse.getData());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void showWelcomeDialog(Data data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("title", data.getTitle());
            bundle.putString("message", data.getMessage());

            if (getSupportFragmentManager().findFragmentByTag(InfoDialog.TAG) == null) {
                InfoDialog infoDialog = InfoDialog.getInstance(bundle);
                infoDialog.show(getSupportFragmentManager(), InfoDialog.TAG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}