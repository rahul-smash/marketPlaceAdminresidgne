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

        if (userResponse == null) {
            return;
        }

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
        setStoreUrl(storeResponse.getStoreUrl());
        setPhoneCode(storeResponse.getPhoneCode());
        setBrandId(storeResponse.getBrandId());
    }

    public String getCurrency() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_CURRENCY, "INR");
    }

    public void setCurrency(String currency) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_CURRENCY, currency).apply();
    }

    public String getPhoneCode() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_PHONE_CODE, "91");
    }

    public void setPhoneCode(String phoneCode) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_PHONE_CODE, phoneCode).apply();
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

    public String getBrandId() {
        return mPrefs.getString(PrefConstants.PREF_KEY_BRAND_ID, "");
    }

    public void setBrandId(String id) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_BRAND_ID, id).apply();
    }

    public String getStoreType() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_TYPE, "");
    }

    public void setStoreType(String type) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_TYPE, type).apply();
    }

    public String getStoreUrl() {
        return mPrefs.getString(PrefConstants.PREF_KEY_STORE_URL, "");
    }

    public void setStoreUrl(String url) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_STORE_URL, url).apply();
    }

    public String getDeviceToken() {
        return mPrefs.getString(PrefConstants.PREF_KEY_DEVICE_TOKEN, "");
    }

    public void setDeviceToken(String type) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_DEVICE_TOKEN, type).apply();
    }

    public void clearAll() {
        mPrefs.edit().clear().apply();
    }

    public int getNotificationCount() {
        return mPrefs.getInt(PrefConstants.PREF_KEY_NOTIFICATION_COUNT, 0);
    }

    public void setNotificationCount(int count) {
        mPrefs.edit().putInt(PrefConstants.PREF_KEY_NOTIFICATION_COUNT, count).apply();
    }

    public String getLoginType() {
        return mPrefs.getString(PrefConstants.PREF_KEY_LOGIN_TYPE, "mobile");
    }

    public void setLoginType(String type) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_LOGIN_TYPE, type).apply();
    }

    public String getFacebookPageAccessToken() {
        return mPrefs.getString(PrefConstants.PREF_KEY_FACEBOOK_ACCESS_TOKEN, null);
    }

    public void setFacebookPageAccessToken(String access_token) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_FACEBOOK_ACCESS_TOKEN, access_token).apply();
    }

    public String getFacebookPageId() {
        return mPrefs.getString(PrefConstants.PREF_KEY_FACEBOOK_PAGE_ID, null);
    }

    public void setFacebookPageId(String id) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_FACEBOOK_PAGE_ID, id).apply();
    }

    public void setTwilioFcmToken(boolean value) {
        mPrefs.edit().putBoolean(PrefConstants.PREF_KEY_IS_TWILIO_FCM_TOKEN_SET, value).apply();
    }

    public boolean isSetTwilioFcmToken() {
        return mPrefs.getBoolean(PrefConstants.PREF_KEY_IS_TWILIO_FCM_TOKEN_SET, false);
    }

    public boolean isPrivateChannelCreated() {
        return mPrefs.getBoolean(PrefConstants.PREF_KEY_IS_PRIVATE_CHANNEL_CREATED, false);
    }

    public void setPrivateChannelCreated(boolean value) {
        mPrefs.edit().putBoolean(PrefConstants.PREF_KEY_IS_PRIVATE_CHANNEL_CREATED, value).apply();
    }

    public boolean isPrivateChannelJoined() {
        return mPrefs.getBoolean(PrefConstants.PREF_KEY_IS_PRIVATE_CHANNEL_JOINED, false);
    }

    public void setPrivateChannelJoined(boolean value) {
        mPrefs.edit().putBoolean(PrefConstants.PREF_KEY_IS_PRIVATE_CHANNEL_JOINED, value).apply();
    }

    public boolean isOnBoardingShown() {
        return mPrefs.getBoolean(PrefConstants.PREF_KEY_IS_ONBOARDING_SHOWN, false);
    }

    public void setOnBoardingShown(boolean value) {
        mPrefs.edit().putBoolean(PrefConstants.PREF_KEY_IS_ONBOARDING_SHOWN, value).apply();
    }

    public String getNotificationRing() {
        return mPrefs.getString(PrefConstants.PREF_KEY_NOTIFICATION_RING, null);
    }

    public void setNotificationRing(String ringUri) {
        mPrefs.edit().putString(PrefConstants.PREF_KEY_NOTIFICATION_RING, ringUri).apply();
    }

    public int getChannelId() {
        return mPrefs.getInt(PrefConstants.PREF_KEY_CHANNEL_ID, 0);
    }

    public void setChannelId(int channelId) {
        mPrefs.edit().putInt(PrefConstants.PREF_KEY_CHANNEL_ID, channelId).apply();
    }

    public boolean isRegisterMarketStore() {
        return mPrefs.getBoolean(PrefConstants.PREF_KEY_MARKET_STORE_REGISTER, false);
    }

    public void setRegisterMarketStore(boolean value) {
        mPrefs.edit().putBoolean(PrefConstants.PREF_KEY_MARKET_STORE_REGISTER, value).apply();
    }
}
