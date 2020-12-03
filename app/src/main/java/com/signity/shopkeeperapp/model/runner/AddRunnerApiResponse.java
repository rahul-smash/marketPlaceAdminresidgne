package com.signity.shopkeeperapp.model.runner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddRunnerApiResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataApiResponse data;

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

    public DataApiResponse getData() {
        return data;
    }

    public void setData(DataApiResponse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "AddRunnerApiResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",data = '" + data + '\'' +
                        "}";
    }
}