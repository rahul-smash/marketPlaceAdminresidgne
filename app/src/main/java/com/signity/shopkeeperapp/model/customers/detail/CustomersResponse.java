package com.signity.shopkeeperapp.model.customers.detail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomersResponse implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("status")
    private String status;

    @SerializedName("profile_image")
    private String profileImage;

    @SerializedName("customer_address")
    private String customerAddress;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return
                "CustomersResponse{" +
                        "id = '" + id + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",email = '" + email + '\'' +
                        ",status = '" + status + '\'' +
                        ",profile_image = '" + profileImage + '\'' +
                        "}";
    }
}