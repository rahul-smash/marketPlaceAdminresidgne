package com.signity.shopkeeperapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CalculateShippingResponse implements Serializable {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("shipping")
    private String shipping;

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
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

    @Override
    public String toString() {
        return
                "CommonResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}