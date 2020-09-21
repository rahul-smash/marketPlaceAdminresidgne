package com.signity.shopkeeperapp.util.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.signity.shopkeeperapp.model.verify.StoreResponse;
import com.signity.shopkeeperapp.model.verify.UserResponse;
import com.signity.shopkeeperapp.util.Constant;

/**
 * Created by Ketan Tetry on 21/09/20.
 */
public class AppPreference {

    private static AppPreference instance;
    private final SharedPreferences mPrefs;

    private AppPreference(Context context) {
        mPrefs = context.getSharedPreferences(PrefConstants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreference getInstance() {
        if (instance == null) {
            throw new RuntimeException("Create instance first");
        }
        return instance;
    }

    public static void createInstance(Context context) {
        if (instance == null) {
            instance = new AppPreference(context);
        }
    }

    public Constant.Mode isLoggedIn() {
        int mode = mPrefs.getInt(PrefConstants.PREF_KEY_USER_LOGGED_IN_MODE, Constant.Mode.NOT_LOGGED_IN.getType());
        return Constant.Mode.values()[mode];
    }

    public void setLoggedIn(Constant.Mode isLoggedIn) {
        mPrefs.edit().putInt(PrefConstants.PREF_KEY_USER_LOGGED_IN_MODE, isLoggedIn.getType()).apply();
    }

    public void saveUser(UserResponse userResponse) {
        setUserId(userResponse.getId());
        setUserName(userResponse.getFullName());
        setOnDuty(userResponse.getOnDuty());
        setUserEmail(userResponse.getEmail());
        setUserMobile(userResponse.getPhone());
    }

    public String getUserMobile() {
        return mPrefs.getString(PrefConstants.PREF_KEY_LOGIN_USER_MOBILE_NUMBER, "");
    }

    public void setUserMobile(String phone) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_LOGIN_USER_MOBILE_NUMBER, phone).apply();
    }

    public String getUserEmail() {
        return mPrefs.getString(PrefConstants.PREF_KEY_LOGIN_USER_EMAIL_ID, "");
    }

    public void setUserEmail(String email) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_LOGIN_USER_EMAIL_ID, email).apply();
    }

    public String getOnDuty() {
        return mPrefs.getString(PrefConstants.PREF_KEY_ON_DUTY, "");
    }

    public void setOnDuty(String onDuty) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_ON_DUTY, onDuty).apply();
    }

    public String getUserName() {
        return mPrefs.getString(PrefConstants.PREF_KEY_USER_NAME, "");
    }

    public void setUserName(String fullName) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_USER_NAME, fullName).apply();
    }

    public String getUserId() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STAFF_ADMIN_ID, "");
    }

    public void setUserId(String id) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STAFF_ADMIN_ID, id).apply();
    }

    public void saveStore(StoreResponse storeResponse) {
        setStoreId(storeResponse.getId());
        setStoreName(storeResponse.getStoreName());
        setLocation(storeResponse.getLocation());
        setTimeZone(storeResponse.getTimezone());
        setLatitude(storeResponse.getLat());
        setLongitude(storeResponse.getLng());
        setCurrency(storeResponse.getCurrency());
        setStoreType(storeResponse.getType());
    }

    public String getCurrency() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_CURRENCY, "");
    }

    public void setCurrency(String currency) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_CURRENCY, currency).apply();
    }

    public String getLongitude() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_LONGITUDE, "");
    }

    public void setLongitude(String lng) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_LONGITUDE, lng).apply();
    }

    public String getLatitude() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_LATITUDE, "");
    }

    public void setLatitude(String lat) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_LATITUDE, lat).apply();
    }

    public String getTimeZone() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_TIMEZONE, "");
    }

    public void setTimeZone(String timezone) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_TIMEZONE, timezone).apply();
    }

    public String getLocation() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_LOCATION, "");
    }

    public void setLocation(String location) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_LOCATION, location).apply();
    }

    public String getStoreName() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_NAME, "");
    }

    public void setStoreName(String storeName) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_NAME, storeName).apply();
    }

    public String getStoreId() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_ID, "");
    }

    public void setStoreId(String id) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_ID, id).apply();
    }

    public String getStoreType() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_TYPE, "");
    }

    public void setStoreType(String type) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_TYPE, type).apply();
    }
}
