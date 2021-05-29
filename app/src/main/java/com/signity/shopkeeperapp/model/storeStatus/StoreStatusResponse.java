package com.signity.shopkeeperapp.model.storeStatus;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreStatusResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private DataResponse data;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StoreStatusResponse{" +
                "success=" + success +
                ", data=" + data +
                ", message=" + message +
                '}';
    }
}