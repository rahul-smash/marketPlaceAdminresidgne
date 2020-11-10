package com.signity.shopkeeperapp.model.orders.storeAddress;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AreaDTO implements Serializable {

    @SerializedName("area_id")
    private String areaId;

    @SerializedName("area_name")
    private String areaName;

    @SerializedName("pickup_add")
    private String pickupAdd;

    @SerializedName("pickup_phone")
    private String pickupPhone;

    @SerializedName("pickup_email")
    private String pickupEmail;

    @SerializedName("pickup_lat")
    private String pickupLat;

    @SerializedName("pickup_lng")
    private String pickupLng;

    @SerializedName("city_id")
    private String cityId;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("min_order")
    private String minOrder;

    @SerializedName("charges")
    private String charges;

    @SerializedName("note")
    private String note;

    @SerializedName("not_allow")
    private boolean notAllow;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPickupAdd() {
        return pickupAdd;
    }

    public void setPickupAdd(String pickupAdd) {
        this.pickupAdd = pickupAdd;
    }

    public String getPickupPhone() {
        return pickupPhone;
    }

    public void setPickupPhone(String pickupPhone) {
        this.pickupPhone = pickupPhone;
    }

    public String getPickupEmail() {
        return pickupEmail;
    }

    public void setPickupEmail(String pickupEmail) {
        this.pickupEmail = pickupEmail;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(String pickupLng) {
        this.pickupLng = pickupLng;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(String minOrder) {
        this.minOrder = minOrder;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isNotAllow() {
        return notAllow;
    }

    public void setNotAllow(boolean notAllow) {
        this.notAllow = notAllow;
    }

    @Override
    public String toString() {
        return
                "AreaDTO{" +
                        "area_id = '" + areaId + '\'' +
                        ",area_name = '" + areaName + '\'' +
                        ",pickup_add = '" + pickupAdd + '\'' +
                        ",pickup_phone = '" + pickupPhone + '\'' +
                        ",pickup_email = '" + pickupEmail + '\'' +
                        ",pickup_lat = '" + pickupLat + '\'' +
                        ",pickup_lng = '" + pickupLng + '\'' +
                        ",city_id = '" + cityId + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",min_order = '" + minOrder + '\'' +
                        ",charges = '" + charges + '\'' +
                        ",note = '" + note + '\'' +
                        ",not_allow = '" + notAllow + '\'' +
                        "}";
    }
}