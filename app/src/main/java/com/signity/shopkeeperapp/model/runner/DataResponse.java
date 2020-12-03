package com.signity.shopkeeperapp.model.runner;

import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.model.OrdersListModel;

import java.io.Serializable;
import java.util.List;

public class DataResponse implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("status")
    private String status;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("social_platform")
    private String socialPlatform;

    @SerializedName("created")
    private String created;

    @SerializedName("area")
    private List<AreaResponse> area;

    @SerializedName("orders")
    private List<OrdersListModel> orders;

    @SerializedName("actie_order")
    private int actieOrder;

    @SerializedName("profile_image_100_80")
    private String profileImage10080;

    @SerializedName("profile_image_300_200")
    private String profileImage300200;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSocialPlatform() {
        return socialPlatform;
    }

    public void setSocialPlatform(String socialPlatform) {
        this.socialPlatform = socialPlatform;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<AreaResponse> getArea() {
        return area;
    }

    public void setArea(List<AreaResponse> area) {
        this.area = area;
    }

    public List<OrdersListModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersListModel> orders) {
        this.orders = orders;
    }

    public int getActieOrder() {
        return actieOrder;
    }

    public void setActieOrder(int actieOrder) {
        this.actieOrder = actieOrder;
    }

    public String getProfileImage10080() {
        return profileImage10080;
    }

    public void setProfileImage10080(String profileImage10080) {
        this.profileImage10080 = profileImage10080;
    }

    public String getProfileImage300200() {
        return profileImage300200;
    }

    public void setProfileImage300200(String profileImage300200) {
        this.profileImage300200 = profileImage300200;
    }

    @Override
    public String toString() {
        return
                "DataResponse{" +
                        "id = '" + id + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",email = '" + email + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",status = '" + status + '\'' +
                        ",profile_image = '" + profileImage + '\'' +
                        ",social_platform = '" + socialPlatform + '\'' +
                        ",created = '" + created + '\'' +
                        ",area = '" + area + '\'' +
                        ",orders = '" + orders + '\'' +
                        ",actie_order = '" + actieOrder + '\'' +
                        ",profile_image_100_80 = '" + profileImage10080 + '\'' +
                        ",profile_image_300_200 = '" + profileImage300200 + '\'' +
                        "}";
    }
}