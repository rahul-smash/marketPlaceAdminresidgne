package com.signity.shopkeeperapp.util;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by rajesh on 30/9/15.
 */
public class Constant {


    public final static String PLATFORM = "android";
    public final static String APP_TITLE = "ValueAppz";
    public static final String TYPE_APPROVE = "com.signity.shopkeeperapp.TYPE_APPROVE";
    public static final String TYPE_PROCESSING = "com.signity.shopkeeperapp.TYPE_PROCESSING";
    public static final String TYPE_SHIPPING = "com.signity.shopkeeperapp.TYPE_SHIPPING";

    public static final String ITEMS = "items";
    public static final String SHARED_PREF = "shopkeeper_pref";
    public static final String STORE_DETAILS = "store_details";
    public static final String STORE_ID = "store_id";
    public static final String LOGIN_USER_MOBILE_NUMBER = "mobile_number";
    public static final String LOGIN_USER_EMAIL_ID = "emailId";
    public static final String LOGIN_CHECK = "com.signity.shopkeeperapp.LOGIN_CHECK";
    public static final String IS_ADMIN = "com.signity.shopkeeperapp.ADMIN";
    public static final String STAFF_ADMIN_ID = "com.signity.shopkeeperapp.STAFF_ADMIN_ID";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String STORE_STATUS_MESSAGE = "com.signity.shopkeeperapp.STORE_STATUS_MESSAGE";
    public static final String STORE_CURRENCY = "com.signity.shopkeeperapp.CURRENCY";
    public static final String VOLUME_STATUS = "com.signity.shopkeeperapp.VOLUME_STATUS";


    public static final int LOCAL_NOTIFY_FOR_DUE_ORDER = 328328;
    public static final int LOCAL_NOTIFY_FOR_8_PM = 328329;
    public static final int LOCAL_NOTIFY_FOR_10_AM = 328330;


    public static final String LOCAL_TYPE_ONE = "type_one";
    public static final String LOCAL_TYPE_TWO = "type_two";
    public static final String LOG_IN_TYPE = "LOG_IN_TYPE";
    public static final String USER_ID = "USER_ID";
    public static final String LOGIN_TYPE = "mLoginType";
    public static final String EMAIL = "mEmail";
    public static final String PHONE = "mPhone";


    public static final String TYPE_DUE_ORDER = "com.signity.shopkeeperapp.DUE_ORDER";
    public static final String TYPE_ACTIVE_ORDER = "com.signity.shopkeeperapp.ACTIVE_ORDER";
    public static final String TYPE_REJECTED = "com.signity.shopkeeperapp.REJECTED";
    public static final String TYPE_DELIVERED = "com.signity.shopkeeperapp.DELIVERED";
    public static final String TYPE_CANCELLED = "com.signity.shopkeeperapp.CANCELLED";
    public static final String TYPE_ALL_ORDER = "com.signity.shopkeeperapp.ALL_ORDER";

    public static final String KEY_ALL = "pending-processing-shipped";

    public static final String REFERESH_DATA_REQURIED = "com.signity.shopkeeperapp.REFERESH_DATA_REQURIED";


    public static final String APP_NOTIFICATION_URL = "com.signity.shopkeeperapp.APP_NOTIFICATION_URL";
    public static final String APP_STATE_VISIBLE = "com.signity.shopkeeperapp.APP_STATE_VISIBLE";
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final String READ_SDCARD = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_SDCARD = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CALL = Manifest.permission.CALL_PHONE;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public enum Mode {

        LOGGED_IN(0), NOT_LOGGED_IN(1);

        private int type;

        Mode(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum StoreDashboard {
        TODAY(1, "Today"), YESTERDAY(2, "Yesterday"), LAST_WEEK(7, "Last Week"), LAST_MONTH(30, "Last Month");

        private int days;
        private String title;

        StoreDashboard(int days, String title) {
            this.days = days;
            this.title = title;
        }

        public int getDays() {
            return days;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum MarketMode {
        CREATIVE, FRAME, GALLERY
    }

    public enum CreativeView {
        VIEW_ALL, VIEW_SHARED
    }

    public enum ShareMode {
        SCHEDULE, RESCHEDULE
    }


    public enum CustomerSort {
        NEWEST("newest"), OLDEST("oldest"), A_Z("az"), Z_A("za");

        private String slug;

        CustomerSort(String slug) {
            this.slug = slug;
        }

        public String getSlug() {
            return slug;
        }
    }

}
