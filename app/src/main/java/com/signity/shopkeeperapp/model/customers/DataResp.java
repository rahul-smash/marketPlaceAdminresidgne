package com.signity.shopkeeperapp.model.customers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataResp implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("zone")
    private String zone;

    @SerializedName("ispickup_add")
    private String ispickupAdd;

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

    @SerializedName("name")
    private String name;

    @SerializedName("geofence")
    private String geofence;

    @SerializedName("radius")
    private String radius;

    @SerializedName("radius_circle")
    private String radiusCircle;

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("state")
    private String state;

    @SerializedName("city_id")
    private String cityId;

    @SerializedName("minamount")
    private String minamount;

    @SerializedName("not_allow")
    private boolean notAllow;

    @SerializedName("charges")
    private String charges;

    @SerializedName("note")
    private String note;

    @SerializedName("created")
    private String created;

    @SerializedName("modified")
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getIspickupAdd() {
        return ispickupAdd;
    }

    public void setIspickupAdd(String ispickupAdd) {
        this.ispickupAdd = ispickupAdd;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeofence() {
        return geofence;
    }

    public void setGeofence(String geofence) {
        this.geofence = geofence;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getRadiusCircle() {
        return radiusCircle;
    }

    public void setRadiusCircle(String radiusCircle) {
        this.radiusCircle = radiusCircle;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getMinamount() {
        return minamount;
    }

    public void setMinamount(String minamount) {
        this.minamount = minamount;
    }

    public boolean isNotAllow() {
        return notAllow;
    }

    public void setNotAllow(boolean notAllow) {
        this.notAllow = notAllow;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return
                "DataResp{" +
                        "id = '" + id + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",zone = '" + zone + '\'' +
                        ",ispickup_add = '" + ispickupAdd + '\'' +
                        ",pickup_add = '" + pickupAdd + '\'' +
                        ",pickup_phone = '" + pickupPhone + '\'' +
                        ",pickup_email = '" + pickupEmail + '\'' +
                        ",pickup_lat = '" + pickupLat + '\'' +
                        ",pickup_lng = '" + pickupLng + '\'' +
                        ",name = '" + name + '\'' +
                        ",geofence = '" + geofence + '\'' +
                        ",radius = '" + radius + '\'' +
                        ",radius_circle = '" + radiusCircle + '\'' +
                        ",country_id = '" + countryId + '\'' +
                        ",state = '" + state + '\'' +
                        ",city_id = '" + cityId + '\'' +
                        ",minamount = '" + minamount + '\'' +
                        ",not_allow = '" + notAllow + '\'' +
                        ",charges = '" + charges + '\'' +
                        ",note = '" + note + '\'' +
                        ",created = '" + created + '\'' +
                        ",modified = '" + modified + '\'' +
                        "}";
    }
}