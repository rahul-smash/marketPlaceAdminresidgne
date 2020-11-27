package com.signity.shopkeeperapp.model.customers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomersResponse implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("status")
    private String status;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("login_status")
    private String loginStatus;

    @SerializedName("area")
    private String area;

    @SerializedName("active_orders")
    private int activeOrders;

    @SerializedName("total_orders")
    private int totalOrders;

    @SerializedName("paid_amount")
    private String paidAmount;

    @SerializedName("customer_address")
    private String customerAddress;

    @SerializedName("created")
    private String createdDate;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

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

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(int activeOrders) {
        this.activeOrders = activeOrders;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Override
    public String toString() {
        return
                "CustomersResponse{" +
                        "id = '" + id + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",status = '" + status + '\'' +
                        ",profile_image = '" + profileImage + '\'' +
                        ",login_status = '" + loginStatus + '\'' +
                        ",area = '" + area + '\'' +
                        ",active_orders = '" + activeOrders + '\'' +
                        ",total_orders = '" + totalOrders + '\'' +
                        ",paid_amount = '" + paidAmount + '\'' +
                        "}";
    }
}