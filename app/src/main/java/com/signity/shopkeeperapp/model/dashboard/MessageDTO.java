package com.signity.shopkeeperapp.model.dashboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageDTO implements Serializable {

    @SerializedName("app_version")
    private String appVersion;

    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("device_model")
    private String deviceModel;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("device_brand")
    private String deviceBrand;

    @SerializedName("device_token")
    private String deviceToken;

    @SerializedName("device_os_version")
    private String deviceOsVersion;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("device_os")
    private String deviceOs;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("logged_date_time")
    private String loggedDateTime;

    @SerializedName("id")
    private String id;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    public void setDeviceOsVersion(String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getLoggedDateTime() {
        return loggedDateTime;
    }

    public void setLoggedDateTime(String loggedDateTime) {
        this.loggedDateTime = loggedDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "MessageDTO{" +
                        "app_version = '" + appVersion + '\'' +
                        ",device_id = '" + deviceId + '\'' +
                        ",device_model = '" + deviceModel + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",device_brand = '" + deviceBrand + '\'' +
                        ",device_token = '" + deviceToken + '\'' +
                        ",device_os_version = '" + deviceOsVersion + '\'' +
                        ",device_type = '" + deviceType + '\'' +
                        ",device_os = '" + deviceOs + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",logged_date_time = '" + loggedDateTime + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}