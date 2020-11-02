package com.signity.shopkeeperapp.model.customers.detail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerDetailResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataResponse data;

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

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "CustomerDetailResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }
}