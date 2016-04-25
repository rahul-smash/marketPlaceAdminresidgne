package com.signity.shopkeeperapp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rajesh on 1/10/15.
 */
public class PrefManager {
    public static final String MyPREFERENCES = "shopKeeperPref";
    public static final String PREF_PROCESSING = "order_to_processing";
    public static final String PREF_SHIPING = "order_to_shipping";
    public static final String DUE_ORDER_NOTI_COUNT = "due_order_noti_count";


    public Context _ctx;
    SharedPreferences sharedpreferences;

    public PrefManager(Context _ctx) {
        this._ctx = _ctx;
        sharedpreferences = _ctx.getSharedPreferences(
                Constant.SHARED_PREF, Context.MODE_PRIVATE);
    }

    public void storeSharedValue(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public String getSharedValue(String key) {
        return sharedpreferences.getString(key, "");
    }

    public void clearSharedValue(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();

    }

    public void setDueOrderLocalNotiCount(int count) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(DUE_ORDER_NOTI_COUNT, count);
        editor.commit();
    }

    public int getDueOrderLocalNotiCount() {
        return sharedpreferences.getInt(DUE_ORDER_NOTI_COUNT, 0);
    }
}
