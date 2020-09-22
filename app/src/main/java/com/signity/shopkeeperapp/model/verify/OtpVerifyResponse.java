package com.signity.shopkeeperapp.model.verify;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OtpVerifyResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("store")
    private List<StoreResponse> store;

    @SerializedName("data")
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "OtpVerifyResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",store = '" + store + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }
}