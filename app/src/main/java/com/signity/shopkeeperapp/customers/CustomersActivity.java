package com.signity.shopkeeperapp.customers;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.adapter.SpacesItemDecoration;
import com.signity.shopkeeperapp.base.BaseActivity;
import com.signity.shopkeeperapp.book.BookOrderActivity;
import com.signity.shopkeeperapp.model.customers.CustomerDataResponse;
import com.signity.shopkeeperapp.model.customers.CustomersResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.util.prefs.AppPreference;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Intent.ACTION_DIAL;

public class CustomersActivity extends BaseActivity implements CustomersAdapter.CustomerAdapterListener {

    private static final String TAG = "CustomersActivity";
    private Toolbar toolbar;
    private CustomersAdapter customersAdapter;
    private RecyclerView recyclerView;
    private int pageSize = 10, currentPageNumber = 1, start, totalCustomers;
    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private View hiddenView;
    private PopupWindow rightMenuPopUpWindow;
    private int checkedId;
    private Constant.CustomerSort customerSort = Constant.CustomerSort.NEWEST;
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    if (start < totalCustomers) {
                        getCustomers();
                    }
                }
            }
        }
    };

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CustomersActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        initViews();
        setUpToolbar();
        setUpAdapter();
        getCustomers();
    }

    public void getCustomers() {
        Map<String, Object> param = new HashMap<>();
        param.put("page", currentPageNumber);
        param.put("pagelength", pageSize);
        param.put("sort_by", customerSort.getSortType());
        param.put("sort_type", customerSort.getSortOrder());

        isLoading = true;
        NetworkAdaper.getNetworkServices().getCustomersNew(param, new Callback<CustomerDataResponse>() {
            @Override
            public void success(CustomerDataResponse customerDataResponse, Response response) {

                if (isDestroyed()) {
                    return;
                }

                isLoading = false;

                if (customerDataResponse.getSuccess()) {
                    currentPageNumber++;
                    start += pageSize;
                    totalCustomers = customerDataResponse.getData().getCustomersCount();
                    if (customerDataResponse.getData() != null) {
                        customersAdapter.addCustomersList(customerDataResponse.getData().getCustomers(), totalCustomers);
                    } else {
                        Toast.makeText(CustomersActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CustomersActivity.this, "Data not Found!", Toast.LENGTH_SHORT).show();
                    customersAdapter.setShowLoading(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                isLoading = false;
                if (!isDestroyed()) {
                    Toast.makeText(CustomersActivity.this, "Network is unreachable", Toast.LENGTH_SHORT).show();
                    customersAdapter.setShowLoading(false);
                }
            }
        });
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(this);
        customersAdapter = new CustomersAdapter(this);
        customersAdapter.setListener(this);
        recyclerView.setAdapter(customersAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) Util.pxFromDp(this, 12)));
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_customers);
        hiddenView = findViewById(R.id.view);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showPopUpMenu() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popwindow_customer_sort, null, false);

        rightMenuPopUpWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        rightMenuPopUpWindow.setOutsideTouchable(true);
        rightMenuPopUpWindow.setBackgroundDrawable(new ColorDrawable());
        rightMenuPopUpWindow.setTouchInterceptor(new View.OnTouchListener() { // or whatever you want
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) // here I want to close the pw when clicking outside it but at all this is just an example of how it works and you can implement the onTouch() or the onKey() you want
                {
                    rightMenuPopUpWindow.dismiss();
                    return true;
                }
                return false;
            }

        });

        float den = getResources().getDisplayMetrics().density;
        int offsetY = (int) (den * 2);
        rightMenuPopUpWindow.showAsDropDown(hiddenView, 0, 0);

        final RadioGroup rdGroup = (RadioGroup) layout.findViewById(R.id.rdGroup);
        RadioButton radioNewest = (RadioButton) layout.findViewById(R.id.radioNewest);

        RadioButton radioOldest = (RadioButton) layout.findViewById(R.id.radioOldest);
        RadioButton radioAZ = (RadioButton) layout.findViewById(R.id.radioAZ);

        RadioButton radioZA = (RadioButton) layout.findViewById(R.id.radioZA);

        switch (checkedId) {
            case R.id.radioOldest:
                radioOldest.setChecked(true);
                break;
            case R.id.radioAZ:
                radioAZ.setChecked(true);
                break;
            case R.id.radioZA:
                radioZA.setChecked(true);
                break;
            case R.id.radioNewest:
            default:
                radioNewest.setChecked(true);
        }

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = rdGroup.findViewById(checkedId);
                int index = rdGroup.indexOfChild(radioButton);

                CustomersActivity.this.checkedId = checkedId;
                currentPageNumber = 1;
                start = 0;
                // Add logic here

                switch (index) {
                    case 0: // first button
                        customerSort = Constant.CustomerSort.NEWEST;
                        break;
                    case 1:
                        customerSort = Constant.CustomerSort.OLDEST;
                        break;
                    case 2:
                        customerSort = Constant.CustomerSort.A_Z;
                        break;
                    case 3:
                        customerSort = Constant.CustomerSort.Z_A;
                        break;
                }
                customersAdapter.clearData();
                getCustomers();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rightMenuPopUpWindow.dismiss();
                    }
                }, 500);
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
        inflater.inflate(R.menu.menu_customer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            runAnimation();
        }

        if (item.getItemId() == R.id.action_add_customer) {
            startActivity(AddCustomerActivity.getStartIntent(this));
            AnimUtil.slideFromRightAnim(this);
        }

        if (item.getItemId() == R.id.action_sort) {
            showPopUpMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickCustomer(CustomersResponse customersResponse) {
        Bundle bundle = new Bundle();
        bundle.putString(CustomerDetailActivity.CUSTOMER_ID, customersResponse.getId());
        startActivity(CustomerDetailActivity.getStartIntent(this, bundle));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onClickBookOrder(CustomersResponse customersResponse) {
        Bundle bundle = new Bundle();
        bundle.putString(BookOrderActivity.CUSTOMER_NUMBER, customersResponse.getPhone());
        startActivity(BookOrderActivity.getIntent(this, bundle));
        AnimUtil.slideFromRightAnim(this);
    }

    @Override
    public void onClickWhatsapp(String phone) {
        if (isValidPhone(phone)) {
            openWhatsapp(phone);
        }
    }

    private boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Sorry! phone number is not available.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClickCall(String phone) {
        if (isValidPhone(phone)) {
            callAlert(phone);
        }
    }

    @Override
    public void onClickMessage(String phone) {
        if (isValidPhone(phone)) {
            Uri uri = Uri.parse(String.format("smsto:%s", phone));
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(intent);
        }
    }

    private void callAlert(final String phone) {
        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(this);
        adb.setTitle("Call " + phone + " ?");
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                actionCall(phone);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private void actionCall(String phone) {
        try {
            PackageManager pm = getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(this);
            } else {
                Toast.makeText(this, "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openWhatsapp(String phone) {
        try {
            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled) {

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                String data = String.format("%s%s@s.whatsapp.net", AppPreference.getInstance().getPhoneCode(), phone);
                sendIntent.putExtra("jid", data);//phone number without "+" prefix
                startActivity(sendIntent);
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean appInstalled = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }
}
