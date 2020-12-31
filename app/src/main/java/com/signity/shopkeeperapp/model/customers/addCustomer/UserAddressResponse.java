package com.signity.shopkeeperapp.model.customers.addCustomer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserAddressResponse implements Serializable {

    @SerializedName("softdelete")
    private String softdelete;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("email")
    private String email;

    @SerializedName("area_id")
    private String areaId;

    @SerializedName("area_name")
    private String areaName;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zipcode")
    private String zipcode;

    @SerializedName("modified")
    private String modified;

    @SerializedName("created")
    private String created;

    @SerializedName("id")
    private String id;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lng")
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getSoftdelete() {
        return softdelete;
    }

    public void setSoftdelete(String softdelete) {
        this.softdelete = softdelete;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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
                "UserAddressResponse{" +
                        "softdelete = '" + softdelete + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",store_id = '" + storeId + '\'' +
                        ",first_name = '" + firstName + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",email = '" + email + '\'' +
                        ",area_id = '" + areaId + '\'' +
                        ",area_name = '" + areaName + '\'' +
                        ",address = '" + address + '\'' +
                        ",city = '" + city + '\'' +
                        ",state = '" + state + '\'' +
                        ",zipcode = '" + zipcode + '\'' +
                        ",modified = '" + modified + '\'' +
                        ",created = '" + created + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}