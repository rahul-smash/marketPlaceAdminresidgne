package com.signity.shopkeeperapp.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.signity.shopkeeperapp.Categories.CategoriesFragment;
import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.canceled_orders.CanceledOrdersFragment;
import com.signity.shopkeeperapp.customer.CustomerFragment;
import com.signity.shopkeeperapp.enquiries.EnquiriesFragment;
import com.signity.shopkeeperapp.manage_stores.ManageStaffActivity;
import com.signity.shopkeeperapp.manage_stores.ManageStoresFragment;
import com.signity.shopkeeperapp.model.DashBoardModelStoreDetail;
import com.signity.shopkeeperapp.model.MobResponse;
import com.signity.shopkeeperapp.network.NetworkAdaper;
import com.signity.shopkeeperapp.orders.ActiveOrderFragment;
import com.signity.shopkeeperapp.orders.DueOrderFragment;
import com.signity.shopkeeperapp.rejected_orders.RejectedItemsFragment;
import com.signity.shopkeeperapp.util.AnimUtil;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.DialogHandler;
import com.signity.shopkeeperapp.util.DialogUtils;
import com.signity.shopkeeperapp.util.FontUtil;
import com.signity.shopkeeperapp.util.ProgressDialogUtil;
import com.signity.shopkeeperapp.util.Util;
import com.signity.shopkeeperapp.view.LoginScreenActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    SlidingPaneLayout mSlidingPanel;

    String[] title = {"Dashboard", "Due Orders", "Active Orders", "Rejected Orders", "Canceled Orders", "Customers", "Enquiries", "Manage Stores", "Categories"};
    String shareContent = "";

    public static String fragmentName = "";
    Button btnMenu, btnMenuRight;
    TextView textTitle, txtShopName, txtShopKeeperName;

    TextView textDashBoard, textDueOrders, textActiveOrders, textRejectedOrders, textCanceledOrders, textCustomers, textEnquiries, textmanageStores;
    PopupWindow rightMenuPopUpWindow;
    View topDot;

    LinearLayout ordersBlock;
    ImageView mOrderDetailBtn;

    Animation menuRightToLeftAnim, menuLeftToRightAnim;
    Animation slideUpAnim;
    Animation slideDownAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSlidingPanel = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
        mSlidingPanel.setShadowResourceRight(R.drawable.transparent_bg);
        mSlidingPanel.setParallaxDistance(300);
        mSlidingPanel.setSliderFadeColor(200);

        btnMenu = (Button) findViewById(R.id.btnMenu);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textTitle.setText(title[0]);
        btnMenu.setOnClickListener(this);

        btnMenuRight = (Button) findViewById(R.id.btnMenuRight);
        btnMenuRight.setOnClickListener(this);

        topDot = (View) findViewById(R.id.topDot);

        txtShopName = (TextView) findViewById(R.id.txtShopName);
        txtShopName.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));
        txtShopKeeperName = (TextView) findViewById(R.id.txtShopKeeperName);
        txtShopKeeperName.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_THIN));

        textDashBoard = (TextView) findViewById(R.id.txtDashBoard);
        textDashBoard.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        ordersBlock = (LinearLayout) findViewById(R.id.ordersBlock);
        mOrderDetailBtn = (ImageView) findViewById(R.id.ordersDetailBtn);
        menuRightToLeftAnim = AnimationUtils.loadAnimation(MainActivity.this
                .getApplicationContext(), R.anim.menu_option_right_to_left);
        menuLeftToRightAnim = AnimationUtils.loadAnimation(MainActivity.this
                .getApplicationContext(), R.anim.menu_option_left_to_right);

        slideUpAnim = AnimationUtils.loadAnimation(this
                .getApplicationContext(), R.anim.slide_up_for_order_detail);
        slideDownAnim = AnimationUtils.loadAnimation(this
                .getApplicationContext(), R.anim.slide_down_for_oder_detail);

        textDueOrders = (TextView) findViewById(R.id.txtDueOrders);
        textDueOrders.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        textActiveOrders = (TextView) findViewById(R.id.txtActiveOrders);
        textActiveOrders.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        textRejectedOrders = (TextView) findViewById(R.id.txtRejectedOrders);
        textRejectedOrders.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        textCanceledOrders = (TextView) findViewById(R.id.txtCanceledOrders);
        textCanceledOrders.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        textCustomers = (TextView) findViewById(R.id.txtCutomers);
        textCustomers.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        textEnquiries = (TextView) findViewById(R.id.txtEnquiries);
        textEnquiries.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        textmanageStores = (TextView) findViewById(R.id.txtManageStores);
        textmanageStores.setTypeface(FontUtil.getTypeface(com.signity.shopkeeperapp.home.MainActivity.this, FontUtil.FONT_ROBOTO_REGULAR));

        String intentValue = "";
        try {
            intentValue = getIntent().getExtras().getString("Active");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (intentValue.equalsIgnoreCase("1")) {
            textTitle.setText(title[2]);
            fragmentName = title[2];
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ActiveOrderFragment.newInstance(this), ActiveOrderFragment.class.getSimpleName()).commit();
        } else {
            fragmentName = title[0];
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainActivityFragment.newInstance(this)).commit();
        }


        DashBoardModelStoreDetail jObject = getStoreDataAsObject(Util.loadPreferenceValue(MainActivity.this, Constant.STORE_DETAILS));

        String storeStatus = "";
        try {
            storeStatus = jObject.getStoreStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (storeStatus.equalsIgnoreCase("0")) {
            String message = Util.loadPreferenceValue(MainActivity.this, Constant.STORE_STATUS_MESSAGE);
            storeStatusAlertNew(message + "\n" + "Do you want to turn the customer app on?", "on");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnMenu:
                setStoreDetails();
                toggleSlidingMenu();
                break;
            case R.id.btnMenuRight:
                showPopUpMenu();
                break;

        }

    }


    public DashBoardModelStoreDetail getStoreDataAsObject(String store) {
        DashBoardModelStoreDetail object;
        Gson gson = new Gson();
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        object = gson.fromJson(store, type);
        return object;
    }

    private void showPopUpMenu() {

        String storeStatus = "";

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.right_menu_view,
                (ViewGroup) findViewById(R.id.popups));

        // layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.popup_window_show));

        String isAdmin = Util.loadPreferenceValue(MainActivity.this, Constant.IS_ADMIN);


        TextView mStoreStatus = (TextView) layout.findViewById(R.id.textStoreStatus);
        TextView manageStaff = (TextView) layout.findViewById(R.id.staff);

        if (isAdmin.equalsIgnoreCase("admin")) {
            manageStaff.setVisibility(View.VISIBLE);
        } else if (isAdmin.equalsIgnoreCase("staff")) {
            manageStaff.setVisibility(View.GONE);
        } else {
            manageStaff.setVisibility(View.GONE);
        }
        ImageView mStoreStatusImage = (ImageView) layout.findViewById(R.id.storeStatus);
        TextView mShare = (TextView) layout.findViewById(R.id.share);
        TextView mLogOut = (TextView) layout.findViewById(R.id.logOut);

        final DashBoardModelStoreDetail jObject = getStoreDataAsObject(Util.loadPreferenceValue(MainActivity.this, Constant.STORE_DETAILS));

        storeStatus = jObject.getStoreStatus();

        if (storeStatus.equalsIgnoreCase("1")) {
            mStoreStatusImage.setBackgroundResource(R.drawable.greeen_circle_bg);
        } else {
            mStoreStatusImage.setBackgroundResource(R.drawable.red_circle_bg);
        }


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
        int offsetY = (int) (den * 5);
        rightMenuPopUpWindow.showAsDropDown(topDot, 0, offsetY);

        mStoreStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                rightMenuPopUpWindow.dismiss();
                if (jObject.getStoreStatus().equalsIgnoreCase("1")) {
//                    storeStatusAlertNew("Do you want to turn the customer app off?", "off");
                    setStoreStatusOn();
                } else {
                    storeStatusAlertNew("Do you want to turn the customer app on?", "on");
                }

            }
        });

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightMenuPopUpWindow.dismiss();

                setShareContent();
            }
        });


        mLogOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rightMenuPopUpWindow.dismiss();

                logoutAlertNew("Are you sure to logout?");


            }
        });
        manageStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightMenuPopUpWindow.dismiss();

                Intent intent = new Intent(MainActivity.this, ManageStaffActivity.class);
                startActivity(intent);

            }
        });


    }

    private void setStoreDetails() {
        try {
            DashBoardModelStoreDetail jObject = getStoreDataAsObject(Util.loadPreferenceValue(MainActivity.this, Constant.STORE_DETAILS));
            txtShopName.setText(jObject.getStoreName());
        } catch (Exception e) {
            e.printStackTrace();
            txtShopName.setText("Store Name");
        }


    }

    public void toggleSlidingMenu() {
        if (!mSlidingPanel.isOpen()) {
            mSlidingPanel.openPane();
        } else {
            mSlidingPanel.closePane();
        }
    }

    public void onMenuButtonClick(View view) {

        switch (view.getId()) {
            case R.id.btnDashBoard:
                textTitle.setText(title[0]);
                fragmentName = title[0];
                replace(MainActivityFragment.newInstance(this));
                toggleSlidingMenu();
                break;
            case R.id.btnOrders:
                actionForOrdersDetailView();
                break;
            case R.id.btnDueOrders:
                textTitle.setText(title[1]);
                fragmentName = title[1];
                replace(DueOrderFragment.newInstance(this));
                toggleSlidingMenu();
                break;
            case R.id.btnActiveOrder:
                textTitle.setText(title[2]);
                fragmentName = title[2];
                replace(ActiveOrderFragment.newInstance(this));
                toggleSlidingMenu();
                break;

            case R.id.btnRejectedOrder:
                textTitle.setText(title[3]);
                fragmentName = title[3];
                replace(RejectedItemsFragment.newInstance(this));
                toggleSlidingMenu();
                break;

            case R.id.btnCanceledOrder:
                textTitle.setText(title[4]);
                fragmentName = title[4];
                replace(CanceledOrdersFragment.newInstance(this));
                toggleSlidingMenu();
                break;

            case R.id.btnCustomer:
                textTitle.setText(title[5]);
                fragmentName = title[5];
                replace(CustomerFragment.newInstance(this));
                toggleSlidingMenu();
                break;

            case R.id.btnEnquiries:
                textTitle.setText(title[6]);
                fragmentName = title[6];
                replace(EnquiriesFragment.newInstance(this));
                toggleSlidingMenu();
                break;

            case R.id.btnManageStores:

                textTitle.setText(title[7]);
                fragmentName = title[7];
                replace(ManageStoresFragment.newInstance(this));
                toggleSlidingMenu();

                break;

            case R.id.btnCategories:

                textTitle.setText(title[8]);
                fragmentName = title[8];
                replace(CategoriesFragment.newInstance(this));
                toggleSlidingMenu();

                break;
        }

    }

    private void actionForOrdersDetailView() {

        if (mOrderDetailBtn.isSelected()) {
            orderDetailViewGone();
        } else {

            orderDetailViewVisible();
        }

    }

    private void orderDetailViewVisible() {
        mOrderDetailBtn.setSelected(true);
        ordersBlock.startAnimation(slideDownAnim);
        ordersBlock.setVisibility(View.VISIBLE);
    }

    private void orderDetailViewGone() {
        mOrderDetailBtn.setSelected(false);
        ordersBlock.startAnimation(slideUpAnim);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 1 second
                        ordersBlock.setVisibility(View.GONE);
                    }
                }, 300);
            }
        });


    }


    private void setShareContent() {

        DashBoardModelStoreDetail jobj = getStoreDataAsObject(Util.loadPreferenceValue(MainActivity.this, Constant.STORE_DETAILS));

        String storeName = "";
        String storeCity = "";
        String storeLiveUrl = "";
        try {
            storeName = jobj.getStoreName();
            storeCity = jobj.getCity();
            storeLiveUrl = jobj.getAndroidAppShare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shareContent = "Kindly download " + storeName + " App from\n" +
                storeLiveUrl + "\n" +
                "Thanks and Regards\n" +
                storeName + "\n" +
                "" + storeCity;

        shareIntent();
    }

    private void shareIntent() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        startActivity(Intent.createChooser(intent, "Share with"));

    }

    private void clearSharedPref() {

        Util.savePreferenceValue(com.signity.shopkeeperapp.home.MainActivity.this, Constant.LOGIN_CHECK, "0");
        Util.savePreferenceValue(this, Constant.DEVICE_TOKEN, "");
        Util.savePreferenceValue(this, Constant.STORE_ID, "");
        Intent intent_home = new Intent(com.signity.shopkeeperapp.home.MainActivity.this,
                LoginScreenActivity.class);
        startActivity(intent_home);
        AnimUtil.slideFromRightAnim(com.signity.shopkeeperapp.home.MainActivity.this);
        finish();
    }

    public void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }

    private void logoutAlert(String title) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);


        adb.setTitle(title);


        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                clearSharedPref();

            }
        });


        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    private void logoutAlertNew(String msg) {

        final DialogHandler dialogHandler = new DialogHandler(MainActivity.this);

        dialogHandler.setDialog(Constant.APP_TITLE, msg);
        dialogHandler.setPostiveButton("Ok", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        clearSharedPref();
                        dialogHandler.dismiss();
                    }
                });

        dialogHandler.setNegativeButton("No", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogHandler.dismiss();
                    }
                });
    }

    private void storeStatusAlert(String title, final String storeStatusValue) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);


        adb.setTitle(title);


        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

//                setStoreStatus(storeStatusValue);

            }
        });


        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    private void storeStatusAlertNew(String msg, final String storeStatusValue) {

        final DialogHandler dialogHandler = new DialogHandler(MainActivity.this);

        dialogHandler.setDialog(Constant.APP_TITLE, msg);
        dialogHandler.setPostiveButton("yes", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setStoreStatus(storeStatusValue, "On");
                        dialogHandler.dismiss();
                    }
                });

        dialogHandler.setNegativeButton("No", true)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogHandler.dismiss();
                    }
                });
    }

    private void setStoreStatusOn() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_store_status);
        TextView positveButton = (TextView) dialog.findViewById(R.id.yesBtn);
        TextView negativeButton = (TextView) dialog.findViewById(R.id.noBtn);
        final EditText message = (EditText) dialog.findViewById(R.id.message);

        String storeSaveMessage = Util.loadPreferenceValue(MainActivity.this, Constant.STORE_STATUS_MESSAGE);
        if (!storeSaveMessage.isEmpty()) {
            message.setText(storeSaveMessage);
        }

        positveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Store message is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                setStoreStatus("off", message.getText().toString());
                dialog.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


    }


    private void setStoreStatus(final String storeStatusValue, final String storeMessage) {
        ProgressDialogUtil.showProgressDialog(MainActivity.this);

        Map<String, String> param = new HashMap<String, String>();
        param.put("store_status", storeStatusValue);
        param.put("message", storeMessage);

        NetworkAdaper.getInstance().getNetworkServices().setStoreStatus(param, new Callback<MobResponse>() {
            @Override
            public void success(MobResponse getResponse, Response response) {
                Log.e("Tab", getResponse.toString());
                if (getResponse.getSuccess()) {
                    ProgressDialogUtil.hideProgressDialog();
                    if (storeStatusValue.equalsIgnoreCase("off")) {
                        updateStoreStatus("0");
                    } else if (storeStatusValue.equalsIgnoreCase("on")) {
                        updateStoreStatus("1");
                    }

                    DialogUtils.showAlertDialog(MainActivity.this, Constant.APP_TITLE, getResponse.getMessage());
                } else {
                    ProgressDialogUtil.hideProgressDialog();

                    DialogUtils.showAlertDialog(MainActivity.this, Constant.APP_TITLE, getResponse.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ProgressDialogUtil.hideProgressDialog();
                DialogUtils.showAlertDialog(MainActivity.this, Constant.APP_TITLE, "Error Occurred, Try again later.");
            }
        });
    }

    private void updateStoreStatus(String status) {

        DashBoardModelStoreDetail jObject = getStoreDataAsObject(Util.loadPreferenceValue(MainActivity.this, Constant.STORE_DETAILS));

        jObject.setStoreStatus(status);

        Util.savePreferenceValue(MainActivity.this, Constant.STORE_DETAILS, getStoreDataAsString(jObject));

    }

    public String getStoreDataAsString(DashBoardModelStoreDetail store) {
        Gson gson = new Gson();
        Type type = new TypeToken<DashBoardModelStoreDetail>() {
        }.getType();
        return gson.toJson(store, type);
    }

    public void printNUm(int num) {

        List<Fragment> list = getSupportFragmentManager().getFragments();
        ActiveOrderFragment fragment = ((ActiveOrderFragment) list.get(0));
        fragment.viewPager.setCurrentItem(num);
        Log.e("Tag", num + "");
    }

    @Override
    public void onBackPressed() {

        if (mSlidingPanel.isOpen()) {
            mSlidingPanel.closePane();

        } else {
            if (fragmentName.equalsIgnoreCase(title[0])) {
                this.finish();
            } else {
                textTitle.setText(title[0]);
                fragmentName = title[0];
                replace(MainActivityFragment.newInstance(this));
                AnimUtil.slideFromLeftAnim(com.signity.shopkeeperapp.home.MainActivity.this);
            }
        }

    }

//    private void sendNotification(String title, String message) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//
//        int icon = R.mipmap.ic_launcher;
//        Uri defaultSoundUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://com.signity.valueappz/raw/notificationrecieved");
////        Uri defaultSoundUri = Uri.parse("android.resource://com.signity.valueappz/" + R.raw.notificationrecieved);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setTicker(title)
//                .setSmallIcon(icon)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
//    }


}
