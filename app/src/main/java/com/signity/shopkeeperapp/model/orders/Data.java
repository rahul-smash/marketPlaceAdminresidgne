package com.signity.shopkeeperapp.model.orders;

import com.google.gson.annotations.SerializedName;
import com.signity.shopkeeperapp.model.customers.detail.CustomerAddressResponse;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

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

    @SerializedName("loyalityPoints")
    private int loyalityPoints;

    @SerializedName("customer_address")
    private List<CustomerAddressResponse> customerAddress;

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

    public int getLoyalityPoints() {
        return loyalityPoints;
    }

    public void setLoyalityPoints(int loyalityPoints) {
        this.loyalityPoints = loyalityPoints;
    }

    public List<CustomerAddressResponse> getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(List<CustomerAddressResponse> customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "id = '" + id + '\'' +
                        ",full_name = '" + fullName + '\'' +
                        ",phone = '" + phone + '\'' +
                        ",email = '" + email + '\'' +
                        ",status = '" + status + '\'' +
                        ",profile_image = '" + profileImage + '\'' +
                        ",loyalityPoints = '" + loyalityPoints + '\'' +
                        ",customer_address = '" + customerAddress + '\'' +
                        "}";
    }
}