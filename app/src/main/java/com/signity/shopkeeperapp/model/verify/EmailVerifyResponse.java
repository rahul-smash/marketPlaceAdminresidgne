package com.signity.shopkeeperapp.model.verify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EmailVerifyResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<StoreResponse> store;

    @SerializedName("User")
    @Expose
    private UserResponse user;

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoreResponse> getStore() {
        return store;
    }

    public void setStore(List<StoreResponse> store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return
                "OtpVerifyResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",store = '" + store + '\'' +
                        "}";
    }
}