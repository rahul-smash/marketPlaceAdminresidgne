package com.signity.shopkeeperapp.model.customers.addCustomer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressResponse extends UserAddressResponse implements Serializable {

    @SerializedName("UserAddress")
    private UserAddressResponse userAddress;

    public UserAddressResponse getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddressResponse userAddress) {
        this.userAddress = userAddress;
    }

    @Override
    public String toString() {
        return
                "AddressResponse{" +
                        "userAddress = '" + userAddress + '\'' +
                        "}";
    }
}